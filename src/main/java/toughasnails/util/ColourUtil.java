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
    public static int scaleMult(int a, int b)
    {
        return (a * b) + 128;
    }

    public static int scaleDiv(int r)
    {
        return ((r >> 8) + r) >> 8;
    }

    public static int scale(int a, int b)
    {
        return scaleDiv(scaleMult(a, b));
    }

    public static int overlayBlendChannel(int underColour, int overColour)
    {
        int retVal;
        if (underColour < 128)
        {
            retVal = scale(2 * underColour, overColour);
        }
        else
        {
            retVal = scale(2 * (255 - underColour), 255 - overColour);
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
