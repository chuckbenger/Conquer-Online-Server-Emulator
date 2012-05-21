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
package conquerboxgame.core;

import conquerboxgame.structures.Locations.PortalLocation;
import conquerboxgame.structures.Locations.TeleportLocation;
import conquerboxgame.structures.Rules;
import java.util.ArrayList;

/**
 *
 * @author chuck
 */
public class CoMath 
{
      
    /**
     * Gets the distance between two points
     * @param one the first entity to check
     * @param two the second entity to check
     * @return returns the distance
     */
    public static double getDistance(Entity one, Entity two)
    {
        return Math.abs(Math.sqrt(Math.pow(one.getX() - two.getX(), 2) + Math.pow(one.getY() - two.getY(), 2)));
    }
    
    /**
     * Gets the portal the client stepped on or returns null if one isn't found
     * @param possibleLocations the possible portals that client may have jumped on
     * @return returns the portal location or null
     */
    public static TeleportLocation getPortal(Client client, ArrayList<PortalLocation> possibleLocations)
    {
        for(PortalLocation possibleLocation : possibleLocations)
        {
            if((client.getX() >= possibleLocation.portalX || client.getX() <= possibleLocation.portalX + Rules.PORTAL_SIZE)
                && possibleLocation.portalY >= client.getY() || client.getY() <= possibleLocation.portalY + Rules.PORTAL_SIZE)
                return possibleLocation.location;
        }
        
        return null;
    }
}
