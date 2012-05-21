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
package conquerboxgame.structures;

import conquerboxgame.MyLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Locations holds enums for teleport locations, portal locations, map id's
 * @author chuck
 */
public class Locations 
{

    
    public static enum TeleportLocation
    {
        TWIN_CITY_TO_APE(Map.TWIN_CITY, 555, 958),
        TWIN_CITY_TO_BIRD(Map.TWIN_CITY, 229, 197),
        TWIN_CITY_TO_NIX(Map.TWIN_CITY, 956, 555),
        TWIN_CITY_TO_DESERT(Map.TWIN_CITY, 67, 463),
        BIRD_FROM_TWIN_CITY(Map.BIRD_ISLAND,1014,713),
        NIX_FROM_TWIN_CITY(Map.PHOENIX_CASTLE,9,376),
        APE_FROM_TWIN_CITY(Map.APE_MOUNTAIN, 377,11),
        DESERT_FROM_TWIN_CITY(Map.DESERT_CITY, 974,669),
        NIX_SPAWN(Map.PHOENIX_CASTLE,190,271),
        BIRD_SPAWN(Map.BIRD_ISLAND,716,575),
        APE_SPAWN(Map.APE_MOUNTAIN,566,566),
        DESERT_SPAWN(Map.DESERT_CITY, 496, 641),
        TWIN_CITY_SPAWN(Map.TWIN_CITY,427,378),
        MARKET(Map.MARKET, 292, 236);
       
        public final Map MAP; //The map to teleport to
        public final int X; //the x position to teleport to
        public final int Y; //The y position to teleport to

        /**
         * Creates a new teleport location based on input parameters
         * @param MAP The map to teleport to 
         * @param X the x position to teleport to
         * @param Y The y position to teleport to
         */
        private TeleportLocation(Map MAP, int X, int Y)
        {
            this.MAP = MAP;
            this.X = X;
            this.Y = Y;
        }

    }
    
    /**
     * Holds game portal locations
     */
    public static enum PortalLocation
    {
        TWIN_CITY_TO_BIRD(TeleportLocation.BIRD_FROM_TWIN_CITY, 220,195,Map.TWIN_CITY),
        BIRD_TO_TWIN_CITY(TeleportLocation.TWIN_CITY_TO_BIRD, 1014,713,Map.BIRD_ISLAND),
        TWIN_CITY_TO_NIX(TeleportLocation.NIX_FROM_TWIN_CITY, 961,559,Map.TWIN_CITY),
        NIX_TO_TWIN_CITY(TeleportLocation.NIX_FROM_TWIN_CITY, 9, 376,Map.PHOENIX_CASTLE),
        TWIN_CITY_TO_APE(TeleportLocation.APE_FROM_TWIN_CITY,557,962,Map.TWIN_CITY),
        APE_TO_TWIN_CITY(TeleportLocation.TWIN_CITY_TO_APE, 377, 11, Map.APE_MOUNTAIN),
        DESERT_TO_TWIN_CITY(TeleportLocation.TWIN_CITY_TO_DESERT, 974, 669, Map.DESERT_CITY);
        
        public final TeleportLocation location; //The location the portal teleports to
        public final int portalX; //The portals general x position
        public final int portalY; //The portals general y position
        public final Map portalMap; //The map the port is on

        /**
         * Creates a new portal location in the input location
         * @param location the location the portal teleports to
         * @param portalX the general x position of the portal
         * @param portalY the general y position of the portal
         * @param portalMap the portal map
         */
        private PortalLocation(TeleportLocation location, int portalX, int portalY, Map portalMap) 
        {
            this.location = location;
            this.portalX = portalX;
            this.portalY = portalY;
            this.portalMap = portalMap;
        }
    }

    /**
     * Holds enumerated aliases for conquer map id's
     */
    public static enum Map
    {
        
        TWIN_CITY(1002),
        DESERT_CITY(1000),
        BIRD_ISLAND(1015),
        PHOENIX_CASTLE(1011),
        APE_MOUNTAIN(1020),
        MARKET(1036);
        
        //The id of the map
        public final int MAP_ID;

        /**
         * Sets the map id for the enum entry
         * @param MAP the maps id
         */
        private Map(int MAP)
        {
            this.MAP_ID = MAP;
        }
    }
   
    /**
     * Loads all the game portals in the hash map 
     * @param portals the hash map of portal locations
     */
    public static void loadPortals(HashMap<Integer,ArrayList<PortalLocation>> portals)
    {
        for(PortalLocation portal : PortalLocation.values())
        {
            ArrayList<PortalLocation> portalList = portals.get(portal.portalMap.MAP_ID);
            
            if(portalList == null)
            {
                portalList = new ArrayList<>();
                portals.put(portal.portalMap.MAP_ID, portalList);
            }
            
            portalList.add(portal);
        }
        
        MyLogger.appendLog(Level.INFO, "Loaded portal locations!");
    }
}
