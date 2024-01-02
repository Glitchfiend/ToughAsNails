/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import toughasnails.init.ModTags;

@Mixin(Item.class)
public class MixinItem
{
    @Redirect(method="use", at=@At(value = "INVOKE", target="Lnet/minecraft/world/entity/player/Player;canEat(Z)Z"))
    public boolean onUse(Player player, boolean canAlwaysEat, Level level, Player player2, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        return stack.is(ModTags.Items.HEATING_CONSUMED_ITEMS) || stack.is(ModTags.Items.COOLING_CONSUMED_ITEMS) || player.canEat(canAlwaysEat);
    }
}
