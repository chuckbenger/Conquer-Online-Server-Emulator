using System;
using System.Collections.Generic;
using System.Text;
using System.Net.Sockets;
using System.Net;
using System.Threading;

namespace BlackBeltCO.COSocket
{
    public delegate void acceptCallback(Socket client); //Called when a client has been accepted

    public class COServerSocket
    {
        private Socket _socListener;
        private ManualResetEvent _suspend; //Used to suspend the current since connections are accepted asynchronous  
        private acceptCallback _callback;

        /// <summary>
        /// Constructor 
        /// </summary>
        public COServerSocket(acceptCallback callback)
        {
            this._suspend = new ManualResetEvent(true);
            this._callback = callback;
        }

        /// <summary>
        /// Binds the socket to a port and begins listening for connections
        /// </summary>
        /// <param name="port">The port to listen on</param>
        /// <param name="backlog">Number of allowed queued connection</param>
        public void listen(int port, int backlog = 10)
        {

            Thread listenThread = new Thread(new ThreadStart(delegate(){

                IPEndPoint localIP = new IPEndPoint(IPAddress.Any, port);

                _socListener = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

                try
                {
                    _socListener.Bind(localIP);
                    _socListener.Listen(backlog);
            
                    while(true)
                    {
                        _suspend.Reset();

                        //Begin accepting a client
                        _socListener.BeginAccept(new AsyncCallback(async_accept), _socListener);

                        //Stops current thred until signal is received
                        _suspend.WaitOne();
                    }
                }
                catch (Exception e)
                {
                    //Defer handlng to caller
                    throw e;
                }
            }));

            listenThread.Start();
        }

        /// <summary>
        /// Asyncronously accepts a client and passed the connected socket to
        /// the method defined in the delegate
        /// </summary>
        /// <param name="result">Accept result</param>
        private void async_accept(IAsyncResult result)
        {
            _suspend.Set();
            Socket listener = result.AsyncState as Socket;
            Socket client = listener.EndAccept(result);

            if (client.Connected)
                _callback(client);
        }
    }
}
