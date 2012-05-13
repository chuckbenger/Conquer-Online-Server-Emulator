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
package conquerboxgame;

import conquerboxgame.core.Kernel;
import conquerboxgame.net.GameHandler;
import conquerboxgame.net.GameServerDecoder;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 *
 * @author chuck
 */
public class ConquerBoxGame {

    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup("Auth-Server");
    private static ChannelFactory factory;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
     
        factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
        ServerBootstrap bootstrap = new ServerBootstrap(factory);

        bootstrap.setPipelineFactory(new ChannelPipelineFactory()
        {
            @Override
            public ChannelPipeline getPipeline() throws Exception
            {
                return Channels.pipeline(new GameServerDecoder(), new GameHandler());
            }
        });

        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);

        Channel server = bootstrap.bind(new InetSocketAddress(Kernel.PORT));

        CHANNEL_GROUP.add(server);

        MyLogger.appendLog(Level.INFO, "Game was bound to port " + Kernel.PORT);
        
    }
}
