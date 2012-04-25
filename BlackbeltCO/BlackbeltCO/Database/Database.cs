using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MySql.Data.MySqlClient;
using BlackBeltCO.Core;


namespace BlackBeltCO.Login
{
    public class Database
    {
      
        private MySqlConnection _connection; //Connection to the database

        /// <summary>
        /// Connects to the database server using the input xml paramenters.
        /// Returns true if connection was succefll other false.
        /// </summary>
        /// <param name="_reader">The xml reader holding the connection params</param>
        /// <returns>Returns true if connection was succefll other false.</returns>
        public bool Connect(XMLReader _reader)
        {
            string ip       = _reader["database-ip"];
            string user     = _reader["database-user"];
            string password = _reader["database-password"];
            string database = _reader["database"];

            string connectionString;

            if (ip == null || user == null || password == null || database == null)
            {
                Kernel.print("All database fields must be filled out!");
                return false;
            }

            //Build database connection string
            connectionString = @"SERVER=" + ip + ";DATABASE=" + database + ";UID=" + user 
                           + ";PASSWORD=" + password;
           
            try
            {
                _connection = new MySqlConnection(connectionString);
                _connection.Open();
            }
            catch (MySqlException e)
            {
                Kernel.print("Failed to connect to db => " + e.Message);
                return false;
            }

            return true;
        }


        /// <summary>
        /// Function to check if a user exists in the database
        /// </summary>
        /// <param name="username">The username of the user</param>
        /// <param name="password">The password of the user</param>
        /// <returns>Returns true if exists else false</returns>
        public bool UserExists(string username, string password)
        {
            bool exists = false;

            MySqlCommand cmd = new MySqlCommand(StoredProcedures.CHECK_IF_USER_EXISTS, _connection);
            cmd.CommandType = System.Data.CommandType.StoredProcedure;
            cmd.Parameters.AddWithValue("?iUsername", username);
            cmd.Parameters.AddWithValue("?iPassword", password);
            cmd.Parameters.Add(new MySqlParameter("?oExists", MySqlDbType.Bit));

            cmd.Parameters["?iUsername"].Direction = System.Data.ParameterDirection.Input;
            cmd.Parameters["?iPassword"].Direction = System.Data.ParameterDirection.Input;
            cmd.Parameters["?oExists"].Direction = System.Data.ParameterDirection.Output;
            
            try
            {
                cmd.ExecuteNonQuery();
                exists = cmd.Parameters["?oExists"].Value.ToString().Equals("1");
            }
            catch (MySqlException e)
            {
                Kernel.print("User exists failed => " + e.Message);
            }

            return exists;
        }
    }
}
