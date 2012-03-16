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
package org.apache.directory.server.core.shared.txn.logedit;


import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.UUID;

import org.apache.directory.server.core.api.partition.Partition;
import org.apache.directory.server.core.api.partition.index.Index;
import org.apache.directory.server.core.api.partition.index.ParentIdAndRdn;
import org.apache.directory.server.core.api.txn.logedit.IndexModification;


/**
 * A Change class for index modification
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class IndexChange implements IndexModification
{
    /** Index this change is done on */
    private transient Index<?> index;

    /** oid of the attribute the index is on */
    private String oid;

    /** key of the forward index */
    private Object key;

    /** id for the index */
    private UUID id;

    /** Change type */
    private Type type;

    /** Whether the index is a system index. False is user index */
    private boolean isSystemIndex;


    // For externalizable
    public IndexChange()
    {
    }


    /**
     * Create a new IndexChange instance.
     * 
     * @param index The changed index
     * @param key The index' key
     * @param id The entry's UUID
     * @param type The change type, Add or Delete
     * @param isSystemIndex Tells f the index is a system index or a user index
     */
    public IndexChange( Index<?> index, Object key, UUID id, Type type, boolean isSystemIndex )
    {
        this.index = index;
        this.oid = index.getAttributeId();
        this.key = key;
        this.id = id;
        this.type = type;
        this.isSystemIndex = isSystemIndex;
    }


    public String getOID()
    {
        return oid;
    }


    public Index<?> getIndex()
    {
        return index;
    }


    public Object getKey()
    {
        return key;
    }


    public UUID getID()
    {
        return id;
    }


    public Type getType()
    {
        return type;
    }


    /**
     * {@inheritDoc}
     */
    public void applyModification( Partition partition, boolean recovery ) throws Exception
    {
        Index<Object> index = ( Index<Object> ) partition.getIndex( oid );

        if ( index == null )
        {
            // TODO decide how to handle index add drop
        }

        if ( type == Type.ADD )
        {
            // During recovery, index might have already been added.
            // But it should not hurt to read the index entry.
            index.add( key, id );
        }
        else
        // delete
        {
            if ( recovery == false )
            {
                index.drop( key, id );
            }
            else
            {
                //If forward or reverse index entry existence diffes, first add the index entry and then delete it.
                boolean forwardExists = index.forward( key, id );
                boolean reverseExists = index.reverse( id, key );

                if ( forwardExists != reverseExists )
                {
                    // We assume reading the same entry to an index wont hurt
                    index.add( key, id );

                    index.drop( key, id );
                }
                else if ( forwardExists )
                {
                    // Index entry exists both for reverse and forward index
                    index.drop( key, id );
                }
            }
        }
    }


    @Override
    public void readExternal( ObjectInput in ) throws IOException, ClassNotFoundException
    {
        oid = in.readUTF();
        KeyType keyType = KeyType.values()[in.readByte()];

        switch ( keyType )
        {
            case STRING:
                key = in.readUTF();
                break;

            case LONG:
                key = in.readLong();
                break;

            case BYTES:
                int length = in.readInt();
                key = new byte[length];
                in.read( ( byte[] ) key );
                break;

            case RDN:
                ParentIdAndRdn parentIdAndRdn = new ParentIdAndRdn();
                parentIdAndRdn.readExternal( in );
                key = parentIdAndRdn;
                break;

            case UUID:
                long mostSignificantBits = in.readLong();
                long leastSignificantBits = in.readLong();
                UUID uuid = new UUID( mostSignificantBits, leastSignificantBits );
                key = uuid;
                break;

            case OBJECT:
                key = in.readObject();
                break;
        }

        id = UUID.fromString( in.readUTF() );
        type = Type.values()[in.readInt()];
    }


    @Override
    public void writeExternal( ObjectOutput out ) throws IOException
    {
        out.writeUTF( oid );

        if ( key instanceof String )
        {
            out.write( KeyType.STRING.ordinal() );
            out.writeUTF( ( String ) key );
        }
        else if ( key instanceof byte[] )
        {
            out.write( KeyType.BYTES.ordinal() );
            out.writeInt( ( ( byte[] ) key ).length );
            out.write( ( byte[] ) key );
        }
        else if ( key instanceof Long )
        {
            out.write( KeyType.LONG.ordinal() );
            out.writeLong( ( Long ) key );
        }
        else if ( key instanceof ParentIdAndRdn )
        {
            out.write( KeyType.RDN.ordinal() );
            ( ( ParentIdAndRdn ) key ).writeExternal( out );
        }
        else if ( key instanceof UUID )
        {
            out.write( KeyType.UUID.ordinal() );
            UUID uuid = ( UUID ) key;
            out.writeLong( uuid.getMostSignificantBits() );
            out.writeLong( uuid.getLeastSignificantBits() );
        }
        else
        {
            out.write( KeyType.OBJECT.ordinal() );
            out.writeObject( key );
        }

        out.writeUTF( id.toString() );
        out.writeInt( type.ordinal() );
    }

    public enum Type
    {
        ADD,
        DELETE
    }


    /**
     * @see Object#toString()
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append( "IndexChange '" );

        // The index's name
        sb.append( index.getAttributeId() ).append( "': " );

        // The change' type
        sb.append( "<" ).append( type ).append( ", " );

        // The entry's UUID
        sb.append( id ).append( ", " );

        // The key
        sb.append( key );

        sb.append( ">" );

        return sb.toString();
    }

    private enum KeyType
    {
        STRING,
        LONG,
        BYTES,
        RDN,
        UUID,
        OBJECT;
    }
}
