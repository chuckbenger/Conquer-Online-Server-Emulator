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
public class Chat 
{
    
  
    /**
     *   0	 ushort	 24 + TotalStringLength
     *   2	 ushort	 1004
     *   4	 uint	 Chat_Colour
     *   8	 uint	 enum("Chat_Type")
     *   12	 uint	 Chat_ID
     *   16	 byte	 String_Count
     *   17	 byte	 String_From_Length
     *   18	 string	 String_From
     *   19 + Pos	 byte	 String_To_Length
     *   20 + Pos	 string	 String_To
     *   21 + Pos	 byte	 String_Suffix_Length
     *   22 + Pos	 string	 String_Suffix
     *   23 + Pos	 byte	 String_Message_Length
     *   24 + Pos	 string	 String_Message
     * 
     * @param color the color of the text
     * @param chatType the chat type
     * @param from the senders name
     * @param to the receivers name
     * @param suffix the message suffix
     * @param message the message
     * @return returns a buffer containing the packet
     */
    public static ChannelBuffer build(long color, long id,long chatType, String from, String to, String suffix, String message)
    {
        PacketWriter writer = new PacketWriter(24 + from.length() + to.length() + message.length() + suffix.length());
        writer.writeUnSignedShort(writer.getBuffer().capacity());
        writer.writeUnSignedShort(PacketTypes.CHAT);
        writer.writeUnSignedInt(color);
        writer.writeUnSignedInt(chatType);
        writer.writeUnSignedInt(id);
        writer.writeUnSignedByte(4);
        writer.writeUnSignedByte(from.length());
        writer.writeString(from);
        writer.writeUnSignedByte(to.length());
        writer.writeString(to);
        writer.writeUnSignedByte(suffix.length());
        writer.writeString(suffix);
        writer.writeUnSignedByte(message.length());
        writer.writeString(message);
      
        
      
        return writer.getBuffer();
    }
}
