package conquerboxauth.Server.Packets;

import java.util.Random;

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
public class AuthResponse
{
    /**
     *   0	 ushort     32
     *   2	 ushort     1055
     *   4	 uint       Account_ID
     *   8	 uint       Login_Token
     *   12	 byte[16]   GameServer_IPAddress
     *   28	 ushort     GameServer_Port
     * 
     * @param acctId the players account id
     * @param gameIP the ip address of the game server
     * @param port the port number of the server
     * @return returns a new authentication response packet
     */
    public static byte[] build(int acctId, String gameIP, int port)
    {
        byte[] packet = new byte[32];
        PacketWriter writer = new PacketWriter(packet);
        writer.writeUnSignedShort(32);
        writer.writeUnSignedShort(PacketTypes.AUTH_RESPONSE);
        writer.writeUnSignedInt(acctId);
        writer.writeUnSignedInt(new Random(System.nanoTime()).nextInt());
        writer.writeString(gameIP);
        writer.setOffset(28);
        writer.writeUnSignedShort(port);

        return packet;
    }
}
