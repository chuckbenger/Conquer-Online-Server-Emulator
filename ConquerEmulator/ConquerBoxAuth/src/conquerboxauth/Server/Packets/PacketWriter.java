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
 * Packet reader provides a class for writing primitive data types from a packet
 */
public class PacketWriter
{
    private byte[] packet; //Packet to traverse data from
    private int offset; //Current offset into the packet


    /**
     * Creates a new packet reader that encapsulates the input packet.
     * Note a reference to the packet is only taken so you must ensure that it
     * is not concurrently modified
     *
     * @param packet the packet
     */
    public PacketWriter(byte[] packet)
    {
        this.packet = packet;
    }

    /**
     * Writes an unsigned byte to the array or returns if not enough space
     *
     * @param ubyte the byte to write
     */
    public void writeUnSignedByte(short ubyte)
    {
        //Make sure there's room in array
        if(offset + 1 > packet.length)
            return;
        
        packet[offset++] = (byte) (ubyte & 0xFF);
    }

    /**
     * Writes a unsigned short to the array or returns if not enough space
     *
     * @param ushort the short to write
     */
    public void writeUnSignedShort(int ushort)
    {
        //Make sure there's room in array
        if(offset + 2 > packet.length)
            return;
        
        packet[offset++] = (byte) (ushort & 0xFF);
        packet[offset++] = (byte) ((ushort >> 8) & 0xFF);
    }

    /**
     * Writes a unsigned short to the array or returns if not enough
     * space
     *
     * @param ushort the short to write
     */
    public void writeUnSignedInt(long ushort)
    {
        //Make sure there's room in array
        if(offset + 4 > packet.length)
            return;
        
        packet[offset++] = (byte)  (ushort & 0xFF);
        packet[offset++] = (byte) ((ushort >> 8) & 0xFF);
        packet[offset++] = (byte) ((ushort >> 16) & 0xFF);
        packet[offset++] = (byte) ((ushort >> 24) & 0xFF);
    }
   

    /**
     * Writes the input string to the array or returns if there's
     * not enough space
     *
     * @param str the string to write
     */
    public void writeString(String str)
    {
        //Make sure there's enough rom
        if(offset + str.length() > packet.length)
            return;
        
        byte[] bytes = str.getBytes();

        for (byte b : bytes)
            packet[offset++] = b;
    }

    /**
     * Sets the offset into the array
     * @param offset the new array offset
     */
    public void setOffset(int offset)
    {
        this.offset = offset;
    }
}
