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

import conquerboxgame.core.Client;
import conquerboxgame.packets.PacketWriter;
import conquerboxgame.structures.Locations;
import conquerboxgame.structures.PacketTypes;
import org.jboss.netty.buffer.ChannelBuffer;
import conquerboxgame.structures.Locations.TeleportLocation;

/**
 *
 * @author chuck
 */
public class NpcCommand
{
    public static final short NO_LINK_BACK = 255;
    
    /**
     * NPC command types
     */
    public enum CommandTypes
    {
        NONE    ((byte)0x00),
        DIALOGUE((byte)0x01),
        OPTION  ((byte)0x02),
        INPUT   ((byte)0x03),
        AVATAR  ((byte)0x04),
        FINISH  ((byte)0x64);

        private final byte value;

        CommandTypes(byte value) 
        {
            this.value = value;
        } 
    }
    
    
    /**
     *   0	 ushort	 16 + Line_Length
     *   2	 ushort	 2032
     *   4	 uint	 Timer
     *   10	 byte	 Link_ID
     *   11	 byte	 Link_Type
     *   12	 byte	 Line_Count
     *   13	 byte	 Line_length
     *   14	 string	 Line
     * @return 
     */
    private static ChannelBuffer build(int linkID, CommandTypes type, String line)
    {
        PacketWriter writer = new PacketWriter(16 + line.length());
        writer.writeUnSignedShort(16 + line.length());
        writer.writeUnSignedShort(PacketTypes.NPC_COMMAND);
        writer.writeUnSignedInt(0);
        writer.writeUnSignedShort(0);
        writer.writeUnSignedByte(linkID);
        writer.writeUnSignedByte(type.value);
        writer.writeUnSignedByte(1);
        writer.writeUnSignedByte(line.length());
        writer.writeString(line);
        writer.writeUnSignedShort(0);
        
        return writer.getBuffer();
    }
    
     public static ChannelBuffer build(int UK1, int ID, int LinkBack, CommandTypes type)
        {
            
            PacketWriter writer = new PacketWriter(16);
            writer.writeUnSignedShort(16);
            writer.writeUnSignedShort(PacketTypes.NPC_COMMAND);
            writer.writeUnSignedInt(UK1);
            writer.writeUnSignedShort(ID);
            writer.writeUnSignedByte(LinkBack);
            writer.writeUnSignedByte(type.value);
            writer.writeUnSignedInt(0);//Uknonwn
            
            return writer.getBuffer();
        }
    
   
    public static void handleDialog(Client client, long id, byte linkBack)
    {
        
        //Npc id is not given in 2032 packet so use the last id from
        //the last interaction
        if(id != 0)
            client.setLastNpcID(id);
        else
            id = client.getLastNpcID();
        
        switch((int)(id & 0xFFFFFFFF))
        {
           
            //TC wharehouse
            case 8:
             if(linkBack == 0)
             {
                 say("FUCK YOU",client);
                 link("NO", linkBack, client);
                 face(15, client);
                 finish(client);
             }
             break;
                
            //TC conductress
            case 10050:
                handleConductress(id, client, linkBack);
                break;
            
            //BI conductress
            case 10056:
                handleConductress(id, client, linkBack);
                break;
                
            //PC conductress
            case 10052:
                handleConductress(id, client, linkBack);
                break;
                
            //Ape conductress
            case 10053:
                handleConductress(id, client, linkBack);
                break;
            
            //Desert conductress
            case 10051:
                handleConductress(id, client, linkBack);
                break;
           
            //Market conductress
            case 45:
              handleConductress(id, client, linkBack);  
              break;
            
            //Merchant Qian
            case 10054:
                if(linkBack == 0)
                {
                    say("Hello brave warrior. Would you like to go to desert city?", client);
                    link("Yes", 1, client);
                    link("No", NO_LINK_BACK, client);
                    face(10, client);
                    finish(client);
                }
                else if(linkBack == 1)
                {
                    Client.teleport(client, TeleportLocation.DESERT_FROM_TWIN_CITY);
                }
                break;
                
            default:
              System.out.println("No npc handler for " + id);
              break;
        }
    }
    
