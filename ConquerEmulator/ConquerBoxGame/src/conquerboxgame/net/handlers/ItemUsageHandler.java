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

import conquerboxgame.core.Client;
import conquerboxgame.packets.ItemUsage;
import conquerboxgame.structures.ItemUsageType;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 *
 * @author chuck
 */
public class ItemUsageHandler 
{
    public static void handleItemUsage(Client client, ChannelBuffer buffer)
    {
        long id = buffer.readUnsignedInt();
        long location = buffer.readUnsignedInt();
        long subtype = buffer.readUnsignedInt();
        long time = buffer.readUnsignedInt();
        
        if(id != client.getCharacterId())
            return;
        
        switch((int)subtype & 0xFFFFFFFF)
        {
            case ItemUsageType.PING:
                    client.send(ItemUsage.build(id, location, subtype, 0));
                    break;
        }
    }
}
