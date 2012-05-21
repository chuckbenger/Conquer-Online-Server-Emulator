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
 *
 * @author chuck
 */
public class Locations 
{

    
    public static enum TeleportLocation
    {
        TWIN_CITY_TO_APE(1002, 555, 958),
        TWIN_CITY_TO_BIRD(1002, 229, 197),
        TWIN_CITY_TO_NIX(1002, 956, 555),
        TWIN_CITY_TO_DESERT(1002, 67, 463),
        BIRD_FROM_TWIN_CITY(1015,1014,713),
        NIX_FROM_TWIN_CITY(1011,9,376),
        APE_FROM_TWIN_CITY(1020, 377,11),
        DESERT_FROM_TWIN_CITY(1000, 974,669),
        NIX_SPAWN(1011,190,271),
        BIRD_SPAWN(1015,716,575),
        APE_SPAWN(1020,566,566),
        DESERT_SPAWN(1000, 496, 641),
        TWIN_CITY_SPAWN(1002,427,378),
        MARKET(1036, 292, 236);
       
        public final int MAP;
        public final int X;
        public final int Y;

        private TeleportLocation(int MAP, int X, int Y)
        {
            this.MAP = MAP;
            this.X = X;
            this.Y = Y;
        }

    }
    
    public static enum PortalLocation
    {
        TWIN_CITY_BIRD(TeleportLocation.BIRD_FROM_TWIN_CITY, 220,195,1002),
        BIRD_TWIN_CITY(TeleportLocation.TWIN_CITY_TO_BIRD, 1014,713,1015),
        TWIN_CITY_NIX(TeleportLocation.NIX_FROM_TWIN_CITY, 961,559,1002),
        NIX_TWIN_CITY(TeleportLocation.TWIN_CITY_TO_NIX, 9, 376,1011),
        TWIN_CITY_APE(TeleportLocation.APE_FROM_TWIN_CITY,557,962,1002),
        APE_TWIN_CITY(TeleportLocation.TWIN_CITY_TO_APE, 377, 11, 1020),
        DESERT_TWIN_CITY(TeleportLocation.TWIN_CITY_TO_DESERT, 974, 669, 1000);
        
        public final TeleportLocation location;
        public final int portalX;
        public final int portalY;
        public final int portalMap;

        private PortalLocation(TeleportLocation location, int portalX, int portalY, int portalMap) 
        {
            this.location = location;
            this.portalX = portalX;
            this.portalY = portalY;
            this.portalMap = portalMap;
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
            ArrayList<PortalLocation> portalList = portals.get(portal.portalMap);
            
            if(portalList == null)
            {
                portalList = new ArrayList<>();
                portals.put(portal.portalMap, portalList);
            }
            
            portalList.add(portal);
        }
        
        MyLogger.appendLog(Level.INFO, "Loaded portal locations!");
    }
}
