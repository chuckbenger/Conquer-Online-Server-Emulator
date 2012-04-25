using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace BlackBeltCO.COSocket
{
    public interface IWorkable
    {
         IHandler Handler { get; }
    }
}
