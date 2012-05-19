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



package conquerboxgame.net;

//~--- non-JDK imports --------------------------------------------------------

import conquerboxgame.packets.npc.SpawnNpc;
import conquerboxgame.MyLogger;

import conquerboxgame.core.Client;
import conquerboxgame.core.CoMath;
import conquerboxgame.core.IHandler;
import conquerboxgame.core.Kernel;

import conquerboxgame.database.Database;

import conquerboxgame.packets.*;

import conquerboxgame.structures.*;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

//~--- JDK imports ------------------------------------------------------------

import java.nio.ByteOrder;
import java.util.ArrayList;

import java.util.logging.Level;

/**
 *
 * @author chuck
 */
public class PacketHandler implements IHandler
{
    /**
     * Handles the input packet
     * @param event the event to handle
     */
    @Override
    public void handle(ServerDataEvent event)
    {
        long   id;
        String response;
        Client client = event.getClient();

        // Create a new channel buffer
        ChannelBuffer buffer = ChannelBuffers.buffer(ByteOrder.LITTLE_ENDIAN, event.getPacket().length);

        buffer.writeBytes(event.getPacket());

        // Discard the length
        buffer.readUnsignedShort();

        // Grab the packet type
        int type = buffer.readUnsignedShort();

        // Switch the packet type
        switch (type)
        {
        /*
         *   0       ushort  28
         *   2       ushort  1052
         *   4       uint    Account_ID
         *   8       uint    Login_Token
         *   12      byte[16]        Message_Bytes
         *
         *   Gets the client token and id and uses them to generate the new decryption key pair.
         *   a response of NEW_ROLE is sent if they don't have an account.
         *   ANSWER_OK is sent if they have an account. Otherwise other response is sent
         */
        case PacketTypes.AUTH_MESSAGE :
            response = "ANSWER_OK";
           
            // Setup the new decryption keys
            client.setId(buffer.readUnsignedInt());
            client.setToken(buffer.readUnsignedInt());
            client.getCrypt().SetKeys(client.getToken(), client.getId());

            // Check if they have a game account already
            if (!Kernel.DATABASE.hasGameAccount(client.getId()))
                response = "NEW_ROLE";
            else
            {
                if (!Kernel.DATABASE.loadCharacterData(client))
                {
                    client.send(AuthMessage.build(0, "SYSTEM", "ALLUSERS", "Failed to load character",
                                                  ChatTypes.LOGININFORMATION));

                    break;
                }
            }

            client.send(AuthMessage.build(0, "SYSTEM", "ALLUSERS", response, ChatTypes.LOGININFORMATION));
            if (response.equals("ANSWER_OK"))
            {
                client.send(CharacterInfo.build(client));
                
            }
            
            break;

        /*
         *   0       ushort  60
         *   2       ushort  1001
         *   4       string[16]      Account_Name
         *   20      string[16]      Character_Name
         *   36      string[16]      Account_Password
         *   52      ushort  Character_Model
         *   54      ushort  Character_Class
         *   56      uint    Account_ID
         *
         *   This packet is sent from the client when it want's to create
         *   a new account.
         */
        case PacketTypes.CHARACTER_CREATION :
            response = "ANSWER_OK";
            buffer.readerIndex(20);

            byte[] name = new byte[16];

            buffer.readBytes(name, 0, name.length);
            client.setName(new String(name).trim());
            buffer.readerIndex(52);
            client.setModel(buffer.readUnsignedShort());
            client.setPlayerClass(buffer.readUnsignedShort());
            if (!Kernel.DATABASE.createCharacter(client))
                response = "Account creation failed";
            client.send(AuthMessage.build(0, "SYSTEM", "ALLUSERS", response, ChatTypes.LOGININFORMATION));
            client.getChannel().close();

            break;

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
         * General data that is requested from the client.
         */
        case PacketTypes.GENERAL_DATA :
            handleGeneralPacket(client, buffer);

            break;

        /**
         * 0     ushort  12
         *   2   ushort  1005
         *   4   uint    Entity_ID
         *   8   byte    Direction
         *   9   bool    Speed
         * This packet is sent when the client is walking
         */
        case PacketTypes.ENTITY_MOVE :
            id = buffer.readUnsignedInt();

            if (id == client.getCharacterId())
            {
                client.setDirection((byte) ((buffer.readByte() & 0xFF) % 8));
                Client.adjustPostionBasedOnDirection(client);
               
            }

            break;

        case PacketTypes.CHAT :
            handleChat(client, buffer);
            break;

        case PacketTypes.NPC_INITIAL:
            conquerboxgame.ConquerBoxGame.dump(buffer.array());
            break;
        default :
            MyLogger.appendLog(Level.INFO, "Unkown packet type => " + type);

            break;
        }
    }

