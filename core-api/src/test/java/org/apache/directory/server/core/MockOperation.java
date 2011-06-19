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
package org.apache.directory.server.core;

import java.util.Collection;
import java.util.List;

import org.apache.directory.server.core.entry.ClonedServerEntry;
import org.apache.directory.server.core.filtering.BaseEntryFilteringCursor;
import org.apache.directory.server.core.filtering.EntryFilteringCursor;
import org.apache.directory.server.core.interceptor.context.LookupOperationContext;
import org.apache.directory.server.core.interceptor.context.OperationContext;
import org.apache.directory.server.core.interceptor.context.SearchOperationContext;
import org.apache.directory.shared.ldap.model.constants.AuthenticationLevel;
import org.apache.directory.shared.ldap.model.entry.Entry;
import org.apache.directory.shared.ldap.model.entry.Modification;
import org.apache.directory.shared.ldap.model.exception.LdapException;
import org.apache.directory.shared.ldap.model.message.Control;
import org.apache.directory.shared.ldap.model.name.Dn;
import org.apache.directory.shared.ldap.model.schema.SchemaManager;

public class MockOperation implements OperationContext
{
    final int count;
    final CoreSession session;


    public MockOperation( SchemaManager schemaManager, int count ) throws Exception 
    {
        this.count = count;
        this.session = new MockCoreSession( new LdapPrincipal( schemaManager, new Dn( schemaManager ), AuthenticationLevel.STRONG ),
            new MockDirectoryService( count ) );
    }


    public EntryFilteringCursor search( SearchOperationContext searchContext )
        throws LdapException
    {
        return new BaseEntryFilteringCursor( new MockCursor( count ), searchContext );
    }


    public EntryFilteringCursor search( SearchOperationContext searchContext, Collection<String> bypass ) throws LdapException
    {
        return new BaseEntryFilteringCursor( new MockCursor( count ), searchContext );
    }


    public void addRequestControl( Control requestControl )
    {
    }


    public void addRequestControls( Control[] requestControls )
    {
    }


    public void addResponseControl( Control responseControl )
    {
    }


    public Collection<String> getByPassed()
    {
        return null;
    }


    public Dn getDn()
    {
        return null;
    }


    public String getName()
    {
        return null;
    }


    public Control getRequestControl( String numericOid )
    {
        return null;
    }


    public Control getResponseControl( String numericOid )
    {
        return null;
    }


    public int getResponseControlCount()
    {
        return 0;
    }


    public Control[] getResponseControls()
    {
        return null;
    }


    public CoreSession getSession()
    {
        return session;
    }


    public boolean hasBypass()
    {
        return false;
    }


    public boolean hasRequestControl( String numericOid )
    {
        return false;
    }


    public boolean hasRequestControls()
    {
        return false;
    }


    public boolean hasResponseControl( String numericOid )
    {
        return false;
    }


    public boolean hasResponseControls()
    {
        return false;
    }


    public boolean isBypassed( String interceptorName )
    {
        return false;
    }


    public boolean isCollateralOperation()
    {
        return false;
    }


    public ClonedServerEntry lookup( Dn dn, Collection<String> bypass ) throws LdapException
    {
        return null;
    }


    public ClonedServerEntry lookup( LookupOperationContext lookupContext ) throws LdapException
    {
        return null;
    }


    public Entry lookup( Dn dn, Collection<String> byPass, String... attrIds ) throws LdapException
    {
        return null;
    }
    

    public LookupOperationContext newLookupContext( Dn dn )
    {
        return null;
    }


    public void setByPassed( Collection<String> byPassed )
    {
    }


    public void setCollateralOperation( boolean collateralOperation )
    {
    }


    public void setDn( Dn dn )
    {
    }


    public LdapPrincipal getEffectivePrincipal()
    {
        return null;
    }


    public OperationContext getFirstOperation()
    {
        return null;
    }


    public OperationContext getLastOperation()
    {
        return null;
    }


    public OperationContext getNextOperation()
    {
        return null;
    }


    public OperationContext getPreviousOperation()
    {
        return null;
    }


    public boolean isFirstOperation()
    {
        return false;
    }


    public void add( Entry entry, Collection<String> bypass ) throws LdapException
    {
    }


    public void delete( Dn dn, Collection<String> bypass ) throws LdapException
    {
    }


    public void modify( Dn dn, List<Modification> mods, Collection<String> bypass ) throws LdapException
    {
    }


    public boolean hasEntry( Dn dn, Collection<String> byPass ) throws LdapException
    {
        return false;
    }


    public ReferralHandlingMode getReferralHandlingMode()
    {
        return null;
    }


    public void setReferralHandlingMode( ReferralHandlingMode referralHandlingMode )
    {
    }


    public Entry getEntry()
    {
        return null;
    }


    public void setEntry( Entry entry )
    {
    }


    public void throwReferral()
    {
    }
    
    
    public boolean isReferralThrown()
    {
        return false;
    }


    public void ignoreReferral()
    {
    }


    public boolean isReferralIgnored()
    {
        return false;
    }
}
