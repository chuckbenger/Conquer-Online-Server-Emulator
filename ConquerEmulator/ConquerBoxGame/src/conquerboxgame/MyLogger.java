package conquerboxgame;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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

/**
 * Provides a basic class to log data
 */
public class MyLogger
{

    private static final Logger LOGGER = Logger.getLogger("LOG");
    private static FileHandler fileHandler;

    /**
     * Sets up the log to output to a file for the current date
     */
    public static void initLog()
    {
        try
        {
            File logDir = new File("logs/");

            if (!logDir.exists())
                logDir.mkdir();

            Date now = new Date();

            String path = logDir.getAbsolutePath() + "\\Game_" + new SimpleDateFormat("yyyy_MMM_dd").format(now) + ".log";

            fileHandler = new FileHandler(path, true);
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            LOGGER.log(Level.INFO, "Log for {0}", now.toString());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Logs the input text to the log at the specified level
     *
     * @param level the level of the log
     * @param text  the text to output
     */
    public static void appendLog(Level level, String text)
    {
        LOGGER.log(level, text);
    }
    
    public static void appendException(StackTraceElement[] e, String title)
    {
        String total = title +"\n";
        for(StackTraceElement s : e)
            total += s.toString() + "\n";
        
        appendLog(Level.SEVERE, total);
    }
}
