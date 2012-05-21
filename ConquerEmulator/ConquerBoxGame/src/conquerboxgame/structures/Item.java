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
import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;

/**
 *
 * @author chuck
 */
public class Item 
{
    private int  ID;
    private String Name;
    private int  reqClass;
    private int  Prof;
    private int  level;
    private int  Sex;
    private int  Str_Require;
    private int  Dex_Require;
    private int  Vit_Require;
    private int  Spi_Require;
    private int  Tradeable;
    private int  Cost;
    private long MaxDamage;
    private long MinDamage;
    private int  DefenseAdd;
    private int  DexAdd;
    private short DodgeAdd;
    private int  HPAdd;
    private int  MPAdd;
    private int  Dura;
    private int  MaxDura;
    private long MagicAttack;
    private long MDefenseAdd;
    private int  Range;
    private int  Frequency;

    /**
     * Loads all game items into the input hash map
     * @param items the hash map to load into
     * @param path the path to the items file
     * @return returns true if successful; else false
     */
    public static boolean load(HashMap<Integer, Item> items, String path)
    {
        File file = new File(path);
        
        if(!file.exists())
            return false;
        
        try 
        {
            long before    = System.currentTimeMillis();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            
            while((line = reader.readLine()) != null)
            {
                String parts[] = line.trim().split(" ");
                Item item = new Item();
                int counter = 0;
                
                item.ID = Integer.parseInt(parts[counter++].trim());
                item.Name = parts[counter++].trim();
                item.reqClass = Integer.parseInt(parts[counter++].trim());
                item.Prof = Integer.parseInt(parts[counter++].trim());
                item.level= Integer.parseInt(parts[counter++].trim());
                item.Sex = Integer.parseInt(parts[counter++].trim());
                item.Str_Require = Integer.parseInt(parts[counter++].trim());
                item.Dex_Require = Integer.parseInt(parts[counter++].trim());
                item.Vit_Require = Integer.parseInt(parts[counter++].trim());
                item.Spi_Require = Integer.parseInt(parts[counter++].trim());
                item.Tradeable = Integer.parseInt(parts[counter++].trim());
                item.Cost = Integer.parseInt(parts[counter++].trim());
                item.MaxDamage = Long.parseLong(parts[counter++].trim());
                item.MinDamage = Long.parseLong(parts[counter++].trim());
                item.DefenseAdd = Integer.parseInt(parts[counter++].trim());
                item.DexAdd= Integer.parseInt(parts[counter++].trim());
                item.DodgeAdd= Short.parseShort(parts[counter++].trim());
                item.HPAdd = Integer.parseInt(parts[counter++].trim());
                item.MPAdd = Integer.parseInt(parts[counter++].trim());
                item.Dura = Integer.parseInt(parts[counter++].trim());
                item.MaxDura = Integer.parseInt(parts[counter++].trim());
                item.MagicAttack =Long.parseLong(parts[counter++].trim());
                item.MDefenseAdd =Long.parseLong(parts[counter++].trim());
                item.Range = Integer.parseInt(parts[counter++].trim());
                item.Frequency = Integer.parseInt(parts[counter++].trim());
                
                items.put(item.ID, item);
            }
            
            long after = System.currentTimeMillis() - before;

            MyLogger.appendLog(Level.INFO, "Load " + items.size() + " items in " + after + " ms");
            
        } catch (IOException ex) {
             MyLogger.appendException(ex.getStackTrace(), ex.getMessage());
             return false;
        }
       
        
        return true;
    }
    
// <editor-fold defaultstate="collapsed" desc="Getters">
    public int getReqClass() {
        return reqClass;
    }

    public int getCost() {
        return Cost;
    }

    public int getDefenseAdd() {
        return DefenseAdd;
    }

    public int getDexAdd() {
        return DexAdd;
    }

    public int getDex_Require() {
        return Dex_Require;
    }

    public short getDodgeAdd() {
        return DodgeAdd;
    }

    public int getDura() {
        return Dura;
    }

    public int getFrequency() {
        return Frequency;
    }

    public int getHPAdd() {
        return HPAdd;
    }

    public int getID() {
        return ID;
    }

    public long getMDefenseAdd() {
        return MDefenseAdd;
    }

    public int getMPAdd() {
        return MPAdd;
    }

    public long getMagicAttack() {
        return MagicAttack;
    }

    public long getMaxDamage() {
        return MaxDamage;
    }

    public int getMaxDura() {
        return MaxDura;
    }

    public long getMinDamage() {
        return MinDamage;
    }

    public String getName() {
        return Name;
    }

    public int getProf() {
        return Prof;
    }

    public int getRange() {
        return Range;
    }

    public int getSex() {
        return Sex;
    }

    public int getSpi_Require() {
        return Spi_Require;
    }

    public int getStr_Require() {
        return Str_Require;
    }

    public int getTradeable() {
        return Tradeable;
    }

    public int getVit_Require() {
        return Vit_Require;
    }

    public int getLevel() {
        return level;
    }
    // </editor-fold>
    
}
