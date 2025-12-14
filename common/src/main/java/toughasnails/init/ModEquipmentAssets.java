/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import toughasnails.core.ToughAsNails;

import java.util.function.BiConsumer;

public class ModEquipmentAssets
{
    public static final ResourceKey<EquipmentAsset> LEAF = createId("leaf");
    public static final ResourceKey<EquipmentAsset> WOOL = createId("wool");

    static ResourceKey<EquipmentAsset> createId(String name)
    {
        return ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(ToughAsNails.MOD_ID, name));
    }
}
