/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.season;

import java.io.IOException;

import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ColorizerGrass;
import toughasnails.api.season.Season.SubSeason;
import toughasnails.core.ToughAsNails;

public class SeasonColorReloadListener implements IResourceManagerReloadListener 
{
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        try
        {
            for (SubSeason season : SubSeason.values())
            {
                ResourceLocation grassLoc = new ResourceLocation(ToughAsNails.MOD_ID, "textures/colormap/grass_" + season.toString().toLowerCase() + ".png");
                SeasonColors.setGrassColorForSeason(season, TextureUtil.readImageData(resourceManager, grassLoc));
                
                ResourceLocation foliageLoc = new ResourceLocation(ToughAsNails.MOD_ID, "textures/colormap/foliage_" + season.toString().toLowerCase() + ".png");
                SeasonColors.setFoliageColorForSeason(season, TextureUtil.readImageData(resourceManager, foliageLoc));
            }
        }
        catch (IOException var3)
        {
            ;
        }
    }
}
