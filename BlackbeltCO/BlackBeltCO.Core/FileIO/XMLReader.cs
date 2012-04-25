using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using System.IO;

namespace BlackBeltCO.Core
{
    /// <summary>
    /// Basic class for reading in XMl files
    /// </summary>
    public class XMLReader
    {
        private  XmlTextReader             _reader; //Reads xml files
        private  Dictionary<string,string> _data; //Holds xml data read from file

        public XMLReader()
        {
            _data = new Dictionary<string,string>();
        }
        
        /// <summary>
        /// Returns the xml node elements value. If node not found null is returned
        /// </summary>
        /// <param name="element">The element value you wish to get</param>
        /// <returns></returns>
        public string this[string element]
        {
            get
            {
                try
                {
                    return _data[element];
                }
                catch (Exception)
                {
                    return null;
                }
            }
        }

        /// <summary>
        /// Reads the xml data found at the specified path into a dictionary that can be 
        /// accessed through this[element].
        /// </summary>
        /// <param name="path">The path to the xml file to read in</param>
        public bool read(string path)
        {
            //Clear previous data
            _data.Clear();

            string element = String.Empty;

            //File not found return
            if (!File.Exists(path))
                return false;
            
            
            _reader = new XmlTextReader(path);

            //Read in xml data
            while (_reader.Read())
            {
                try
                {
                    if (_reader.NodeType == XmlNodeType.Element)
                        element = _reader.Name;
                    else if (_reader.NodeType == XmlNodeType.Text)
                        _data.Add(element, _reader.Value);
                    
                }
                catch (Exception)
                {
                    return false;
                }
            }

            _reader.Close();

            return true;
        }
    }
}
