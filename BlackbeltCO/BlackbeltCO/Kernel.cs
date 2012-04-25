using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using BlackBeltCO.Core;
using BlackBeltCO.COSocket;
using System.Net.Sockets;

namespace BlackBeltCO.Login
{
    public class Kernel
    {

        private const string _NAME      = "BlackBeltCO "; //Name of the server for console output
        private const string _DB_CONFIG = "config/config.xml";  //Holds the database configuration
        private COServerSocket _serverSocket;
        private List<COClientSocket> _clients;
        private ConsumerPool _workers;
        private XMLReader _configReader;

        /// <summary>
        /// Outputs the specified text to the console
        /// </summary>
        /// <param name="text">The text to output</param>
        public static void print(string text)
        {
            Console.WriteLine(_NAME + DateTime.Now.TimeOfDay + ": " + text);
        }

        /// <summary>
        /// Creates a new kernel handler which start the entire server
        /// </summary>
        public Kernel()
        {
            _clients = new List<COClientSocket>();
            _workers = new ConsumerPool();
            _configReader = new XMLReader();

            if (!_configReader.read(_DB_CONFIG))
            {
                print("Couldn't read " + _DB_CONFIG);
                Console.ReadKey();
                Environment.Exit(-1);
            }
            try
            {
                _serverSocket = new COServerSocket(new acceptCallback(this.acceptClient));
                _serverSocket.listen(int.Parse(_configReader["login-port"]));
            }
            catch(Exception e)
            {
                print("Error binding listener => " + e.Message);
                Console.ReadKey();
                Environment.Exit(-1);
            }
            
        }

        /// <summary>
        /// Accepts a client connection. This method is called from a
        /// COServerSocket instance delegate
        /// </summary>
        /// <param name="client">Clients socket connection</param>
        private void acceptClient(Socket client)
        {
            LoginHandler clienthandler = new LoginHandler(_configReader);
            COClientSocket newClient = new COClientSocket(client, new recieveCallback(proccessData), clienthandler);
            newClient.beginReceive();
            Console.WriteLine("SAD");
        }

        /// <summary>
        /// Called when data is received from a COClientSocket. Data is begins to
        /// be processed here.
        /// </summary>
        /// <param name="buffer">The data that was recieved</param>
        private void proccessData(COClientSocket client)
        {
            //Lock to prevent buffer from being edited before copied
            lock (client)
            {
                //If client disconnected remove them from
                //the consumer and return
                if (client.Disconnected)
                {
                    _workers.removeKey(client);
                    return;
                }
                

                //Copy bytes into anothor array to prevent race conditions
                //between worker threads processing this data while it's being written
                //to from a receive callback
                byte[] copy = new byte[client.BytesRead];
                Buffer.BlockCopy(client.Buffer, 0, copy, 0, client.BytesRead);

                _workers.addTask(client, copy);
            }
        }

        /// <summary>
        /// Main method for login
        /// </summary>
        public static void Main()
        {
            new Kernel();
        }
    }
}
