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

/**
 * Entity provides a base class that represents attributes that all
 * game entities have
 * @author chuck
 */
public class Entity
{
    protected byte direction;
    protected int  health;
    protected long id;
    protected int  map;
    protected long model;
    protected int  prevX;
    protected int  prevY;
    protected int  x;
    protected int  y;

    // <editor-fold defaultstate="collapsed" desc="Getters">
    public byte getDirection()
    {
        return direction;
    }

    public int getMap()
    {
        return map;
    }

    public long getModel()
    {
        return model;
    }

    public int getHealth()
    {
        return health;
    }

    public long getId()
    {
        return id;
    }

    public int getPrevX()
    {
        return prevX;
    }

    public int getPrevY()
    {
        return prevY;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Setters">
    public void setMap(int map)
    {
        this.map = map;
    }

    public void setModel(long model)
    {
        this.model = model;
    }

    public void setHealth(int health)
    {
        this.health = health;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setX(int x)
    {
        this.prevX = this.x;
        this.x     = x;
    }

    public void setY(int y)
    {
        this.prevY = this.y;
        this.y     = y;
    }

    public void setDirection(byte direction)
    {
        this.direction = direction;
    }

    // </editor-fold>
    
    
    /**
     * Adjusts the clients location x,y coordinate based in it's direction
     */
    public void adjustPosByDirection()
    {
        switch (direction)
        {
        case 0 :
            y++;
            break;

        case 1 :
            y++;
            x--;
            break;

        case 2 :
            x--;
            break;

        case 3 :
            x--;
            y--;
            break;

        case 4 :
            y--;
            break;

        case 5 :
            y--;
            x++;
            break;

        case 6 :
            x++;
            break;

        case 7 :
            x++;
            y++;
            break;
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
