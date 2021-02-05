/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.config.ServerConfig;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin
{
    @Inject(method = "causeFoodExhaustion", at = @At("HEAD"))
    protected void onCauseFoodExhaustion(float exhaustion, CallbackInfo ci)
    {
        if (ServerConfig.enableThirst.get())
        {
            ThirstHelper.getThirst((PlayerEntity) (Object) this).addExhaustion(exhaustion);
        }
    }
}
