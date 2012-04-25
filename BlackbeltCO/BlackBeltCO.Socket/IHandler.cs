using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace BlackBeltCO.COSocket
{
    public interface IHandler
    {
         void Handle(object task);
    }
}
