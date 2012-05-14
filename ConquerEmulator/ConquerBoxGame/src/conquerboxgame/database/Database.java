package conquerboxgame.database;

//~--- non-JDK imports --------------------------------------------------------

import conquerboxgame.MyLogger;

import conquerboxgame.core.Client;

//~--- JDK imports ------------------------------------------------------------

import java.sql.*;

import java.util.logging.Level;

/**
 *
 * Copyright 2012 Charles Benger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
public class Database
{
    private static final String PASSWORD = "july301992";
    private static final String URL      = "jdbc:mysql://192.168.1.100:3306/ConquerBox";
    private static final String USERNAME = "root";
    private Statement           stmt;

    /**
     * Empty constructor
     */
    public Database()
    {
    }

    /**
     * Creates a new connection to the database
     *
     * @return returns true if successful, otherwise false.
     */
    public boolean connect()
    {
        try
        {
            Connection db = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            stmt = db.createStatement();
        }
        catch (SQLException e)
        {
            MyLogger.appendLog(Level.SEVERE, e.toString());

            return false;
        }

        return true;
    }

    /**
     * Checks if the user has a game account
     *
     * @param acctId  the client account id
     * @return returns true if they have on, else false
     */
    public boolean hasGameAccount(long acctId)
    {
        try
        {
            ResultSet set = stmt.executeQuery("SELECT 1 FROM Game_Accounts WHERE account_id = '" + acctId + "'");

            return set.next();
        }
        catch (SQLException e)
        {
            MyLogger.appendLog(Level.SEVERE, e.getMessage());

            return false;
        }
    }

    /**
     * Creates a new character based on the client passed in
     * @param c the client to create the account for
     * @return returns true if created else false
     */
    public boolean createCharacter(Client c)
    {
        String insert = "INSERT INTO `conquerbox`.`game_accounts`"
                        + "(`account_id`, `model`, `class`, `string_count`, `name_length`, `name`)" + "VALUES("
                        + c.getId() + "," + c.getModel() + "," + c.getPlayerClass() + ",1," + c.getName().length()
                        + ",'" + c.getName() + "');";

        try
        {
            return stmt.executeUpdate(insert) == 1;
        }
        catch (SQLException ex)
        {
            MyLogger.appendException(ex.getStackTrace(), ex.getMessage());

            return false;
        }
    }

    /**
     * Loads the character data into a client object
     * @param c the client to load the data into
     * @return returns true if data was read, or false if error
     */
    public boolean loadCharacterData(Client c)
    {
        String select = "SELECT " + "`game_accounts`.`character_id`," + "`game_accounts`.`account_id`,"
                        + "`game_accounts`.`model`," + "`game_accounts`.`hair`," + "`game_accounts`.`gold`,"
                        + "`game_accounts`.`exp`," + "`game_accounts`.`strength`," + "`game_accounts`.`dex`,"
                        + "`game_accounts`.`vit`," + "`game_accounts`.`spirit`," + "`game_accounts`.`stat_points`,"
                        + "`game_accounts`.`hp`," + "`game_accounts`.`mp`," + "`game_accounts`.`pk_points`,"
                        + "`game_accounts`.`level`," + "`game_accounts`.`class`," + "`game_accounts`.`reborn`,"
                        + "`game_accounts`.`display_name`," + "`game_accounts`.`string_count`,"
                        + "`game_accounts`.`name_length`," + "`game_accounts`.`name`,"
                        + "`game_accounts`.`spouse_length`," + "`game_accounts`.`spouse_name`,"
                        + "`game_accounts`.`x`," + "`game_accounts`.`y`,"
                        + "`game_accounts`.`map`"
                        + "FROM `conquerbox`.`game_accounts`" + " WHERE account_id = '" + c.getId() + "'";

        try
        {
            ResultSet set = stmt.executeQuery(select);

            if (!set.next())
                return false;
            c.setCharacterId(1000000 + set.getInt("character_id"));
            c.setId(set.getInt("account_id"));
            c.setModel(set.getInt("model"));
            c.setHair(set.getInt("hair"));
            c.setGold(set.getInt("gold"));
            c.setExperience(set.getInt("exp"));
            c.setStrength(set.getInt("strength"));
            c.setDexterity(set.getInt("dex"));
            c.setVitality(set.getInt("vit"));
            c.setSprit(set.getInt("spirit"));
            c.setStatPoints(set.getInt("stat_points"));
            c.setHealth(set.getInt("hp"));
            c.setMagic(set.getInt("mp"));
            c.setPkPoints(set.getInt("pk_points"));
            c.setLevel(set.getShort("level"));
            c.setPlayerClass(set.getInt("class"));
            c.setReborn(set.getShort("reborn"));
            c.setName(set.getString("name"));
            c.setX(set.getInt("x"));
            c.setY(set.getInt("y"));
            c.setMap(set.getInt("map"));
            return true;
        }
        catch (SQLException ex)
        {
            MyLogger.appendException(ex.getStackTrace(), ex.getMessage());

            return false;
        }
    }
    
    
    /**
     * Persists a client object to the database
     * @param c the client to persist
     */
    public boolean persistClient(Client c)
    {
        String insert = "UPDATE `conquerbox`.`game_accounts` SET "
                        + "`game_accounts`.`model` = '" +  c.getModel() + "'," 
                        + "`game_accounts`.`hair` = '" +  c.getHair() + "'," 
                        + "`game_accounts`.`gold`= '" +  c.getGold() + "'," 
                        + "`game_accounts`.`exp`= '" +  c.getExperience() + "'," 
                        + "`game_accounts`.`strength`= '" +  c.getStrength() + "'," 
                        + "`game_accounts`.`dex`= '" +  c.getDexterity() + "'," 
                        + "`game_accounts`.`vit`= '" +  c.getVitality() + "',"  
                        + "`game_accounts`.`spirit`= '" +  c.getSprit() + "'," 
                        + "`game_accounts`.`stat_points`= '" +  c.getStatPoints() + "'," 
                        + "`game_accounts`.`hp`= '" +  c.getHealth() + "'," 
                        + "`game_accounts`.`mp`= '" +  c.getMagic() + "'," 
                        + "`game_accounts`.`pk_points`= '" +  c.getPkPoints() + "'," 
                        + "`game_accounts`.`level`= '" +  c.getLevel() + "'," 
                        + "`game_accounts`.`class`= '" +  c.getPlayerClass() + "'," 
                        + "`game_accounts`.`reborn`= '" +  c.getReborn() + "'," 
                        + "`game_accounts`.`x`= '" +  c.getX() + "'," 
                        + "`game_accounts`.`y`= '" +  c.getY() + "'," 
                        + "`game_accounts`.`map`= '" +  c.getMap() + "'" 
                        + " WHERE character_id = '" + (c.getCharacterId() - 1000000) + "'";
        
        try
        {
            return stmt.executeUpdate(insert) == 1;
        }
        catch (SQLException ex)
        {
            MyLogger.appendException(ex.getStackTrace(), ex.getMessage());

            return false;
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
