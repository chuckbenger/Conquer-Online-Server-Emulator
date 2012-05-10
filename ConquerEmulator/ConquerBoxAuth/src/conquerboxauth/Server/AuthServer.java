package conquerboxauth.Server;



import conquerboxauth.MyLogger;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;


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
public class AuthServer
{

    //Group that contains all channels connected to auth server
    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup("Auth-Server");
    private ChannelFactory factory;
    private HashMap<Channel, AuthClient> clientHashMap;

    /**
     * Sets up the boss and worker threads and binds the server channel to specified port
     *
     * @param port the port number to bind to
     */
    public AuthServer(int port)
    {

        clientHashMap = new HashMap<>();

        factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
        ServerBootstrap bootstrap = new ServerBootstrap(factory);

        bootstrap.setPipelineFactory(new ChannelPipelineFactory()
        {
            @Override
            public ChannelPipeline getPipeline() throws Exception
            {
                return Channels.pipeline(new AuthDecoder(clientHashMap), new AuthHandler(clientHashMap));
            }
        });

        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);

        Channel server = bootstrap.bind(new InetSocketAddress(port));

        CHANNEL_GROUP.add(server);

        MyLogger.appendLog(Level.INFO, "Auth was bound to port " + port);
    }

    /**
     * Shuts down the auth server and frees resources
     */
    public void shutDown()
    {
        ChannelGroupFuture future = CHANNEL_GROUP.close();
        future.awaitUninterruptibly();
        factory.releaseExternalResources();
    }
}

















