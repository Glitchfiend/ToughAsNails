/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.util;

import java.awt.Color;

public class ColourUtil 
{
    public static int multiplyColours(int colour1, int colour2)
    {
        //Convert each colour to a scale between 0 and 1 and multiply them
        //Multiply by 255 to bring back between 0 and 255
        return (int)((colour1 / 255.0F) * (colour2 / 255.0F) * 255.0F);
    }

    public static int overlayBlendChannel(int underColour, int overColour)
    {
        int retVal;
        if (underColour < 128)
        {
            retVal = multiplyColours(2 * underColour, overColour);
        }
        else
        {
            retVal = multiplyColours(2 * (255 - underColour), 255 - overColour);
            retVal = 255 - retVal;
        }
        return retVal;
    }
    
    public static int overlayBlend(int underColour, int overColour)
    {
        Color colour1 = new Color(underColour);
        Color colour2 = new Color(overColour);
        
        return new Color(overlayBlendChannel(colour1.getRed(), colour2.getRed()), overlayBlendChannel(colour1.getGreen(), colour2.getGreen()), overlayBlendChannel(colour1.getBlue(), colour2.getBlue())).getRGB();
    }
}
