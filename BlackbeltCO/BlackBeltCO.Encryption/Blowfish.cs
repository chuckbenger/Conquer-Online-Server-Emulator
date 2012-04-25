using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.InteropServices;

namespace BlackBeltCO.Encryption
{//WARNING: This is a super old blowfish code... I'm POSITIVE it can be improved on. It's just what I have handy right now.   
    public class GameCryptography
    {
        Blowfish _blowfish;
        public GameCryptography(byte[] key)
        {
            _blowfish = new Blowfish(BlowfishAlgorithm.CFB64);
            lock(key)
                _blowfish.SetKey(key);
        }

        public void Decrypt(byte[] packet)
        {            
            byte[] buffer = _blowfish.Decrypt(packet);
            lock(packet)
                System.Buffer.BlockCopy(buffer, 0, packet, 0, buffer.Length);
        }

        public void Encrypt(byte[] packet)
        {
            byte[] buffer = _blowfish.Encrypt(packet);
            lock (packet)
            System.Buffer.BlockCopy(buffer, 0, packet, 0, buffer.Length);
        }

        public Blowfish Blowfish
        {
            get { return _blowfish; }
        }
        public void SetKey(byte[] k)
        {
            _blowfish.SetKey(k);
        }
        public void SetIvs(byte[] i1, byte[] i2)
        {
            _blowfish.EncryptIV = i1;
            _blowfish.DecryptIV = i2;
        }
    }

    public enum BlowfishAlgorithm
    {
        ECB,
        CBC,
        CFB64,
        OFB64,
    };

    public class Blowfish : IDisposable
    {
        [DllImport("libeay32.dll", CallingConvention = CallingConvention.Cdecl)]
        public extern static void CAST_set_key(IntPtr _key, int len, byte[] data);

        [DllImport("libeay32.dll", CallingConvention = CallingConvention.Cdecl)]
        public extern static void CAST_ecb_encrypt(byte[] in_, byte[] out_, IntPtr schedule, int enc);

        [DllImport("libeay32.dll", CallingConvention = CallingConvention.Cdecl)]
        public extern static void CAST_cbc_encrypt(byte[] in_, byte[] out_, int length, IntPtr schedule, byte[] ivec, int enc);

        [DllImport("libeay32.dll", CallingConvention = CallingConvention.Cdecl)]
        public extern static void CAST_cfb64_encrypt(byte[] in_, byte[] out_, int length, IntPtr schedule, byte[] ivec, ref int num, int enc);

        [DllImport("libeay32.dll", CallingConvention = CallingConvention.Cdecl)]
        public extern static void CAST_ofb64_encrypt(byte[] in_, byte[] out_, int length, IntPtr schedule, byte[] ivec, out int num);

        [StructLayout(LayoutKind.Sequential)]
        struct bf_key_st
        {
            [MarshalAs(UnmanagedType.ByValArray, SizeConst = 18)]
            public UInt32[] P;
            [MarshalAs(UnmanagedType.ByValArray, SizeConst = 1024)]
            public UInt32[] S;
        }

        private BlowfishAlgorithm _algorithm;
        private IntPtr _key;
        private byte[] _encryptIv;
        private byte[] _decryptIv;
        private int _encryptNum;
        private int _decryptNum;

        public Blowfish(BlowfishAlgorithm algorithm)
        {
            _algorithm = algorithm;
            _encryptIv = new byte[8] { 0, 0, 0, 0, 0, 0, 0, 0 };
            _decryptIv = new byte[8] { 0, 0, 0, 0, 0, 0, 0, 0 };
            bf_key_st key = new bf_key_st();
            key.P = new UInt32[16 + 2];
            key.S = new UInt32[4 * 256];
            _key = Marshal.AllocHGlobal(key.P.Length * sizeof(UInt32) + key.S.Length * sizeof(UInt32));
            Marshal.StructureToPtr(key, _key, false);
            _encryptNum = 0;
            _decryptNum = 0;
        }

        public void Dispose()
        {
            Marshal.FreeHGlobal(_key);
        }

        public void SetKey(byte[] data)
        {
            _encryptNum = 0;
            _decryptNum = 0;
            CAST_set_key(_key, data.Length, data);
        }

        public byte[] Encrypt(byte[] buffer)
        {
            byte[] ret = new byte[buffer.Length];
            switch (_algorithm)
            {
                case BlowfishAlgorithm.ECB:
                    CAST_ecb_encrypt(buffer, ret, _key, 1);
                    break;
                case BlowfishAlgorithm.CBC:
                    CAST_cbc_encrypt(buffer, ret, buffer.Length, _key, _encryptIv, 1);
                    break;
                case BlowfishAlgorithm.CFB64:
                    CAST_cfb64_encrypt(buffer, ret, buffer.Length, _key, _encryptIv, ref _encryptNum, 1);
                    break;
                case BlowfishAlgorithm.OFB64:
                    CAST_ofb64_encrypt(buffer, ret, buffer.Length, _key, _encryptIv, out _encryptNum);
                    break;
            }
            return ret;
        }

        public byte[] Decrypt(byte[] buffer)
        {
            byte[] ret = new byte[buffer.Length];
            switch (_algorithm)
            {
                case BlowfishAlgorithm.ECB:
                    CAST_ecb_encrypt(buffer, ret, _key, 0);
                    break;
                case BlowfishAlgorithm.CBC:
                    CAST_cbc_encrypt(buffer, ret, buffer.Length, _key, _decryptIv, 0);
                    break;
                case BlowfishAlgorithm.CFB64:
                    CAST_cfb64_encrypt(buffer, ret, buffer.Length, _key, _decryptIv, ref _decryptNum, 0);
                    break;
                case BlowfishAlgorithm.OFB64:
                    CAST_ofb64_encrypt(buffer, ret, buffer.Length, _key, _decryptIv, out _decryptNum);
                    break;
            }
            return ret;
        }

        public byte[] EncryptIV
        {
            get { return _encryptIv; }
            set { System.Buffer.BlockCopy(value, 0, _encryptIv, 0, 8); }
        }

        public byte[] DecryptIV
        {
            get { return _decryptIv; }
            set { System.Buffer.BlockCopy(value, 0, _decryptIv, 0, 8); }
        }
    }
}
