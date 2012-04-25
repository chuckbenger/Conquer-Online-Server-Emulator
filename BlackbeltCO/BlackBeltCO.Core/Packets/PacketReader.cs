using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace BlackBeltCO.Core.Packets
{
    /// <summary>
    /// Used to read packets
    /// </summary>
    public class PacketReader {
       
        private  int    offset;    // Current offset into packet being parsed
        private  byte[] packet;    // Packet being parsed


        public byte[] Packet
        {
            get
            {
                return packet;
            }
            set
            {
                this.packet = value;
            }
        }

        public int Offset
        {
            set
            {
                offset = value;
            }
        }
       
        /// <summary>
        /// Reads a single byte
        /// </summary>
        /// <returns>Returns the byte at the current offset</returns>
        public  byte readByte() {
            return packet[offset++];
        }

        /// <summary>
        /// Reads a 32 bit integer
        /// </summary>
        /// <returns>Returns a 32 bit integer</returns>
        public  int readInt32() {

            if (offset + 4 > packet.Length)
                return 0;

            return ((int) packet[offset++] << 24) | ((int) (packet[offset++] & 0xFF) << 16)
                   | ((int) (packet[offset++] & 0xFF) << 8) | (int) (packet[offset++] & 0xFF);
        }

        
        /// <summary>
        /// Reads a 64 bit integer
        /// </summary>
        /// <returns>Returns a 64 bit integer</returns>
        public  long readInt64() {

            if (offset + 8 > packet.Length)
                return 0;

            return ((long) (packet[offset++] & 0xff) << 56) | ((long) (packet[offset++] & 0xff) << 48)
                   | ((long) (packet[offset++] & 0xff) << 40) | ((long) (packet[offset++] & 0xff) << 32)
                   | ((long) (packet[offset++] & 0xff) << 24) | ((long) (packet[offset++] & 0xff) << 16)
                   | ((long) (packet[offset++] & 0xff) << 8) | ((long) (packet[offset++] & 0xff) << 0);
        }

        /// <summary>
        /// Reads a string of n length
        /// </summary>
        /// <param name="length">The length of the string to read</param>
        /// <returns>Returns the string</returns>
        public  String readString(int length) {

            if (offset + length > packet.Length)
                return null;

            String str = Encoding.ASCII.GetString(packet,offset,length);
            offset += length;

            return str;
        }
    }

}
