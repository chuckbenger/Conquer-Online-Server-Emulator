using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace BlackBeltCO.Core.Packets
{
    
    /// <summary>
    /// Used for writing packets to a byte array
    /// </summary>
    public class PacketWriter {
        private  int    offset;    // Current offset into packet being inserted
        private  byte[] packet;    // Packet being parsed

       /// <summary>
       /// Writes a 32 bit integer to the buffer
       /// </summary>
       /// <param name="value">The int to write</param>
        public  void writeInt32(int value) {
            packet[offset++] = (byte) (value >> 24);
            packet[offset++] = (byte) (value >> 16);
            packet[offset++] = (byte) (value >> 8);
            packet[offset++] = (byte) (value);
        }

        /// <summary>
        /// Writes a byte to the buffer
        /// </summary>
        /// <param name="value">The byte to write</param>
        public  void writeByte(byte value) {
            packet[offset++] = value;
        }

       /// <summary>
       /// Writes a 64 bit integer to the buffer
       /// </summary>
       /// <param name="value">The long to write</param>
        public  void writeInt64(long value) {
            packet[offset++] = (byte) (value >> 56);
            packet[offset++] = (byte) (value >> 48);
            packet[offset++] = (byte) (value >> 40);
            packet[offset++] = (byte) (value >> 32);
            packet[offset++] = (byte) (value >> 24);
            packet[offset++] = (byte) (value >> 16);
            packet[offset++] = (byte) (value >> 8);
            packet[offset++] = (byte) (value);
        }

        /// <summary>
        /// Converts a string to bytes and writes n number of those
        /// bytes to the buffer
        /// </summary>
        /// <param name="value">The string to convert</param>
        /// <param name="max">The max number of bytes to write</param>
        public  void writeString(String value, int max) {
            byte[] bytes = Encoding.ASCII.GetBytes(value);
            
            Buffer.BlockCopy(bytes, 0, packet, offset, max);

            offset += bytes.Length;
        } 
    }

}
