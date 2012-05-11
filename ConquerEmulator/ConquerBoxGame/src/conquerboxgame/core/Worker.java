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
import conquerboxgame.net.ServerDataEvent;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.jboss.netty.channel.Channel;

/**
 * Worker provides a class that you can append data to a queue that the worker
 * will handle in a different thread
 * @author chuck
 */
public class Worker implements Runnable
{
    
    private IHandler               handler; //Used by a worker thread to call the socket thread to queue data to send
    private boolean                handle;       //Whether to continue handling packets
    private boolean                stopAdd;      //Whether to stop allowing packets to be added
    private boolean                gracefullShutdown; //Whether the gracefull shutdown method was triggered
    private final HashMap<Channel, Queue<byte[]>> packetQueue;  //Queued data to be processed
    

    
    private final byte START_HASH_SIZE = 50; //Initial size of the hashmap
    /**
     * Creates a new worker object. 
     */
    public Worker()
    {
        packetQueue = new HashMap<>(START_HASH_SIZE);
        handle = true;
        stopAdd = false;
    }
    
    /**
     * Adds the input packet to the packet queue for the channel to be 
     * processed by the worker thread
     * @param channel the channel to add the packet to
     * @param packet the packet
     */
    public void add(Channel channel, byte[] packet)
    {
        if(stopAdd)
            return;
        
        //Copy the packet to a new local one since we
        //don't want a reference to it
        byte [] newPacket = new byte[packet.length];
        System.arraycopy(packet, 0, newPacket, 0, newPacket.length);
        
        //Hashmap is not threadsafe
        synchronized(packetQueue)
        {
            //Attempt to get a queu using the input channel
            Queue<byte[]> queue = packetQueue.get(channel);
            
            //If it's null create a new queue
            if(queue == null)
            {
                queue = new ConcurrentLinkedDeque<>();
                packetQueue.put(channel, queue);
            }
            
            //Append the packet to the queue
            queue.add(newPacket);
            
            //Let thread now that there is a packet
            packetQueue.notify();
        }
    }
    
    /**
     * process the packet
     */
    @Override
    public void run() 
    {
        boolean allEmpty = false; //Whether all channel queues are empty
        
        while(handle)
        {
            for(Channel channel : packetQueue.keySet())
            {
                Queue<byte[]> queue = packetQueue.get(channel);
                
                //If empty then skip
                if(queue.isEmpty())
                {
                    allEmpty = true;
                    continue;
                }
                
                allEmpty = false;
                
                //Grab the next packet to process
                byte[] process = queue.remove();
                
                 //Handle the packet
                if(process != null)
                    handler.handle(new ServerDataEvent(process, channel));

            }
            
            //If all queues empty and gracefull shutdown has been set
            //break from the thread
            if(allEmpty && gracefullShutdown)
               break;
            
            //If all queues are empty pause the thread
            if(allEmpty)
            {
                try 
                {
                    synchronized(packetQueue)
                    {
                        packetQueue.wait();
                    }
                } 
                catch (InterruptedException ex) {
                    MyLogger.appendException(ex.getStackTrace(), ex.toString());
                }
            }
        }
    }
    
    /**
     * Indicate to thread that it should shutdown. But continue 
     * processing the rest of the data
     */
    public void gracefullShutdown()
    {
        stopAdd = true;
        gracefullShutdown = true;
    }
    
    /**
     * Kill the thread without handling the remaining packet.
     */
    public void kill()
    {
        handle = false;
    }
    
    /**
     * The class that the worker thread will call to handle a packet
     * @param handle the class that implements IHandler
     */
    public void registerHandler(IHandler handle)
    {
        handler = handle;
    }
}
