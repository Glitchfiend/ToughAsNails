/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.thirst.ThirstHooks;

@Mixin(value = FoodData.class, remap = false)
public abstract class MixinFoodData
{
    @Inject(method="tick", at=@At(value="HEAD"), cancellable = true)
    public void onTick(ServerPlayer player, CallbackInfo ci)
    {
        ThirstHooks.doFoodDataTick((FoodData)(Object)this, player);
        ci.cancel();
    }
}
