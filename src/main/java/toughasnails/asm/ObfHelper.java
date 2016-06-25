/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.asm;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class ObfHelper
{
    public static String createMethodDescriptor(boolean obfuscated, String returnType, String... types)
    {
        String result = "(";
        
        for (String type : types)
        {
            if (type.length() == 1) result += type;
            else
            {
                result += "L" + (obfuscated ? FMLDeobfuscatingRemapper.INSTANCE.unmap(type) : type) + ";";
            }
        }
        
        if (returnType.length() > 1)
        {
            returnType = "L" + unmapType(obfuscated, returnType) + ";";
        }
        
        result += ")" + returnType;
        
        return result;
    }
    
    public static String unmapType(boolean obfuscated, String type)
    {
        return obfuscated ? FMLDeobfuscatingRemapper.INSTANCE.unmap(type) : type;
    }
}