    /**
     * Send dialog to the client
     * @param text the text the client will receive
     * @param client the client to send to
     */
    private static void say(String text, Client client)
    {
        client.send(build(NO_LINK_BACK, CommandTypes.DIALOGUE, text));
    }
    
    /**
     * Sends a option for the dialog sent to the client
     * @param text the option text
     * @param linkID the link id that the client will respond with so we can determine their choice
     * @param client the client to send to
     */
    private static void link(String text, int linkID, Client client)
    {
        client.send(build(linkID, CommandTypes.OPTION, text));
    }
    
    
    /**
     * Sets the face that the client will see
     * @param face the face id
     * @param client the client to send to
     */
    private static void face(int face, Client client)
    {
        client.send(build(2544, face, NO_LINK_BACK, CommandTypes.AVATAR));
    }
    
    /**
     * End the dialog session and let the client respond
     * @param client the client to send to
     */
    private static void finish(Client client)
    {
        client.send(build(0,0,NO_LINK_BACK,CommandTypes.FINISH));
    }
    
    
    private static void handleConductress(long id, Client client, byte linkBack)
    {
       
         if(linkBack == 0)
                {
                    if(id == 45)
                    {
                        say("Would you like to leave the market?", client);
                        link("Yes", 1,client);
                        link("No",NO_LINK_BACK, client);
                        face(63, client);
                        finish(client);
                    }
                    else
                    {
                        say("Hello wanderer, I'm the conductress. For only 1000 silver I can teleport you to your destination.", client);
                  
                    
                        if(id == 10050)
                        {
                            link("Bird island", 1, client);
                            link("Ape city", 2, client);
                            link("Desert City", 3, client);
                            link("Phoneix Castle", 4, client);

                        }
                        else 
                            link("Twin City", 6, client);

                        link("Market", 5,client);
                        link("No thanks", NO_LINK_BACK, client);
                        face(1, client);
                        finish(client);
                     }
                }
                else if(id == 10050)
                { 
                    if(linkBack == 1)
                        Client.teleport(client, TeleportLocation.TWIN_CITY_TO_BIRD);
                    else if(linkBack == 2)
                        Client.teleport(client, TeleportLocation.TWIN_CITY_TO_APE);
                    else if(linkBack == 3)
                        Client.teleport(client, TeleportLocation.TWIN_CITY_TO_DESERT);
                    else if(linkBack == 4)
                         Client.teleport(client, TeleportLocation.TWIN_CITY_TO_NIX);
                    else if(linkBack == 5)
                        Client.teleport(client, TeleportLocation.MARKET);
                }
                //Market conductress
                else if(id == 45)
                {
                    if(linkBack == 1)
                    {
                        TeleportLocation location = null;
                        
                        switch(client.getPrevMap())
                        {
                            case 1002:
                                location = TeleportLocation.TWIN_CITY_SPAWN;
                                break;
                                
                            case 1015:
                                location = TeleportLocation.BIRD_SPAWN;
                                break;
                                
                            case 1011:
                                location = TeleportLocation.NIX_SPAWN;
                                break;
                                
                            case 1020:
                                location = TeleportLocation.APE_SPAWN;
                                break;
                                
                            case 1000:
                                location = TeleportLocation.DESERT_SPAWN;
                                break;
                        }
                        if(location != null)
                            Client.teleport(client, location);
                    }
                }
                else if(linkBack == 5)
                    Client.teleport(client, TeleportLocation.MARKET);
                else if(linkBack == 6)
                {
                    //BI conductress
                    if(id == 10056)
                        Client.teleport(client, TeleportLocation.BIRD_FROM_TWIN_CITY);
                    
                    //PI conductress
                    else if(id == 10052)
                        Client.teleport(client, TeleportLocation.NIX_FROM_TWIN_CITY);
                    
                    //Ape condcutress
                    else if(id == 10053)
                        Client.teleport(client, TeleportLocation.APE_FROM_TWIN_CITY);
                    
                    //Desert conductress
                    else if(id == 10051)
                        Client.teleport(client, TeleportLocation.DESERT_FROM_TWIN_CITY);
                }
    }
}
