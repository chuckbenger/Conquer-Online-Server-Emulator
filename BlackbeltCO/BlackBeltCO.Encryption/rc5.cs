using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace BlackBeltCO.Encryption
{

    public sealed class RC5Exception : Exception
    {
        public RC5Exception(string message) : base(message) { }
    }

    public sealed class RC5
    {
        private readonly uint[] _bufKey = new uint[4];
        private readonly uint[] _bufSub = new uint[26];

        public RC5(byte[] data)
        {
            if (data.Length != 16) throw new RC5Exception("Invalid data length. Must be 16 bytes");
            const uint p32 = 0xB7E15163;
            const uint q32 = 0x61C88647;
            uint offsetA = 0, offsetB = 0, A = 0, B = 0;
            for (int i = 0; i < 4; i++)
                _bufKey[i] = (uint)(data[i * 4] + (data[i * 4 + 1] << 8) + (data[i * 4 + 2] << 16) + (data[i * 4 + 3] << 24));
            _bufSub[0] = p32;
            for (int i = 1; i < 26; i++)
            {
                _bufSub[i] = _bufSub[i - 1] - q32;
            }
            for (int s = 1; s <= 78; s++)
            {
                _bufSub[offsetA] = LeftRotate(_bufSub[offsetA] + A + B, 3);
                A = _bufSub[offsetA];
                offsetA = (offsetA + 1) % 0x1A;
                _bufKey[offsetB] = LeftRotate(_bufKey[offsetB] + A + B, (int)(A + B));
                B = _bufKey[offsetB];
                offsetB = (offsetB + 1) % 4;
            }
        }
        public byte[] Decrypt(byte[] data)
        {
            if (data.Length % 8 != 0) throw new RC5Exception("Invalid password length. Must be multiple of 8");
            int nLen = data.Length / 8 * 8;
            if (nLen <= 0) throw new RC5Exception("Invalid password length. Must be greater than 0 bytes.");
            uint[] bufData = new uint[data.Length / 4];
            for (int i = 0; i < data.Length / 4; i++)
                bufData[i] = (uint)(data[i * 4] + (data[i * 4 + 1] << 8) + (data[i * 4 + 2] << 16) + (data[i * 4 + 3] << 24));
            for (int i = 0; i < nLen / 8; i++)
            {
                uint ld = bufData[2 * i];
                uint rd = bufData[2 * i + 1];
                for (int j = 12; j >= 1; j--)
                {
                    rd = RightRotate((rd - _bufSub[2 * j + 1]), (int)ld) ^ ld;
                    ld = RightRotate((ld - _bufSub[2 * j]), (int)rd) ^ rd;
                }
                uint B = rd - _bufSub[1];
                uint A = ld - _bufSub[0];
                bufData[2 * i] = A;
                bufData[2 * i + 1] = B;
            }
            byte[] result = new byte[bufData.Length * 4];
            for (int i = 0; i < bufData.Length; i++)
            {
                result[i * 4] = (byte)bufData[i];
                result[i * 4 + 1] = (byte)(bufData[i] >> 8);
                result[i * 4 + 2] = (byte)(bufData[i] >> 16);
                result[i * 4 + 3] = (byte)(bufData[i] >> 24);
            }
            return result;
        }

        public byte[] Encrypt(byte[] data)
        {
            if (data.Length % 8 != 0) throw new RC5Exception("Invalid password length. Must be multiple of 8");
            int nLen = data.Length / 8 * 8;
            if (nLen <= 0) throw new RC5Exception("Invalid password length. Must be greater than 0 bytes.");
            uint[] bufData = new uint[data.Length / 4];
            for (int i = 0; i < data.Length / 4; i++)
                bufData[i] = (uint)(data[i * 4] + (data[i * 4 + 1] << 8) + (data[i * 4 + 2] << 16) + (data[i * 4 + 3] << 24));
            for (int i = 0; i < nLen / 8; i++)
            {
                uint A = bufData[i * 2];
                uint B = bufData[i * 2 + 1];
                uint le = A + _bufSub[0];
                uint re = B + _bufSub[1];
                for (int j = 1; j <= 12; j++)
                {
                    le = LeftRotate((le ^ re), (int)re) + _bufSub[j * 2];
                    re = LeftRotate((re ^ le), (int)le) + _bufSub[j * 2 + 1];
                }
                bufData[i * 2] = le;
                bufData[i * 2 + 1] = re;
            }
            byte[] result = new byte[bufData.Length * 4];
            for (int i = 0; i < bufData.Length; i++)
            {
                result[i * 4] = (byte)bufData[i];
                result[i * 4 + 1] = (byte)(bufData[i] >> 8);
                result[i * 4 + 2] = (byte)(bufData[i] >> 16);
                result[i * 4 + 3] = (byte)(bufData[i] >> 24);
            }
            return result;
        }

        internal static uint LeftRotate(uint dwVar, int dwOffset)
        {
            return (dwVar << (dwOffset & 0x1F) | dwVar >> 0x20 - (dwOffset & 0x1F));
        }

        internal static uint RightRotate(uint dwVar, int dwOffset)
        {
            return (dwVar >> (dwOffset & 0x1F) | dwVar << 0x20 - (dwOffset & 0x1F));
        }
    }
}


