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
package org.apache.directory.server.core.configuration;


/**
 * A {@link Configuration} that shuts down ApacheDS.
 *
 * @org.apache.xbean.XBean element="Shutdown"
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class ShutdownConfiguration extends Configuration
{
    private static final long serialVersionUID = 3141844093107051149L;


    /**
     * Creates a new instance.
     */
    public ShutdownConfiguration()
    {
    }


    /**
     * Creates a new instance that operates on the {@link DirectoryService}
     * with the specified ID.
     */
    public ShutdownConfiguration(String instanceId)
    {
        setInstanceId( instanceId );
    }
}
