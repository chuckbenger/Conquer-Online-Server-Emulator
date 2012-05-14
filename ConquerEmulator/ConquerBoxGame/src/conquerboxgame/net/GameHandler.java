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
package conquerboxgame.net;

import conquerboxgame.MyLogger;
import conquerboxgame.core.Client;
import conquerboxgame.core.Kernel;
import java.util.logging.Level;
import org.jboss.netty.channel.*;

/**
 * GameHandler is used to manage a channels various states
 * @author chuck
 */
public class GameHandler extends SimpleChannelHandler
{
    /**
     * Once a non-null value has been returned from the decoder offload the packet
     * to a worker thread for processing
     */
     @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception
    {
        //Grab the packet
        ServerDataEvent event = (ServerDataEvent)e.getMessage();
  
        //Make sure it's not null before attempting to handle it
        if(event == null)
            return;
        
        //Offloads this packet to a worker thread
        Kernel.offloadToWorker(event);
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
    {
        Client client = new Client(e.getChannel());

        synchronized (Kernel.CLIENTS)
        {
            Kernel.CLIENTS.put(client.getChannel(), client);
        }
        
        MyLogger.appendLog(Level.INFO, "Client has connected! => " + client.getChannel().getRemoteAddress().toString());
    }


    
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
    {
        synchronized (Kernel.CLIENTS)
        {
            Kernel.DATABASE.persistClient(Kernel.CLIENTS.remove(e.getChannel()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception
    {
        MyLogger.appendException(e.getCause().getStackTrace(),e.toString());
    }
}
