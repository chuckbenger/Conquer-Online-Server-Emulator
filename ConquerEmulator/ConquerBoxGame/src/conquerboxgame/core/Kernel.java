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

import conquerboxgame.MyLogger;
import conquerboxgame.database.Database;
import conquerboxgame.net.PacketHandler;
import conquerboxgame.net.ServerDataEvent;
import conquerboxgame.structures.NPC;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import org.jboss.netty.channel.Channel;

/**
 * Kernel is the Main class that contains most of the game server data objects
 * @author chuck
 */
public class Kernel 
{
    public static final int PORT = 8080;
    
    //Constant primitives
    private final static byte NUM_WORKERS     = 1;  //Number of dedicated worker threads
    private final static byte INITIAL_CLIENTS = 30; //Initial size of the hashmap
    
    //Constant objects
    public static final HashMap<Channel,Client> CLIENTS = new HashMap<>(INITIAL_CLIENTS); //Clients logged in
    public static final Worker[] WORKERS  = new Worker[NUM_WORKERS]; //Worker threads
    public static final Database DATABASE = new Database();
    
    //Holds all npc's for each map
    public static final HashMap<Integer,ArrayList<NPC>> NPC_MAP = new HashMap<>();
    
    
    //Non constant primitives
    private static byte currentWorker = 0; //Current thread to offload to
    
    public static void init()
    {

         //Create and start each worker thread
        for(int i = 0; i < WORKERS.length; i++)
        {
            WORKERS[i] = new Worker();
            WORKERS[i].registerHandler(new PacketHandler());
            new Thread(WORKERS[i]).start();
        }
        
        //DB connection failed so shutdown server
        if(!DATABASE.connect())
        {
            MyLogger.appendLog(Level.SEVERE, "Failed to connect to database. Shutting down");
            killAllWorkers();
            System.exit(-1);
        }
       
        if(!DATABASE.loadNPCData(NPC_MAP))
        {
            MyLogger.appendLog(Level.SEVERE, "Failed to load npc data. shutting down");
            killAllWorkers();
            System.exit(-1);
        }
    }
    
    
    /**
     * Kill all threads imediatly
     */
    public static void killAllWorkers()
    {
         for(Worker worker : WORKERS)
             worker.kill();
    }
    
    /**
     * Gracefully shutdown all threads by letting them finish the
     * data they have left.
     */
    public static void graceFullShutdownAllWorkers()
    {
         for(Worker worker : WORKERS)
             worker.gracefullShutdown();
    }
    
    /**
     * Offloads the data event to a worker thread to process.
     * @param event the event containing the client and it's data
     */
    public static void offloadToWorker(ServerDataEvent event)
    {
        //Offload to worker 
        WORKERS[currentWorker++].add(event.getClient(), event.getPacket());
        
        //If we've reached the last worker reset the counter
        if(currentWorker >= WORKERS.length)
            currentWorker = 0;
    }
}
