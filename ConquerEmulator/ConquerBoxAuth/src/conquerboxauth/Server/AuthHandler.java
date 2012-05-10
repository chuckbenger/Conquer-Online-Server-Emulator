package conquerboxauth.Server;


import conquerboxauth.MyLogger;

import conquerboxauth.Server.Packets.AuthResponse;
import conquerboxauth.Server.Packets.PacketReader;
import conquerboxauth.Server.Packets.PacketTypes;
import java.util.HashMap;
import java.util.logging.Level;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;

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

/**
 * AuthHandler is used to actually handle a packet that was received from the client.
 * AuthDecoder is called before this object overridden receive method to make
 * sure entire packet has arrived.
 * @author chuck
 */
public class AuthHandler extends SimpleChannelHandler implements IHandler
{

    private final HashMap<Channel, AuthClient> clientHashMap; //Reference to the connected auth clients
    private final AuthDB db;
 

    /**
     * Creates a new authentication handler for a client
     *
     * @param clientHashMap reference to the hashmap containing the client related to a channel
     */
    public AuthHandler(HashMap<Channel, AuthClient> clientHashMap)
    {
        this.clientHashMap = clientHashMap;
        this.db = new AuthDB();

        //If database connection failed. exit
        if (!db.connect())
        {
            MyLogger.appendLog(Level.SEVERE, "Couldn't connect to the database, shutting down.");
            System.exit(-1);
        }
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception
    {
        AuthDataEvent event = (AuthDataEvent) e.getMessage();

        //Only handle if non null value
        if (event != null)
            handle(event, e.getChannel());
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
    {
        AuthClient client = new AuthClient();

        synchronized (clientHashMap)
        {
            clientHashMap.put(e.getChannel(), client);
        }
    }


    /**
     * Handles the input AuthDataEvent
     * @param event the event to handle
     * @param channel the channel the data was received from
     */
    @Override
    public void handle(AuthDataEvent event, Channel channel)
    {
        PacketReader reader = new PacketReader(event.getBuffer());

        //Skip over the length field since it's checked in decoder
        reader.setOffset(2);

        int packetType = reader.readUnSignedShort();

        switch (packetType)
        {
            /*
             *   Auth Request
             *   0	 ushort          52
             *   2	 ushort          1051
             *   4	 string[16]	 Account_Name
             *   20	 string[16]	 Account_Password
             *   36	 string[16]	 GameServer_Name
             */
            case PacketTypes.AUTH_REQUEST:
                String username = reader.readString(16).trim();
                String password = reader.readString(16).trim();
                String server = reader.readString(16).trim();

                //If valid forward to game server else boot them
                if (db.isUserValid(username, "noob"))
                {
                    byte []packet = AuthResponse.build(db.getAcctId(username), "192.168.1.103", 8080);
                    
                    event.getClient().getClientCryptographer().Encrypt(packet);
                    
                    ChannelBuffer buf = ChannelBuffers.buffer(packet.length);
                    
                    buf.writeBytes(packet);
                    
                    ChannelFuture complete = channel.write(buf);
                    
                    //Close the channel once packet is sent
                    complete.addListener(new ChannelFutureListener() {

                        @Override
                        public void operationComplete(ChannelFuture arg0) throws Exception {
                            arg0.getChannel().close();
                        }
                    });
                    
                } else
                   channel.close();
                    
 
                MyLogger.appendLog(Level.INFO, "Login attempt from " + username + " => " + password + " Server => " + server);
                break;
                
            default:
              MyLogger.appendLog(Level.INFO, "Unkown packet: ID = " + packetType + new String(event.getBuffer()));  
              break;
        }
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
    {
        synchronized (clientHashMap)
        {
            clientHashMap.remove(e.getChannel());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception
    {
        MyLogger.appendException(e.getCause().getStackTrace(),e.toString());
    }
}
