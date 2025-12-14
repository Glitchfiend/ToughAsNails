/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipePropertySet;
import toughasnails.core.ToughAsNails;

public class ModRecipePropertySets
{
    public static final ResourceKey<RecipePropertySet> WATER_PURIFYING = register("water_purifying");

    private static ResourceKey<RecipePropertySet> register(String name)
    {
        return ResourceKey.create(RecipePropertySet.TYPE_KEY, Identifier.fromNamespaceAndPath(ToughAsNails.MOD_ID, name));
    }
}
