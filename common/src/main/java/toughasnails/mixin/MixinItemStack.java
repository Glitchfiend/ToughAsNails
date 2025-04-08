/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.api.item.TANItems;

import javax.annotation.Nullable;

@Mixin(ItemStack.class)
public abstract class MixinItemStack
{
    @Shadow public abstract Item getItem();

    @Shadow @org.jetbrains.annotations.Nullable public abstract <T> T set(DataComponentType<T> dataComponentType, @org.jetbrains.annotations.Nullable T object);

    @Inject(method="inventoryTick", at=@At(value="TAIL"))
    public void onTick(Level level, Entity entity, @Nullable EquipmentSlot slot, CallbackInfo ci)
    {
        // As of 1.21.5 the item-based inventory tick isn't called on the client
        if (!level.isClientSide())
            return;

        Item item = this.getItem();

        if (item == TANItems.LEAF_BOOTS || item == TANItems.LEAF_LEGGINGS || item == TANItems.LEAF_CHESTPLATE || item == TANItems.LEAF_HELMET)
            this.set(DataComponents.DYED_COLOR, new DyedItemColor(BiomeColors.getAverageFoliageColor(entity.level(), entity.blockPosition())));
    }
}
