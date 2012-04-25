using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace BlackBeltCO.Encryption
{
    public interface ISecureClient
    {
        IConquerCipher Cryptographer { get; }
    }
}
