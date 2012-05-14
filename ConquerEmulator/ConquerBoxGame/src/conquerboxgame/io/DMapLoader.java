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



package conquerboxgame.io;

//~--- non-JDK imports --------------------------------------------------------

import conquerboxgame.MyLogger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//~--- JDK imports ------------------------------------------------------------

import java.io.*;

import java.nio.ByteOrder;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 *
 * @author chuck
 */
public class DMapLoader
{
    private static final byte                       HEIGHT      = 0x7C;
    private static final short                      HEIGHT_SIGN = 0x1 << 7;
    private static final HashMap<Integer, byte[][]> MAPS        = new HashMap<>();    // Holds all the game tiles for a map id

    // Flags
    private static final byte PORTAL = 0x1 << 1;
    private static final byte VALID  = 0x1;

    /**
     * Loads the maps from the specified path
     * @param path the path containing the maps
     */
    public static void load(String path) throws ParserConfigurationException, SAXException, IOException
    {
        File mapConfig = new File(path);
        long before    = System.currentTimeMillis();

        // First check if the map file exists
        if (!mapConfig.exists())
        {
            MyLogger.appendLog(Level.SEVERE, "Couldn't locate DMap files at " + path);
            System.exit(-1);
        }

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser        parser  = factory.newSAXParser();
        DefaultHandler   handler = new DefaultHandler()
        {
            boolean mapId   = false;    // Whether the mapid element was found
            boolean mapPath = false;    // Whether the mapPath element was found
            String  id;                 // Id of the map
            String  path;               // Path tht map
            @Override
            public void characters(char[] ch, int start, int length) throws SAXException
            {
                if (mapId)
                {
                    id    = new String(ch, start, length);
                    mapId = false;
                }

                if (mapPath)
                {
                    path    = new String(ch, start, length);
                    mapPath = false;
                }
            }
            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException
            {
                if (qName.equalsIgnoreCase("path"))
                {
                    // Load the dmap
                    loadDMap(id, "res/" + path);
                }
            }
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes)
                    throws SAXException
            {
                if (qName.equalsIgnoreCase("mapid"))
                    mapId = true;
                if (qName.equals("path"))
                    mapPath = true;
            }
        };

        parser.parse(mapConfig, handler);

        long after = System.currentTimeMillis() - before;

        MyLogger.appendLog(Level.INFO, "Loaded DMaps in " + after + " ms");
    }

    private static void loadDMap(String id, String path)
    {
        File map = new File(path);
        int  mapId;

        // If map doesn't exist return
        if (!map.exists())
        {
            MyLogger.appendLog(Level.WARNING, "Couldn't load dmap " + path);

            return;
        }

        try
        {
            mapId = Integer.parseInt(id);
        }
        catch (NumberFormatException ex)
        {
            MyLogger.appendException(ex.getStackTrace(), ex.getMessage());

            return;
        }

        try
        {
            // Read the map data into a byte array
            DataInputStream input   = new DataInputStream(new FileInputStream(map));
            byte[]          mapData = new byte[input.available()];

            input.readFully(mapData);

            // Copy into a channel buffer so we can read primitive data types
            ChannelBuffer buffer = ChannelBuffers.copiedBuffer(ByteOrder.LITTLE_ENDIAN, mapData);

            // Advance to byte 268 since thats where the data we want is
            buffer.readerIndex(268);

            // Read the max x and y coords from the dmap
            int      maxX = buffer.readInt();
            int      maxY = buffer.readInt();
            byte[][] dmap = new byte[maxX][maxY];

            // Go through the file and read in dmap information
            // about whether the tile is valid to step on, a portal, etc.
            for (int i = 0; i < maxY; i++)
            {
                for (int j = 0; j < maxX; j++)
                {
                    // Only create a new tile if it's valid
                    // to save on memory usage
                    if (buffer.readShort() == 0)
                    {
                        // Set flag to valid
                        dmap[j][i] |= VALID;
                    }

                    // Junk data
                    buffer.readShort();
                    buffer.readByte();

                    byte height = buffer.readByte();

                    // If the height is negative then set the sign bit
                    if (height < 0)
                        dmap[j][i] |= HEIGHT_SIGN;

                    // Set the height to bits 3 to 7
                    dmap[j][i] |= (height << 2);
                }

                // Junk data
                buffer.readInt();
            }

            int portalCount = buffer.readInt();

            for (int i = 0; i < portalCount; i++)
            {
                int x = buffer.readInt();
                int y = buffer.readInt();

                if ((x < maxX) && (y < maxY))
                {
                    // Set the portal flag
                    dmap[x][y] |= PORTAL;
                    dmap[x][y] &= ~VALID;
                }

                buffer.readInt();
            }

            MAPS.put(mapId, dmap);
        }
        catch (IOException ex)
        {
            MyLogger.appendException(ex.getStackTrace(), ex.getMessage());
        }
    }

    /**
     * Gets whether the tile is valid to be stepped on
     * @return returns true if valid
     */
    public static boolean valid(int map, int x, int y)
    {
        try
        {
            byte tile = MAPS.get(map)[x][y];

            return (tile & VALID) == VALID;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Gets whether the specified tile is in a port range
     * @return returns true if it is a portal
     */
    public static boolean isPortal(int map, int x, int y)
    {
        try
        {
            byte tile = MAPS.get(map)[x][y];

            return (tile & PORTAL) == PORTAL;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Returns a reference to a d map object.
     * Do not change this array since it will affect the original
     * @param map the id of the map to get
     * @return returns the map
     */
    public static byte[][] getDmap(int map)
    {
        return MAPS.get(map);
    }

    public static Set<Integer> getKeys()
    {
        return MAPS.keySet();
    }

    public static byte getHeight(int map, int x, int y)
    {
        try
        {
            byte tile = MAPS.get(map)[x][y];

            return (byte) ((tile & HEIGHT_SIGN) | ((tile & HEIGHT) >> 2));
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return -100;
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
