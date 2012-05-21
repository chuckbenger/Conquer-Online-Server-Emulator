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
package conquerboxgame.net.handlers;

import conquerboxgame.MyLogger;
import conquerboxgame.core.Client;
import conquerboxgame.core.CoMath;
import conquerboxgame.core.Kernel;
import conquerboxgame.packets.GeneralUpdate;
import conquerboxgame.structures.GeneralTypes;
import conquerboxgame.structures.Locations;
import java.util.logging.Level;
import org.jboss.netty.buffer.ChannelBuffer;
import conquerboxgame.structures.Locations.TeleportLocation;

/**
 *
 * @author chuck
 */
public class GeneralHandler 
{
    /**
     * Handles general packet types
     * @param client the client
     * @param buffer the buffer holding the packet
     */
    public static void handleGeneralPacket(Client client, ChannelBuffer buffer)
    {
        long timer = buffer.readInt();
        long other = System.currentTimeMillis() / 10000L;
        long id    = buffer.readInt();
        int  x;
        int  y;

        if(id != client.getCharacterId())
            return;
        
        buffer.readerIndex(24);

        int subtype = buffer.readInt();

        switch (subtype)
        {
            /**
            * Request the clients position
            */
            case GeneralTypes.POS_REQUEST :
                client.send(GeneralUpdate.build(client.getCharacterId(), client.getX(), client.getY(), 0, 0,
                                                client.getMap(), 0, GeneralTypes.POS_REQUEST));
                break;

            case GeneralTypes.AVATAR :
                    buffer.readerIndex(20);
                    client.setX(buffer.readUnsignedShort());
                    client.setY(buffer.readUnsignedShort());
                    client.send(GeneralUpdate.build(client.getCharacterId(), client.getX(), client.getY(), 0, 0,0, 0, GeneralTypes.AVATAR));
                    Client.spawnNpcs(client);
                break;

            case GeneralTypes.PORTAL:
                conquerboxgame.ConquerBoxGame.dump(buffer.array());
                TeleportLocation location = CoMath.getPortal(client, Kernel.PORTALS.get(client.getMap()));
                
                //If map was found teleport them there. Otherwise back to tc
                if(location != null)
                    Client.teleport(client, location);
                else
                    Client.teleport(client, TeleportLocation.TWIN_CITY_SPAWN);
                break;
                
            case GeneralTypes.RETRIEVE_SURROUNDINGS:
                Client.spawnNpcs(client);
                break;
                
            default :
                MyLogger.appendLog(Level.INFO, "Unkown packet subtype => " + subtype);
                break;
        }
    }
}
