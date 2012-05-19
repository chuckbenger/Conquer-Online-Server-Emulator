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

import org.jboss.netty.buffer.ChannelBuffer;

/**
 *
 * @author chuck
 */
public class PacketReader 
{
    
    /**
     * Reads a string from a channel buffer of the specified length
     * @param buffer the buffer to read from
     * @param length the number of bytes to read
     * @return returns the bytes as a string
     */
    public static String readStringFromBuffer(ChannelBuffer buffer, int length)
    {
        
        //Make sure we have enough bytes to read
        if(buffer.readerIndex() + length > buffer.capacity())
            return null;
        
        byte[] data = new byte[length];
        
        buffer.readBytes(data, 0, data.length);
        
        return new String(data);
    }
}
