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
import conquerboxgame.structures.Locations;

/**
 *
 * @author chuck
 */
public class CommandHandler 
{
    
    /**
     * Handles cat commands from the client
     * @param client the client that sent the command
     * @param message the message
     */
    public static void handleCommand(Client client, String message)
    {
        
        message = message.substring(1, message.length());
        
        //Switch to uppercase and split by spaces
        String args[] = message.toUpperCase().trim().split(" ");
        
        switch(args.length)
        {
            case 1:
            
             //Disconnect client
             if(args[0].equals("DC"))
                client.getChannel().disconnect();
             break;
            
               
            case 2:
                
                //Teleport client
                if(args[0].equals("TELE"))
                {
                    if(args[1].equals("PC"))
                        Client.teleport(client, Locations.TeleportLocation.NIX_SPAWN);
                    else if(args[1].equals("BI"))
                        Client.teleport(client, Locations.TeleportLocation.BIRD_SPAWN);
                    else if(args[1].equals("APE"))
                        Client.teleport(client, Locations.TeleportLocation.APE_SPAWN);
                    else if(args[1].equals("TC"))
                        Client.teleport(client, Locations.TeleportLocation.TWIN_CITY_SPAWN);
                    else if(args[1].equals("DES"))
                        Client.teleport(client, Locations.TeleportLocation.DESERT_SPAWN);
                }
             break;
        }
        
       
    }
}
