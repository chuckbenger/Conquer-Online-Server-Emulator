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
import conquerboxgame.packets.PacketReader;
import conquerboxgame.structures.ChatTypes;
import conquerboxgame.structures.Locations;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 *
 * @author chuck
 */
public class ChatHandler
{
    
    /**
     * Handles chat packets sent from the client
     * @param client the client
     * @param buffer the buffer holding the packet
     */
    public static void handleChat(Client client, ChannelBuffer buffer)
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
        
        if(message.startsWith("/"))
            CommandHandler.handleCommand(client, message);
        
        
        switch((int)chatType)
        {
            case ChatTypes.TALK:
                //conquerboxgame.ConquerBoxGame.dump(buffer.array());
               // client.send(Chat.build(chatColor,id, ChatTypes.TALK,from , to, suffix, message));
                break;
        }
    }
}
