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

//~--- non-JDK imports --------------------------------------------------------

import conquerboxgame.structures.PacketTypes;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 *
 * @author chuck
 */
public class AuthMessage
{
    /**
     * 
     * @param MessageID
     * @param from
     * @param to
     * @param message
     * @param CType
     * @return 
     */
    public static ChannelBuffer build(int MessageID, String from, String to, String message, int CType)
    {
        PacketWriter writer = new PacketWriter(21 + from.length() + to.length() + message.length());
        
        writer.writeUnSignedShort(writer.getBuffer().array().length);
        writer.writeUnSignedShort(PacketTypes.CHAT);
        writer.writeUnSignedByte(222);
        writer.writeUnSignedByte(222);
        writer.writeUnSignedByte(222);
        writer.writeUnSignedByte(0);
        writer.writeUnSignedInt(CType);
        writer.writeUnSignedInt(MessageID);
        writer.writeUnSignedByte(4);
        writer.writeUnSignedByte(from.length());
        writer.writeString(from);
        writer.writeUnSignedByte(to.length());
        writer.writeString(to);
        writer.writeUnSignedByte(0);
        writer.writeUnSignedByte(message.length());
        writer.writeString(message);
        
        return writer.getBuffer();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
