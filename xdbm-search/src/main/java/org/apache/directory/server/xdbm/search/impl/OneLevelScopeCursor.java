/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.directory.server.xdbm.search.impl;


import org.apache.directory.server.core.cursor.AbstractCursor;
import org.apache.directory.server.core.cursor.Cursor;
import org.apache.directory.server.core.cursor.InvalidCursorPositionException;
import org.apache.directory.server.xdbm.IndexEntry;
import org.apache.directory.server.xdbm.Store;

import javax.naming.directory.Attributes;


/**
 * A Cursor over entries satisfying one level scope constraints with alias
 * dereferencing considerations when enabled during search.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$
 */
public class OneLevelScopeCursor extends AbstractCursor<IndexEntry<?, Attributes>>
{
    private static final String UNSUPPORTED_MSG =
        "Scope Cursors are not ordered and do not support positioning by element.";

    /** A onelevel ScopeNode Evaluator */
    private final OneLevelScopeEvaluator evaluator;

    /** A Cursor over the entries in the scope of the search base */
    private final Cursor<IndexEntry<Long,Attributes>> scopeCursor;

    /** A Cursor over entries brought into scope by alias dereferencing */
    private final Cursor<IndexEntry<Long,Attributes>> dereferencedCursor;

    /** Currently active Cursor: we switch between two cursors */
    private Cursor<IndexEntry<Long,Attributes>> cursor;

    /** Whether or not this Cursor is positioned so an entry is available */
    private boolean available = false;


    /**
     * Creates a Cursor over entries satisfying one level scope criteria.
     *
     * @param db the entry store
     * @param evaluator an IndexEntry (candidate) evaluator
     * @throws Exception on db access failures
     */
    public OneLevelScopeCursor( Store<Attributes> db, OneLevelScopeEvaluator evaluator ) throws Exception
    {
        this.evaluator = evaluator;
        scopeCursor = db.getOneLevelIndex().forwardCursor( evaluator.getBaseId() );

        if ( evaluator.isDereferencing() )
        {
            dereferencedCursor = db.getOneAliasIndex().forwardCursor( evaluator.getBaseId() );
        }
        else
        {
            dereferencedCursor = null;
        }
    }


    public boolean available()
    {
        return available;
    }


    public void before( IndexEntry<?, Attributes> element ) throws Exception
    {
        throw new UnsupportedOperationException( UNSUPPORTED_MSG );
    }


    public void after( IndexEntry<?, Attributes> element ) throws Exception
    {
        throw new UnsupportedOperationException( UNSUPPORTED_MSG );
    }


    public void beforeFirst() throws Exception
    {
        cursor = scopeCursor;
        cursor.beforeFirst();
        available = false;
    }


    public void afterLast() throws Exception
    {
        if ( evaluator.isDereferencing() )
        {
            cursor = dereferencedCursor;
        }
        else
        {
            cursor = scopeCursor;
        }

        cursor.afterLast();
        available = false;
    }


    public boolean first() throws Exception
    {
        beforeFirst();
        return next();
    }


    public boolean last() throws Exception
    {
        afterLast();
        return previous();
    }


    public boolean previous() throws Exception
    {
        // if the cursor has not been set - position it after last element
        if ( cursor == null )
        {
            afterLast();
        }

        available = cursor.previous();

        // if we're using the scopeCursor (1st Cursor) then return result as is
        if ( cursor == scopeCursor )
        {
            return available;
        }

        /*
         * Below here we are using the dereferencedCursor so if nothing is
         * available after an advance backwards we need to switch to the
         * scopeCursor and try a previous call after positioning past it's 
         * last element.
         */
        if ( ! available )
        {
            cursor = scopeCursor;
            cursor.afterLast();
            return available = cursor.previous();
        }

        return true;
    }


    public boolean next() throws Exception
    {
        // if the cursor hasn't been set position it before the first element
        if ( cursor == null )
        {
            beforeFirst();
            return scopeCursor.next();
        }

        available = cursor.next();

        // if we're using dereferencedCursor (2nd) then we return the result
        if ( cursor == dereferencedCursor )
        {
            return available;
        }

        /*
         * Below here we are using the scopeCursor so if nothing is
         * available after an advance forward we need to switch to the
         * dereferencedCursor and try a previous call after positioning past
         * it's last element.
         */
        if ( ! available )
        {
            if ( dereferencedCursor != null )
            {
                cursor = dereferencedCursor;
                cursor.beforeFirst();
                return available = cursor.next();
            }

            return false;
        }

        return true;
    }


    public IndexEntry<Long, Attributes> get() throws Exception
    {
        if ( available )
        {
            return cursor.get();
        }

        throw new InvalidCursorPositionException( "Cursor has not been positioned yet." );
    }


    public boolean isElementReused()
    {
        return scopeCursor.isElementReused() ||
            ( dereferencedCursor != null && dereferencedCursor.isElementReused() );
    }
}