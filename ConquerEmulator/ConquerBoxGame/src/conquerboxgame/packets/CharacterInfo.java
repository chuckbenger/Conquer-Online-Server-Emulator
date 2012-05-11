/**
 * **********************************************************************
 * Copyright 2012 Charles Benger
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * ***************************************************************************
 */
package conquerboxgame.packets;

import conquerboxgame.core.Client;

/**
 * CharacterInfo is used to create a packet that contains a client data
 * @author chuck
 */
public class CharacterInfo 
{
    /**
     *   0	 ushort	 66 + TotalStringLength
     *   2	 ushort	 1006
     *   4	 uint	 Character_ID
     *   8	 uint	 Character_Model
     *   12	 ushort	 Character_HairStyle
     *   16	 uint	 Character_Gold
     *   20	 uint	 Character_Experience
     *   40	 ushort	 Character_Strength
     *   42	 ushort	 Character_Dexterity
     *   44	 ushort	 Character_Vitality
     *   46	 ushort	 Character_Spirit
     *   48	 ushort	 Character_StatPoints
     *   50	 ushort	 Character_HP
     *   52	 ushort	 Character_MP
     *   54	 ushort	 Character_PKPoints
     *   56	 byte	 Character_Level
     *   57	 byte	 Character_Class
     *   59	 byte	 Character_Reborn
     *   60	 bool	 Character_Name_Display
     *   61	 byte	 String_Count
     *   62	 byte	 Character_Name_Length
     *   63	 string	 Character_Name
     *   64 + Pos	 byte	 Spouse_Name_Length
     *   65 + Pos	 string	 Spouse_Name
     * 
     * Creates a new character info packet based on the information from the client
     * @param client the client to create the packet about
     * @return returns the new packet
     */
    public static byte[] build(Client client)
    {
        byte []packet = new byte[66 + client.getName().length()];
        PacketWriter writer = new PacketWriter(packet);
        
        writer.writeUnSignedShort(packet.length);
        writer.writeUnSignedShort(PacketTypes.CHARACTER_DATA);
        writer.writeUnSignedInt(client.getId());
        writer.writeUnSignedInt(client.getModel());
        writer.writeUnSignedShort(client.getHair());
        writer.writeUnSignedInt(client.getGold());
        writer.writeUnSignedInt(client.getExperience());
        writer.writeUnSignedShort(client.getStrength());
        writer.writeUnSignedShort(client.getDexterity());
        writer.writeUnSignedShort(client.getVitality());
        writer.writeUnSignedShort(client.getSprit());
        writer.writeUnSignedShort(client.getStatPoints());
        writer.writeUnSignedShort(client.getHealth());
        writer.writeUnSignedShort(client.getMagic());
        writer.writeUnSignedShort(client.getPkPoints());
        writer.writeUnSignedByte(client.getLevel());
        writer.writeUnSignedByte(client.getPlayerClass());
        writer.writeUnSignedByte(client.getReborn());
        writer.writeUnSignedByte((byte)1);
        writer.writeUnSignedByte((short)client.getName().length());
        writer.writeString(client.getName());
        //spouse goes here
        
        return packet;
    }
}
