using System;
using System.Collections.Generic;
using System.Text;
using System.Net.Sockets;

namespace BlackBeltCO.COSocket
{
    //Delegate used to call user defined method when a packet is received
    public delegate void recieveCallback(COClientSocket client);

    /// <summary>
    /// COClientSocket provides a class for handling a clients socket connection.
    /// </summary>
    public class COClientSocket : IWorkable
    {
        private Socket _client; //Clients connected socket
        private recieveCallback _callback; //Called when data is received
        private int _bufferSize; 
        private byte[] _buffer;
        private IHandler _handler; //Class that implements IHandler
        private int bytesRead;
        private bool _started; //Whether the receive function has been called already
        private bool _disconnected; //If true the client disconnected

        public int BytesRead { get { return bytesRead; } }
        public byte[] Buffer { get { return _buffer; } }
        public bool Disconnected { get { return _disconnected; } }

        //Implemented from IWorkable
        public IHandler Handler { get { return _handler; } }

        

        /// <summary>
        /// Creates a new object for managing the clients connection
        /// </summary>
        /// <param name="client">The connected socket</param>
        /// <param name="callback">The method to call when data is read</param>
        public COClientSocket(Socket client, recieveCallback callback, IHandler handler, int bufferSize = 1024)
        {
            this._bufferSize = bufferSize;
            this._buffer = new byte[_bufferSize];
            this._client = client;
            this._callback = callback;
            this._handler = handler;
            this._disconnected = false;
        }

        /// <summary>
        /// Starts the begin receive function. only call this method once per
        /// client
        /// </summary>
        public void beginReceive()
        {
            if (!_started)
            {
                try
                {
                    if (_client.Connected)
                    {
                        _client.BeginReceive(_buffer, 0, _bufferSize, SocketFlags.None, new AsyncCallback(Receive), _client);
                        _started = true;
                    }
                }
                catch (Exception e)
                {
                    //Indicate client disconnected
                    _disconnected = true;
                    _callback(this);
                }
            }
        }

        /// <summary>
        /// Called when there is data to be read. If there was data it's
        /// sent to the _callback method.
        /// </summary>
        /// <param name="result">Receive result</param>
        private void Receive(IAsyncResult result)
        {
            try
            {
                Socket client = result.AsyncState as Socket;

                bytesRead = client.EndReceive(result);
                if (bytesRead > 0)
                {
                    //Calls the user defined callback method
                    _callback(this);

                    //Begin receiving again
                    _client.BeginReceive(_buffer, 0, _bufferSize, SocketFlags.None, new AsyncCallback(Receive), _client);
                }
            }
            catch (Exception e)
            {
                //Indicate client disconnected
                _disconnected = true;
                _callback(this);
            }
        }

        /// <summary>
        /// Sends the input data to the client
        /// </summary>
        /// <param name="buffer">The byte buffer to send</param>
        public void send(byte[] buffer)
        {
            try
            {
                _client.Send(buffer);
            }
            catch (Exception e)
            {
                //Indicate client disconnected
                _disconnected = true;
                _callback(this);
            }
        }
    }
}
