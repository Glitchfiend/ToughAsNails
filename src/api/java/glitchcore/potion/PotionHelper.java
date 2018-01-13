/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package glitchcore.potion;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class PotionHelper
{
    public static void registerPotion(ResourceLocation location, Potion potion)
    {
        potion.setRegistryName(location);
        ForgeRegistries.POTIONS.register(potion);
    }

    public static void registerPotionType(ResourceLocation location, PotionType potionType)
    {
        potionType.setRegistryName(location);
        ForgeRegistries.POTION_TYPES.register(potionType);
    }
}
