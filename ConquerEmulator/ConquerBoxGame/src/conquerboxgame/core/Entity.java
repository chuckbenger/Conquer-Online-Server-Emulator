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
    protected long id;
    protected int  x;
    protected int  y;

    // <editor-fold defaultstate="collapsed" desc="Getters">
    public byte getDirection()
    {
        return direction;
    }

    
    public long getId()
    {
        return id;
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
    

    public void setX(int x)
    {
        this.x     = x;
    }

    public void setY(int y)
    {
        this.y     = y;
    }
    
    public void setId(long id)
    {
        this.id = id;
    }

    
    public void setDirection(byte direction)
    {
        this.direction = direction;
    }

    // </editor-fold>

    @Override
    public String toString() {
        return "Entity{" + "direction=" + direction + ", id=" + id + ", x=" + x + ", y=" + y + '}';
    }
    
    
  
}


//~ Formatted by Jindent --- http://www.jindent.com
