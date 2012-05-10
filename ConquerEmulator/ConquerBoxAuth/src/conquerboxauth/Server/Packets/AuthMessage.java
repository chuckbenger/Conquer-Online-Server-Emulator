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
public class AuthMessage
{
    /**
     *   0	 ushort	 28
     *   2	 ushort	 1052
     *   4	 uint	 Account_ID
     *   8	 uint	 Login_Token
     *   12	 byte[16]	 Message_Bytes
     * 
     * @param acctId the players account id
     * @param gameIP the ip address of the game server
     * @param port the port number of the server
     * @return returns a new authentication response packet
     */
    public static byte[] build(int acctId, String message)
    {

        if(message.length() > 16)
            message = message.substring(1,16);
        
        byte[] packet = new byte[28];
        PacketWriter writer = new PacketWriter(packet);
        writer.writeUnSignedShort(28);
        writer.writeUnSignedShort(PacketTypes.AUTH_MESSAGE);
        writer.writeUnSignedInt(acctId);
        writer.writeUnSignedInt(System.nanoTime());
        writer.writeString(message);

        return packet;
    }
}
