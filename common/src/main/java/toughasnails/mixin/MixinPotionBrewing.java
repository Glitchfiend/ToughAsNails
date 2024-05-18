/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.api.potion.TANPotions;

@Mixin(PotionBrewing.class)
public class MixinPotionBrewing
{
    @Inject(method="addVanillaMixes", at=@At("TAIL"))
    private static void addVanillaMixes(PotionBrewing.Builder builder, CallbackInfo ci)
    {
        builder.addMix(Potions.AWKWARD, Items.SNOWBALL, TANPotions.ICE_RESISTANCE);
        builder.addMix(TANPotions.ICE_RESISTANCE, Items.REDSTONE, TANPotions.LONG_ICE_RESISTANCE);
    }
}
