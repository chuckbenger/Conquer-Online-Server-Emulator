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
package conquerboxgame;

/**
 * Entity provides a base class that represents attributes that all
 * game entities have
 * @author chuck
 */
public class Entity 
{
    protected long   id;
    protected int    health; 
    protected int    x;
    protected int    y;
    protected int    prevX;
    protected int    prevY;

   // <editor-fold defaultstate="collapsed" desc="Getters">
    public int getHealth() {
        return health;
    }

    public long getId() {
        return id;
    }

    public int getPrevX() {
        return prevX;
    }

    public int getPrevY() {
        return prevY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Setters">
    public void setHealth(int health) {
        this.health = health;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setX(int x) {
        this.prevX = this.x;
        this.x = x;
    }

    public void setY(int y) {
        this.prevY = this.y;
        this.y = y;
    }
    // </editor-fold>
    
}
