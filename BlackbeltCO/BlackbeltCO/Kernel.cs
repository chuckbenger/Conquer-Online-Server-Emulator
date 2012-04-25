using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using BlackBeltCO.Core;
using BlackBeltCO.COSocket;


namespace BlackBeltCO.Login
{
    public class Kernel
    {

        private const string _NAME      = "BlackBeltCO "; //Name of the server for console output
        private const string _DB_CONFIG = "config/database.xml";  //Holds the database configuration

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
                Kernel.print("Couldn't read " + _DB_CONFIG);
                Console.ReadKey();
                Environment.Exit(-1);
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
