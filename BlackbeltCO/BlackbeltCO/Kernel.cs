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
        private const string _DB_CONFIG = "config/database.xml";  //Holds the database configuration
        private Database _database;

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
            XMLReader configReader = new XMLReader();
            if (!configReader.read(_DB_CONFIG))
            {
                print("Couldn't read " + _DB_CONFIG);
                Console.ReadKey();
                Environment.Exit(-1);
            }

            _database = new Database();
            if (!_database.Connect(configReader))
            {
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
