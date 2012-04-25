using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace BlackBeltCO.Encryption
{
    public sealed class LoginCipher: IConquerCipher
    {
        private struct CryptCounter
        {
            public CryptCounter(ushort with)
            {
                Counter = with;
            }
            ushort Counter;

            public byte Key2
            {
                get { return (byte)(Counter >> 8); }
            }

            public byte Key1
            {
                get { return (byte)(Counter & 0xFF); }
            }

            public void Increment()
            {
                Counter++;
            }
        }

        private CryptCounter DecryptCounter;
        private CryptCounter EncryptCounter;
        private static byte[] CryptKey1;
        private static byte[] CryptKey2;

        static LoginCipher()
        {
            if (CryptKey1 != null)
            {
                if (CryptKey1.Length != 0)
                    return;
            }
            CryptKey1 = new byte[0x100];
            CryptKey2 = new byte[0x100];
            byte i_key1 = 0x9D;
            byte i_key2 = 0x62;
            for (int i = 0; i < 0x100; i++)
            {
                CryptKey1[i] = i_key1;
                CryptKey2[i] = i_key2;
                i_key1 = (byte)((0x0F + (byte)(i_key1 * 0xFA)) * i_key1 + 0x13);
                i_key2 = (byte)((0x79 - (byte)(i_key2 * 0x5C)) * i_key2 + 0x6D);
            }
        }
        
        public LoginCipher()
        {
            EncryptCounter = new CryptCounter();
            DecryptCounter = new CryptCounter();
        }
        public void Encrypt(byte[] buffer, int length)
        {
            for (int i = 0; i < length; i++)
            {
                buffer[i] ^= (byte)0xAB;
                buffer[i] = (byte)(buffer[i] >> 4 | buffer[i] << 4);
                buffer[i] ^= (byte)(CryptKey1[EncryptCounter.Key1] ^ CryptKey2[EncryptCounter.Key2]);
                EncryptCounter.Increment();
            }
        }

        public void Decrypt(byte[] buffer, int length)
        {
            for (int i = 0; i < length; i++)
            {
                buffer[i] ^= (byte)0xAB;
                buffer[i] = (byte)(buffer[i] >> 4 | buffer[i] << 4);
                buffer[i] ^= (byte)(CryptKey2[DecryptCounter.Key2] ^ CryptKey1[DecryptCounter.Key1]);
                DecryptCounter.Increment();
            }
        }
    }
}
