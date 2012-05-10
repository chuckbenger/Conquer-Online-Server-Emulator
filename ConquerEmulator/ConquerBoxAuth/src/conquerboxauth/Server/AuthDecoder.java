package conquerboxauth.Server;

import conquerboxauth.MyLogger;
import java.util.HashMap;
import java.util.logging.Level;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

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
 * Used to buffer packets until the entire packet is read.
 */
public class AuthDecoder extends FrameDecoder
{
    private final HashMap<Channel, AuthClient> clientHashMap;  //Reference to the connected auth clients

    /**
     * Creates a new authentication decoder for a client
     *
     * @param clientHashMap reference to the hashmap containing the client related to a channel
     */
    public AuthDecoder(HashMap<Channel, AuthClient> clientHashMap)
    {
        this.clientHashMap = clientHashMap;
    }

    /**
     * If entire packet hasn't been read return null otherwise return a channel buffer containing
     * the data to process. Null returned keeps buffering the data until there is more to read
     *
     * @return return null or data
     */
    @Override
    protected Object decode(ChannelHandlerContext channelHandlerContext, Channel channel, ChannelBuffer channelBuffer) throws Exception
    {
        AuthClient client;
        byte[] packet;

        if (channelBuffer.readableBytes() < Short.SIZE)
            return null;

        //Get the associated client auth object
        synchronized (clientHashMap)
        {
            client = clientHashMap.get(channel);
        }

        packet = new byte[channelBuffer.readableBytes()];

        channelBuffer.getBytes(0, packet);

        //Decrypt the packet.
        synchronized(client)
        {
          client.getClientCryptographer().Decrypt(packet);
        }
      
        int packetLength = packet_length(packet);

        //If length is 0 or only partial packet has arived then indicate to keep buffering
        if (packet.length < packetLength || packetLength < 0)
            return null;

        channelBuffer.readerIndex(packet.length);
        
        return new AuthDataEvent(client, packet);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception
    {
        MyLogger.appendException(e.getCause().getStackTrace(),e.toString());
    }

    /**
     * Returns the total size of a packet.
     *
     * @param packet the packet to check the size of
     * @return returns the length of the packet in bytes
     */
    private static short packet_length(byte[] packet)
    {
        return (short) ((short) (packet[1] & 0xFF) << 8 | (short) (packet[0] & 0xFF));
    }
}
