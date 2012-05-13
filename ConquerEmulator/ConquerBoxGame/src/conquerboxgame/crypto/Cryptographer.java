package conquerboxgame.crypto;

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
public class Cryptographer
{
    public int      InCounter      = 0;
    public int      OutCounter     = 0;
    private boolean UsingAlternate = false;
    private byte[]  Key3;
    private byte[]  Key4;

    public void Encrpyt(byte[] Packet)
    {
        for (int i = 0; i < Packet.length; i++)
        {
            Packet[i] = (byte) ((Packet[i] & 0xFF) ^ 0xAB);
            Packet[i] = (byte) (((Packet[i] & 0xFF) << 4) | ((Packet[i] & 0xFF) >> 4));
            Packet[i] = (byte) (ConquerKeys.Key2[OutCounter >> 8] ^ (Packet[i] & 0xFF));
            Packet[i] = (byte) (ConquerKeys.Key1[OutCounter & 0xFF] ^ (Packet[i] & 0xFF));
            OutCounter++;
        }
    }

    public void Decrypt(byte[] Packet)
    {
        for (int i = 0; i < Packet.length; i++)
        {
            Packet[i] = (byte) ((Packet[i] & 0xFF) ^ 0xAB);
            Packet[i] = (byte) (((Packet[i] & 0xFF) << 4) | ((Packet[i] & 0xFF) >> 4));

            if (UsingAlternate)
            {
                Packet[i] = (byte) (Key4[InCounter >> 8] ^ (Packet[i] & 0xFF));
                Packet[i] = (byte) (Key3[InCounter & 0xFF] ^ (Packet[i] & 0xFF));
            }
            else
            {
                Packet[i] = (byte) (ConquerKeys.Key2[InCounter >> 8] ^ (Packet[i] & 0xFF));
                Packet[i] = (byte) (ConquerKeys.Key1[InCounter & 0xFF] ^ (Packet[i] & 0xFF));
            }

            InCounter++;
        }
    }

    public int getInCounter()
    {
        return InCounter;
    }

    public void setInCounter(int InCounter)
    {
        this.InCounter = InCounter;
    }

    public void SetKeys(long inKey1, long inKey2)
    {
        byte[] XorKey   = new byte[4];
        long   DWordKey = ((inKey1 + inKey2) ^ 0x4321) ^ inKey1;

        XorKey[0] = (byte) (DWordKey & 0xFF);
        XorKey[1] = (byte) ((DWordKey >> 8) & 0xFF);
        XorKey[2] = (byte) ((DWordKey >> 16) & 0xFF);
        XorKey[3] = (byte) ((DWordKey >> 24) & 0xFF);
        Key3      = new byte[256];
        for (short i = 0; i < 256; i++)
            Key3[i] = (byte) (XorKey[i % 4] ^ ConquerKeys.Key1[i]);

        int IMul = (int) Math.abs(DWordKey * DWordKey);

        DWordKey  = (long) IMul;
        XorKey[0] = (byte) (DWordKey & 0xFF);
        XorKey[1] = (byte) ((DWordKey >> 8) & 0xFF);
        XorKey[2] = (byte) ((DWordKey >> 16) & 0xFF);
        XorKey[3] = (byte) ((DWordKey >> 24) & 0xFF);
        Key4      = new byte[256];
        for (short i = 0; i < 256; i++)
            Key4[i] = (byte) (XorKey[i % 4] ^ ConquerKeys.Key2[i]);
        OutCounter     = 0;
        UsingAlternate = true;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
