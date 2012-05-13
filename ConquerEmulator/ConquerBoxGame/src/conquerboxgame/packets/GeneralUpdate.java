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

import conquerboxgame.structures.GeneralTypes;
import conquerboxgame.structures.PacketTypes;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 *
 * @author chuck
 */
public class GeneralUpdate
{
    /**
     *   0       ushort  28
     *   2       ushort  1010
     *   4       uint    Timer
     *   8       uint    Entity_ID
     *   12      ushort  Value_A
     *   14      ushort  Value_B
     *   16      ushort  Value_C
     *   20      ushort  Value_D
     *   22      ushort  Value_E
     *   24      uint   Data_Type
     *
     *
     * This packet is sent to the World Server and to the client to manage
     *   general actions such as walking, jumping, changing directions, and
     *   other actions.
     * @param id the character id to update
     * @param parm1
     * @param parm2
     * @param parm3
     * @param parm4
     * @param parm5
     * @param parm6
     * @param type the subtype of the packet
     * @see GeneralTypes
     * @return returns a new channel buffer containing the packet
     */
    public static ChannelBuffer build(long id, int parm1, int parm2, int parm3, int parm4, int parm5, int parm6,
                                      int type)
    {
        PacketWriter Packet = new PacketWriter(28);

        Packet.writeUnSignedShort(Packet.getBuffer().array().length);
        Packet.writeUnSignedShort(PacketTypes.GENERAL_DATA);
        Packet.writeUnSignedInt(System.currentTimeMillis() / 10000L);
        Packet.writeUnSignedInt(id);
        Packet.writeUnSignedShort(parm1);
        Packet.writeUnSignedShort(parm2);
        Packet.writeUnSignedShort(parm3);
        Packet.writeUnSignedShort(parm4);
        Packet.writeUnSignedShort(parm5);
        Packet.writeUnSignedShort(parm6);
        Packet.writeUnSignedInt(type);

        // spouse goes here
        return Packet.getBuffer();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