    /**
     * Handles general packet types
     * @param client the client
     * @param buffer the buffer holding the packet
     */
    private void handleGeneralPacket(Client client, ChannelBuffer buffer)
    {
        long timer = buffer.readInt();
        long other = System.currentTimeMillis() / 10000L;
        long id    = buffer.readInt();
        int  x;
        int  y;

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
            if (id == client.getCharacterId())
            {
                buffer.readerIndex(20);
                client.setX(buffer.readUnsignedShort());
                client.setY(buffer.readUnsignedShort());
                client.send(GeneralUpdate.build(client.getCharacterId(), client.getX(), client.getY(), 0, 0,0, 0, GeneralTypes.AVATAR));
                spawnNpcs(client);
            }

            break;

            
        case GeneralTypes.RETRIEVE_SURROUNDINGS:
             //client.send(SpawnNpc.build(1, client.getX(),client.getY(), 10, 1, 2));
         
            spawnNpcs(client);
            break;
        default :
            MyLogger.appendLog(Level.INFO, "Unkown packet subtype => " + subtype);
            break;
        }
    }

    /**
     * Handles chat packets sent from the client
     * @param client the client
     * @param buffer the buffer holding the packet
     */
    private void handleChat(Client client, ChannelBuffer buffer)
    {
        buffer.readerIndex(4);

        byte   toLength;         // The length of the destination name
        byte   stringCount;      // Total number of strings
        byte   suffixLength;     // Length of the suffix
        byte   messageLength;    // The length of the message
        byte   fromLength;       // THe length of the from string
        String from;             // The person that sent the packet
        String to;               // Who the packet is for
        String message;          // The actual message
        String suffix;//The message suffix
        long   chatColor = buffer.readUnsignedInt();
        long   chatType  = buffer.readUnsignedInt();

        long id = buffer.readUnsignedInt();    // Chat id
        id++;
        
        stringCount = buffer.readByte();
        
        //Make sure theres a string
        if (stringCount == 0)
            return;
        
        //Read the sender information
        fromLength = buffer.readByte();
        from = PacketReader.readStringFromBuffer(buffer, fromLength);
        if(from == null)
            return;
        
        //Read the destination field
        toLength = buffer.readByte();
        to = PacketReader.readStringFromBuffer(buffer, toLength);
        if(to == null)
            return;
        
        //Read the suffix
        suffixLength = buffer.readByte();
        suffix = PacketReader.readStringFromBuffer(buffer, suffixLength);
        if(suffix == null)
            return;
        
        //Read the actual message
        messageLength = buffer.readByte();
        message = PacketReader.readStringFromBuffer(buffer, messageLength);
        if(message == null)
            return;
        
        if(message.contains("dc"))
            client.getChannel().disconnect();
      
        
        
        
        switch((int)chatType)
        {
            case ChatTypes.TALK:
                //conquerboxgame.ConquerBoxGame.dump(buffer.array());
               // client.send(Chat.build(chatColor,id, ChatTypes.TALK,from , to, suffix, message));
                break;
        }
    }
    
    /**
     * Spawns npc's that are within the clients view
     * @param client the client to spawn them for
     */
    private void spawnNpcs(Client client)
    {
         ArrayList<NPC> npcs = Kernel.NPC_MAP.get(client.getMap());
         for(NPC npc : npcs)
         {
             if(!client.getMyNpcs().contains(npc))
             {
                if(CoMath.getDistance(npc, client) < Rules.DISTANCE_RENDER)
                {
                    client.getMyNpcs().add(npc);
                    client.send(SpawnNpc.build(npc.getId(),  npc.getX(),npc.getY(),npc.getType(), npc.getDirection() , npc.getInteraction()));
                }
            }
         }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
