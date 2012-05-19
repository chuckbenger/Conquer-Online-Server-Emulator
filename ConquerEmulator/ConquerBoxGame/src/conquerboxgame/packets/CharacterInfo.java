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

//~--- non-JDK imports --------------------------------------------------------

import conquerboxgame.core.Client;

import conquerboxgame.structures.PacketTypes;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * CharacterInfo is used to create a packet that contains a client data
 * @author chuck
 */
public class CharacterInfo
{
    /**
     *   0       ushort  66 + TotalStringLength
     *   2       ushort  1006
     *   4       uint    Character_ID
     *   8       uint    Character_Model
     *   12      ushort  Character_HairStyle
     *   16      uint    Character_Gold
     *   20      uint    Character_Experience
     *   40      ushort  Character_Strength
     *   42      ushort  Character_Dexterity
     *   44      ushort  Character_Vitality
     *   46      ushort  Character_Spirit
     *   48      ushort  Character_StatPoints
     *   50      ushort  Character_HP
     *   52      ushort  Character_MP
     *   54      ushort  Character_PKPoints
     *   56      byte    Character_Level
     *   57      byte    Character_Class
     *   59      byte    Character_Reborn
     *   60      bool    Character_Name_Display
     *   61      byte    String_Count
     *   62      byte    Character_Name_Length
     *   63      string  Character_Name
     *   64 + Pos        byte    Spouse_Name_Length
     *   65 + Pos        string  Spouse_Name
     *
     * Creates a new character info packet based on the information from the client
     * @param client the client to create the packet about
     * @return returns the new packet
     */
    public static ChannelBuffer build(Client Client)
    {
        PacketWriter Packet = new PacketWriter(63 + Client.getName().length());

        Packet.writeUnSignedShort(Packet.getBuffer().array().length);
        Packet.writeUnSignedShort(PacketTypes.CHARACTER_DATA);
        Packet.writeUnSignedInt(Client.getCharacterId());
        Packet.writeUnSignedInt(Client.getModel());
        Packet.writeUnSignedShort(311);
        Packet.writeUnSignedShort(0);
        Packet.writeUnSignedInt(Client.getGold());
        Packet.writeUnSignedInt(Client.getExperience());
        Packet.writeUnSignedInt(0);
        Packet.writeUnSignedInt(0);
        Packet.writeUnSignedInt(0);
        Packet.writeUnSignedInt(0);
        Packet.writeUnSignedShort(Client.getStrength());
        Packet.writeUnSignedShort(Client.getDexterity());
        Packet.writeUnSignedShort(Client.getVitality());
        Packet.writeUnSignedShort(Client.getSprit());
        Packet.writeUnSignedShort(Client.getStatPoints());
        Packet.writeUnSignedShort(Client.getHealth());
        Packet.writeUnSignedShort(Client.getMagic());
        Packet.writeUnSignedShort(Client.getPkPoints());
        Packet.writeUnSignedByte(Client.getLevel());
        Packet.writeUnSignedByte(Client.getPlayerClass());
        Packet.writeUnSignedByte(1);
        Packet.writeUnSignedByte(Client.getReborn());
        Packet.writeUnSignedByte(1);
        Packet.writeUnSignedByte(2);
        Packet.writeUnSignedByte(Client.getName().length());
        Packet.writeString(Client.getName());

        // spouse goes here
        return Packet.getBuffer();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
