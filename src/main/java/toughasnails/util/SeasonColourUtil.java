/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.util;

import java.awt.Color;

import net.minecraft.util.math.MathHelper;
import toughasnails.api.season.Season.SubSeason;

public class SeasonColourUtil 
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
        int r = overlayBlendChannel(colour1.getRed(), colour2.getRed());
        int g = overlayBlendChannel(colour1.getGreen(), colour2.getGreen());
        int b = overlayBlendChannel(colour1.getBlue(), colour2.getBlue());
        
        return r << 16 | g << 8 | b;
    }
    
    public static int saturateColour(int colour, float saturationMultiplier)
    {
        float[] hsb = Color.RGBtoHSB(colour >> 16, colour >> 8, colour, null);
        hsb[1] = MathHelper.clamp_int((int)(colour * saturationMultiplier), 0, 100);
        return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
    }
    
    public static int applySeasonalGrassColouring(SubSeason season, int originalColour)
    {
        int overlay = season.getGrassOverlay();
        float saturationMultiplier = season.getGrassSaturationMultiplier();
        int newColour = overlay == 0xFFFFFF ? originalColour : overlayBlend(originalColour, overlay);
        return saturationMultiplier != -1 ? saturateColour(newColour, saturationMultiplier) : newColour;
    }
    
    public static int applySeasonalFoliageColouring(SubSeason season, int originalColour)
    {
        int overlay = season.getFoliageOverlay();
        float saturationMultiplier = season.getFoliageSaturationMultiplier();
        int newColour = overlay == 0xFFFFFF ? originalColour : overlayBlend(originalColour, overlay);
        return saturationMultiplier != -1 ? saturateColour(newColour, saturationMultiplier) : newColour;
    }
}
