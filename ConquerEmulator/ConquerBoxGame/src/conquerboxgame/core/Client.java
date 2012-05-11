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

import conquerboxgame.crypto.Cryptographer;

/**
 *
 * @author chuck
 */
public class Client extends Entity
{
    private long   gold;
    private long   experience;
    private int    strength;
    private int    dexterity;
    private int    vitality;
    private int    sprit;
    private int    statPoints;
    private int    magic;
    private int    pkPoints;
    private int    hair;
    private short  level;
    private short  playerClass;
    private short  reborn;
    private String name;
    
    private Cryptographer crypt;
    
    /**
     * Creates a new client object
     */
    public Client()
    {
        crypt = new Cryptographer();
    }

    // <editor-fold defaultstate="collapsed" desc="Getters">

    public int getHair() {
        return hair;
    }
    
    public int getDexterity() {
        return dexterity;
    }

    public long getExperience() {
        return experience;
    }

    public long getGold() {
        return gold;
    }

    public short getLevel() {
        return level;
    }

    public int getMagic() {
        return magic;
    }

    public String getName() {
        return name;
    }

    public int getPkPoints() {
        return pkPoints;
    }

    public short getPlayerClass() {
        return playerClass;
    }

    public short getReborn() {
        return reborn;
    }

    public int getSprit() {
        return sprit;
    }

    public int getStatPoints() {
        return statPoints;
    }

    public int getStrength() {
        return strength;
    }

    public int getVitality() {
        return vitality;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Setters">

    public void setHair(int hair) {
        this.hair = hair;
    }
        
    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPkPoints(int pkPoints) {
        this.pkPoints = pkPoints;
    }

    public void setPlayerClass(short playerClass) {
        this.playerClass = playerClass;
    }

    public void setReborn(short reborn) {
        this.reborn = reborn;
    }

    public void setSprit(int sprit) {
        this.sprit = sprit;
    }

    public void setStatPoints(int statPoints) {
        this.statPoints = statPoints;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setVitality(int vitality) {
        this.vitality = vitality;
    }
    // </editor-fold>
      
}
