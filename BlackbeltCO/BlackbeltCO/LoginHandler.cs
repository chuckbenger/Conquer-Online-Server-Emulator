using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using BlackBeltCO.Core.Packets;
using BlackBeltCO.Core;
using BlackBeltCO.COSocket;


namespace BlackBeltCO.Login
{
    public class LoginHandler : IHandler
    {
        
        private Database _database; //Threads connection to the database
        private PacketReader _packetReader;
        private PacketWriter _packetWriter;

        public LoginHandler(XMLReader config)
        {
            _packetReader = new PacketReader();
            _packetWriter = new PacketWriter();
            _database = new Database();
            _database.Connect(config);
        }

        public void Handle(object task)
        {
            Console.WriteLine("Handling Task from thread " + System.Threading.Thread.CurrentThread.GetHashCode().ToString());
        }
    }
}
