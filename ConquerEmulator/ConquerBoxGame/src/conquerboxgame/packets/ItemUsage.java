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

import conquerboxgame.structures.PacketTypes;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 *
 * @author chuck
 */
public class ItemUsage 
{
    
    /**
     * 0	 ushort	 20
        2	 ushort	 1009
        4	 uint	 Item_ID
        8	 uint	 Item_Location
        12	 uint	 Item_UsageType
        16	 uint	 Timer
     * @param id
     * @param parm1
     * @param parm2
     * @param parm3
     * @param parm4
     * @param parm5
     * @param parm6
     * @param type
     * @return 
     */
     public static ChannelBuffer build(long id, long location, long type, long timer)
    {
        PacketWriter Packet = new PacketWriter(20);
        Packet.writeUnSignedShort(Packet.getBuffer().array().length);
        Packet.writeUnSignedShort(PacketTypes.ITEM_USAGE);
        Packet.writeUnSignedInt(id);
        Packet.writeUnSignedInt(location);
        Packet.writeUnSignedInt(type);
        Packet.writeUnSignedInt(timer);

        // spouse goes here
        return Packet.getBuffer();
    }
}
