package conquerboxgame.packets;

//~--- non-JDK imports --------------------------------------------------------

import java.nio.ByteOrder;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * 
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
 * 
 */

/**
 * Packet reader provides a class for writing primitive data types from a packet
 */
public class PacketWriter
{
    private ChannelBuffer buffer;    // The bufer to write the packet to

    /**
     * Creates a new packet reader that encapsulates the input packet.
     * Note a reference to the packet is only taken so you must ensure that it
     * is not concurrently modified.
     *
     * @param packet the packet
     */
    public PacketWriter(int length)
    {
        // Little endian needs to be used to properly read packets
        buffer = ChannelBuffers.buffer(ByteOrder.LITTLE_ENDIAN, length);
    }

    /**
     * Writes an unsigned byte to the array
     *
     * @param ubyte the byte to write
     */
    public void writeUnSignedByte(int ubyte)
    {
        buffer.writeByte(ubyte & 0xFF);
    }

    /**
     * Writes a unsigned short to the array
     *
     * @param ushort the short to write
     */
    public void writeUnSignedShort(int ushort)
    {
        buffer.writeShort(ushort & 0xFFFF);
    }

    /**
     * Writes a unsigned short to the array
     * space
     *
     * @param ushort the short to write
     */
    public void writeUnSignedInt(long uint)
    {
        buffer.writeInt((int) uint & 0xFFFFFFFF);
    }

    /**
     * Writes a long value to the buffer
     * @param value the value to write
     */
    public void writeLong(long value)
    {
        buffer.writeLong(value);
    }

    /**
     * Writes the input string to the array
     *
     * @param str the string to write
     */
    public void writeString(String str)
    {
        byte[] bytes = str.getBytes();

        for (byte b : bytes)
            buffer.writeByte(b);
    }

    /**
     * Returns a reference to the channel buffer
     * @return returns the channel buffer
     */
    public ChannelBuffer getBuffer()
    {
        return buffer;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
