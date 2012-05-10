package conquerboxauth.Cryptography;

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
public class Assembler
{
    public static int rollLeft(long Value, byte Roll, byte Size)
    {
        Roll = (byte) ((Roll & 0xFF) & 0x1f);
        return (int) ((((Value << (((Roll & 0xFF)) & 0x1F)) & 0xFFFFFFFFL)) | ((Value & 0xFFFFFFFFL) >> ((Size & 0xFF) - (Roll & 0xFF))));
    }

    public static int rollRight(long Value, byte Roll, byte Size)
    {
        Roll = (byte) ((Roll & 0xFF) & 0x1f);
        return (int) ((((Value << ((((Size & 0xFF) - (Roll & 0xFF))) & 0x1F)) & 0xFFFFFFFFL)) | ((Value & 0xFFFFFFFFL) >> (Roll & 0xFF)));
    }
}
