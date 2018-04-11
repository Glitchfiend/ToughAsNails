/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.entities.EntityFreeze;
import toughasnails.temperature.TemperatureHandler;
import toughasnails.temperature.modifier.SeasonModifier;

public class ModCompat
{
    @GameRegistry.ObjectHolder("biomesoplenty:hot_spring_water")
    public static final Block HOT_SPRING_WATER = null;
    
    public static Biome alps = null;
    public static Biome cold_desert = null;
    public static Biome alps_foothills = null;
    public static Biome glacier = null;
    public static Biome polar_chasm = null;
    
    public static void postInit()
    {
        alps = ForgeRegistries.BIOMES.getValue(new ResourceLocation("biomesoplenty:alps"));
        cold_desert = ForgeRegistries.BIOMES.getValue(new ResourceLocation("biomesoplenty:cold_desert"));
        alps_foothills = ForgeRegistries.BIOMES.getValue(new ResourceLocation("biomesoplenty:alps_foothills"));
        glacier = ForgeRegistries.BIOMES.getValue(new ResourceLocation("biomesoplenty:glacier"));
        polar_chasm = ForgeRegistries.BIOMES.getValue(new ResourceLocation("biomesoplenty:polar_chasm"));
        
        ModEntities.addSpawn(EntityFreeze.class, 10, 1, 3, EnumCreatureType.MONSTER, alps, cold_desert, alps_foothills, glacier, polar_chasm);

        if (Loader.isModLoaded("sereneseasons"))
        {
            TemperatureHelper.registerTemperatureModifier(new SeasonModifier("season"));
        }
    }
}