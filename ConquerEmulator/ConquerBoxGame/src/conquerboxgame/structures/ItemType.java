/**
 * **********************************************************************
 * Copyright 2012 Charles Benger
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * ***************************************************************************
 */
package conquerboxgame.structures;

/**
 *
 * @author chuck
 */
public class ItemType 
{

            public String Name;
            public String Description;
            public int ID;
            public int Price;
            public short Class, Prof, Level, Str_Req, Dex_Req, Vit_Req,
                Spi_Req, Damage_Max, Damage_Min, Defense_Add, Dex_Add, Dodge_Add,
                HP_Add, MP_Add, Magic_Attack, MDefense_Add, Dura, MaxDura, Frequency;
            public byte Range, TradeType;

}
