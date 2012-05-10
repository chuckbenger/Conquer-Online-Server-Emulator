package conquerboxauth;


import conquerboxauth.Server.AuthServer;
import conquerboxauth.Server.Packets.PacketReader;
import conquerboxauth.Server.Packets.PacketWriter;
import org.jboss.netty.channel.SimpleChannelHandler;


/**
 * **********************************************************************
 * Copyright 2012 Charles Benger
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ***************************************************************************
 */


public class Main extends SimpleChannelHandler
{

    /**
     * Entry point
     * @param args 
     */
    public static void main(String args[])
    {
        MyLogger.initLog();
        AuthServer authServer = new AuthServer(9958);
        
       
    }
}
