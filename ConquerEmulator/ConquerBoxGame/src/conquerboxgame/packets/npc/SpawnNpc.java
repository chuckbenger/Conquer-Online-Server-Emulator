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
package conquerboxgame.packets.npc;

import conquerboxgame.packets.PacketWriter;
import conquerboxgame.structures.PacketTypes;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * SpawnNpc is used to generate a packet that will spawn 
 * an npc in the input location.
 * @author chuck
 */
public class SpawnNpc {
    
    
    /**
     * 0	 ushort	 20
     * 2	 ushort	 2030
     * 4	 uint	 NPC_ID
     * 8	 ushort	 NPC_CordX
     * 10	 ushort	 NPC_CordY
     * 12	 ushort	 NPC_Type
     * 14        ushort  NPC_Direction
     * 16	 ushort	 NPC_Interaction
     * @param id the id of the npc
     * @param x the x coordinate of the npc
     * @param y the y coordinate of the npc
     * @param type the npc's subtype
     * @param direction the direction the npc is facing
     * @param interaction how the client will interact
     * @return returns returns a new channel buffer containing the packet
     */
    public static ChannelBuffer build(long id, int x, int y, int type, int direction, int interaction)
    {
     PacketWriter writer = new PacketWriter(20);
        writer.writeUnSignedShort(20);
        writer.writeUnSignedShort(PacketTypes.NPC_SPAWN);
        writer.writeUnSignedInt(id);
        writer.writeUnSignedShort(x);
        writer.writeUnSignedShort(y);
        writer.writeUnSignedShort(type);
        writer.writeUnSignedShort(direction);
        writer.writeUnSignedInt(interaction);

        return writer.getBuffer();
    }
    
}
