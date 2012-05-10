package conquerboxauth.Server.Packets;

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
 * Packet reader provides a class for reading primitive data types from a packet
 */
public class PacketReader
{
    private byte[] packet; //Packet to traverse data from
    private int offset; //Current offset into the packet

    private static final int END_OF_ARRAY = -1;

    /**
     * Creates a new packet reader that encapsulates the input packet.
     * Note a reference to the packet is only taken so you must ensure that it
     * is not concurrently modified
     *
     * @param packet the packet
     */
    public PacketReader(byte[] packet)
    {
        this.packet = packet;
    }

    /**
     * Reads a unsigned 8 bit number or -1 if empty
     *
     * @return returns a unsigned 8 bit number or -1 if not long enough
     */
    public short readUSignedByte()
    {
        if (packet.length < offset + 1)
            return END_OF_ARRAY;

        return (short)(packet[offset++] & 0xFF);
    }

    /**
     * Reads a unsigned 16 bit number or -1 if empty
     *
     * @return returns a unsigned 16 bit number or -1 if not long enough
     */
    public int readUnSignedShort()
    {
        if (packet.length < offset + 2 )
            return END_OF_ARRAY;

        return ((packet[++offset] & 0xFF) << 8 | (packet[offset - 1] & 0xFF));
    }
    
    /**
     * Returns an unsigned 32 bit number or -1 if empty
     * @return  returns a unsigned 32 bit number or -1 if not long enough
     */
    public long readUnsignedInt()
    {
        if (packet.length < offset + 4 )
            return END_OF_ARRAY;
        
        long value = ((packet[offset + 3] & 0xFF) << 24 
                     |(packet[offset + 2] & 0xFF) << 16
                     |(packet[offset + 1] & 0xFF) << 8
                     |(packet[offset    ] & 0xFF)); 
        
        offset += 4;
        
        return value;
    }

    /**
     * Reads a string from the packet or returns null if to long
     *
     * @param length the length of the string to read
     * @return returns the string
     */
    public String readString(int length)
    {
        if (packet.length < offset + length)
            return null;

        byte[] string;

        string = new byte[length];

        System.arraycopy(packet, offset, string, 0, length);

        offset += length;

        return new String(string);
    }

    /**
     * Sets the current read offset into the packet
     * @param offset the new offset
     */
    public void setOffset(int offset)
    {
        this.offset = offset;
    }
}
