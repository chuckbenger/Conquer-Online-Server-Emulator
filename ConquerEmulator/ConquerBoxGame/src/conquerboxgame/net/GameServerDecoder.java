/**
 * **********************************************************************
 * Copyright 2012 Charles Benger
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * ***************************************************************************
 */



package conquerboxgame.net;

//~--- non-JDK imports --------------------------------------------------------

import conquerboxgame.core.Client;
import conquerboxgame.core.Kernel;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * GameServerDecoder is used to buffer a packet until the entire packet
 * has been received
 * @author chuck
 */
public class GameServerDecoder extends FrameDecoder
{
    
    /*
     * If null is returned the packet continues to be buffered. If non-null value returned
     * then entire packet has been read.
     */
    @Override
    protected Object decode(ChannelHandlerContext handler, Channel channel, ChannelBuffer buffer) throws Exception
    {
        final Client client;
        byte[]       packet;

        // Get the client related to the channel
        synchronized (Kernel.CLIENTS)
        {
            client = Kernel.CLIENTS.get(channel);
        }

        //Create a new packet and copy over the bytes
        packet = new byte[buffer.readableBytes()];
        buffer.getBytes(0, packet);
        
        int before = client.getCrypt().getInCounter();

        // Decrypt the packet
        synchronized (client)
        {
            client.getCrypt().Decrypt(packet);
        }
        
            
        short packetLength = packet_length(packet);

        // If length is 0 or only partial packet has arived then indicate to keep buffering
        if ((packet.length < packetLength) || (packetLength <= 0))
        {
            //Restore the old dec counter since whole packet wan't read
            client.getCrypt().setInCounter(before);
            return null;
        }
        // Let the buffer know that we read data
        buffer.readerIndex(packet.length);

        return new ServerDataEvent(packet, client);
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


//~ Formatted by Jindent --- http://www.jindent.com
