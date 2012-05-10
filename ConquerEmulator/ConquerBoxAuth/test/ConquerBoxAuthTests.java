
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
import conquerboxauth.Server.Packets.PacketReader;
import conquerboxauth.Server.Packets.PacketWriter;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author chuck
 */
public class ConquerBoxAuthTests {
    
    public ConquerBoxAuthTests() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
       
    }
    
   @Test   
   public void writeByteTest()
   {
       byte []packet = new byte[5];
       PacketWriter writer  = new PacketWriter(packet);
       writer.writeUnSignedByte((short)50);
       
       assertEquals(packet[0], 50);
   }
   
   @Test
   public void writeByteNull()
   {
       byte[] packet = null;
       boolean triggered = false;
       
       try
       {
          PacketWriter writer = new PacketWriter(packet);
          writer.writeUnSignedByte((short)50);
       }
       catch(NullPointerException e)
       {
           triggered = true;
       }
       assertTrue(triggered);
   }
   
   @Test
   public void writeShortTest()
   {
       byte[]packet = new byte[2];
       short value = 500;
       PacketWriter writer = new PacketWriter(packet);
       writer.writeUnSignedShort(value);
       
       PacketReader reader = new PacketReader(packet);
       
       assertEquals(value, reader.readUnSignedShort());
   }
   
   @Test
   public void writeShortTestLowSpace()
   {
       byte[]packet = new byte[1];
       short value = 500;
       PacketWriter writer = new PacketWriter(packet);
       
       
       assertEquals(0,packet[0]);
   }
   
   @Test
   public void writeUINTTest()
   {
       long value = 412321;
       byte[]packet = new byte[4];
       PacketWriter writer = new PacketWriter(packet);
       writer.writeUnSignedInt(value);
       
       PacketReader reader = new PacketReader(packet);
       assertEquals(value, reader.readUnsignedInt());
   }
   
   @Test
   public void writeStringTest()
   {
       String ip = "192.168.1.1";
       byte []packet = new byte[ip.length()];
       PacketWriter writer = new PacketWriter(packet);
       writer.writeString(ip);
       
       PacketReader reader = new PacketReader(packet);
       assertEquals(ip, reader.readString(ip.length()));
   }
   
   @Test
   public void readStringToLong()
   {
       String ip = "192.168.1.1";
       byte []packet = new byte[ip.length()];
       PacketWriter writer = new PacketWriter(packet);
       writer.writeString(ip);
       
       PacketReader reader = new PacketReader(packet);
       assertEquals(null, reader.readString(ip.length() + 1));
   }
}
