/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.directory.server.core.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.directory.server.core.factory.DefaultDirectoryServiceFactory;


/**
 * An anntation for the DirectoryService builder
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(
    { ElementType.METHOD, ElementType.TYPE })
public @interface CreateDS
{
    /** @return The Factory to use to create a DirectoryService */
    Class<?> factory() default DefaultDirectoryServiceFactory.class;


    /** @return The DS name */
    String name() default "defaultDS";


    /** @return flag to enable/disable access control, default is false */
    boolean enableAccessControl() default false;


    /** @return flag to enable/disable anonymous access, default is false */
    boolean allowAnonAccess() default false;


    /** @return flag to enable/disable changelog, default is true */
    boolean enableChangeLog() default true;


    /** @return The list of partitions to create */
    CreatePartition[] partitions() default
        {};


    /** @return additional interceptors */
    Class<?>[] additionalInterceptors() default
        {};


    /** @return authenticators, when empty the default authenticators are used, else this must contain the complete list */
    CreateAuthenticator[] authenticators() default
        {};


    /** @return The loaded schemas */
    LoadSchema[] loadedSchemas() default
        {};
}
