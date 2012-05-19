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
package conquerboxgame.structures;

import conquerboxgame.core.Entity;

/**
 * NPC contains attributes that any npc will have such as position, id, etc
 * @author chuck
 */
public class NPC extends Entity
{
    private final int type;          //the npcs sub type
    private final short interaction; //How the client will interact with the client

    /**
     * Creates a new npc with using the input parameters
     * @param NPC_ID the uid of the npc
     * @param x the npc's x coordinate
     * @param y the npc's y coordinate
     * @param type the npc's type
     * @param direction the direction the npc is facing
     * @param interaction the type of interaction the client has
     */
    public NPC(int NPC_ID, int x, int y, int type, byte direction, short interaction) {
        this.id = NPC_ID;
        this.x = x;
        this.y = y;
        this.type = type;
        this.direction = direction;
        this.interaction = interaction;
    }

   
    public short getInteraction() {
        return interaction;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "NPC{" + "type=" + type + ", interaction=" + interaction + '}' + super.toString();
    }  
}
