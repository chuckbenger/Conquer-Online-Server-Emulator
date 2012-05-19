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

//~--- non-JDK imports --------------------------------------------------------

import conquerboxgame.crypto.Cryptographer;
import conquerboxgame.structures.NPC;
import java.util.ArrayList;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;

/**
 *
 * @author chuck
 */
public class Client extends Entity
{
    private int           characterId;
    private Channel       channel;
    private Cryptographer crypt;
    private int           dexterity;
    private long          experience;
    private long          gold;
    private int           hair;
    private short         level;
    private int           magic;
    private String        name;
    private int           pkPoints;
    private int           playerClass;
    private short         reborn;
    private int           sprit;
    private int           statPoints;
    private int           strength;
    private long          token;
    private int           vitality;
    private int           health;
    private long          model;
    private int           prevX;
    private int           prevY;
    private int           map;
    
    private ArrayList<NPC> myNpcs = new ArrayList<>(); //Npcs currently on the clients screen

    /**
     * Creates a new client object
     */
    public Client(Channel channel)
    {
        name         = "chuck";
        this.channel = channel;
        crypt        = new Cryptographer();
    }

    // <editor-fold defaultstate="collapsed" desc="Getters">

     public int getPrevX()
    {
        return prevX;
    }

    public int getPrevY()
    {
        return prevY;
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

    public int getCharacterId() {
        return characterId;
    }
    
    
    public long getToken()
    {
        return token;
    }

    public Cryptographer getCrypt()
    {
        return crypt;
    }

    public Channel getChannel()
    {
        return channel;
    }

    public int getHair()
    {
        return hair;
    }

    public int getDexterity()
    {
        return dexterity;
    }

    public long getExperience()
    {
        return experience;
    }

    public long getGold()
    {
        return gold;
    }

    public short getLevel()
    {
        return level;
    }

    public int getMagic()
    {
        return magic;
    }

    public String getName()
    {
        return name;
    }

    public int getPkPoints()
    {
        return pkPoints;
    }

    public int getPlayerClass()
    {
        return playerClass;
    }

    public short getReborn()
    {
        return reborn;
    }

    public int getSprit()
    {
        return sprit;
    }

    public int getStatPoints()
    {
        return statPoints;
    }

    public int getStrength()
    {
        return strength;
    }

    public int getVitality()
    {
        return vitality;
    }

    public ArrayList<NPC> getMyNpcs() {
        return myNpcs;
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
    
    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }
    
    @Override
    public void setX(int x)
    {
        this.prevX = this.x;
        this.x     = x;
    }

    @Override
    public void setY(int y)
    {
        this.prevY = this.y;
        this.y     = y;
    }

    
    public void setToken(long token)
    {
        this.token = token;
    }

    public void setHair(int hair)
    {
        this.hair = hair;
    }

    public void setDexterity(int dexterity)
    {
        this.dexterity = dexterity;
    }

    public void setExperience(long experience)
    {
        this.experience = experience;
    }

    public void setGold(long gold)
    {
        this.gold = gold;
    }

    public void setLevel(short level)
    {
        this.level = level;
    }

    public void setMagic(int magic)
    {
        this.magic = magic;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPkPoints(int pkPoints)
    {
        this.pkPoints = pkPoints;
    }

    public void setPlayerClass(int playerClass)
    {
        this.playerClass = playerClass;
    }

    public void setReborn(short reborn)
    {
        this.reborn = reborn;
    }

    public void setSprit(int sprit)
    {
        this.sprit = sprit;
    }

    public void setStatPoints(int statPoints)
    {
        this.statPoints = statPoints;
    }

    public void setStrength(int strength)
    {
        this.strength = strength;
    }

    public void setVitality(int vitality)
    {
        this.vitality = vitality;
    }

    
    // </editor-fold>

    
    /**
     * Adds a npc to the clients screen
     * @param npc the npc to add
     */
    public void addNPC(NPC npc)
    {
        myNpcs.add(npc);
    }
    
    /**
     * Encrypt the packet and send it to the client
     * @param packet the packet to encrypt
     */
    public void send(ChannelBuffer buffer)
    {
        crypt.Encrpyt(buffer.array());
        channel.write(buffer);
    }
    
    
    
    /*
     * Static methods
     */
    
    /**
     * Adjusts a client x and y coordinates based on their current direction
     * @param client the client to update
     */
    public static void adjustPostionBasedOnDirection(Client client)
    {
        
        switch (client.getDirection())
        {
        case 0 :
            client.y++;
            break;

        case 1 :
            client.y++;
            client.x--;
            break;

        case 2 :
            client.x--;
            break;

        case 3 :
            client.x--;
            client.y--;
            break;

        case 4 :
            client.y--;
            break;

        case 5 :
            client.y--;
            client.x++;
            break;

        case 6 :
            client.x++;
            break;

        case 7 :
            client.x++;
            client.y++;
            break;
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
