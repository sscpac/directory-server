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

package org.apache.directory.server.dhcp.options.dhcp;


import java.net.InetAddress;

import org.apache.directory.server.dhcp.options.AddressOption;


/**
 * This option is used in DHCPOFFER and DHCPREQUEST messages, and may
 * optionally be included in the DHCPACK and DHCPNAK messages.  DHCP
 * servers include this option in the DHCPOFFER in order to allow the
 * client to distinguish between lease offers.  DHCP clients use the
 * contents of the 'server identifier' field as the destination address
 * for any DHCP messages unicast to the DHCP server.  DHCP clients also
 * indicate which of several lease offers is being accepted by including
 * this option in a DHCPREQUEST message.
 * 
 * The identifier is the IP address of the selected server.
 * 
 * The code for this option is 54, and its length is 4.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class ServerIdentifier extends AddressOption
{
    public ServerIdentifier()
    {
    }


    public ServerIdentifier(InetAddress localHost)
    {
        setAddress( localHost );
    }


    /*
     * @see org.apache.directory.server.dhcp.options.DhcpOption#getTag()
     */
    public byte getTag()
    {
        return 54;
    }
}
