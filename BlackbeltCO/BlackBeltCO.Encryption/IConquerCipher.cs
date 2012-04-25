using System;

namespace BlackBeltCO.Encryption
{
    public interface IConquerCipher
    {
        void Encrypt(byte[] buffer, int len);
        void Decrypt(byte[] buffer, int len);
    }
}
