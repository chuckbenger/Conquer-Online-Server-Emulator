package conquerboxauth.Server;



import conquerboxauth.MyLogger;
import java.sql.*;
import java.util.logging.Level;

/**
 * **********************************************************************
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
 * ***************************************************************************
 */
public class AuthDB
{
    private static final String USERNAME = "root";
    private static final String PASSWORD = "july301992";
    private static final String URL = "jdbc:mysql://192.168.1.100:3306/ConquerBox";

    private ResultSet set; //Results from a query
    private Statement stmt;

    /**
     * Empty constructor
     */
    public AuthDB()
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

        } catch (SQLException e)
        {
            MyLogger.appendLog(Level.SEVERE, e.toString());
            return false;
        }

        return true;
    }

    /**
     * Checks to see if the input username and password exist in the database
     *
     * @param username the username to check
     * @param password the password to check
     * @return returns true if exists, else false
     */
    public boolean isUserValid(String username, String password)
    {

        //16 characters is the max pass limit
        if (username.length() > 16 || password.length() > 16)
            return false;

        try
        {
            set = stmt.executeQuery("SELECT 1 FROM authaccounts WHERE username = '" + username + "' AND password = '" + password + "'");
            return set.next();

        } catch (SQLException e)
        {
            MyLogger.appendLog(Level.SEVERE, e.getMessage());
            return false;
        }
    }

    /**
     * Gets the id for an account
     *
     * @param username the username to check
     * @return returns the acct id
     */
    public int getAcctId(String username)
    {

        //16 characters is the max pass limit
        if (username.length() > 16)
            return -1;

        try
        {
            set = stmt.executeQuery("SELECT idAuthAccounts FROM authaccounts WHERE username = '" + username + "'");

            if (set.next())
                return set.getInt(1);

            return -1;

        } catch (SQLException e)
        {
            MyLogger.appendLog(Level.SEVERE, e.getMessage());
            return -1;
        }
    }
}
