/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import toughasnails.core.ToughAsNails;
import toughasnails.thirst.ThirstHooks;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer
{
    @Redirect(method="aiStep", at=@At(value="INVOKE", target="Lnet/minecraft/client/player/LocalPlayer;setSprinting(Z)V"))
    public void aiStep_setSprinting(LocalPlayer player, boolean sprinting)
    {
        ThirstHooks.onAiStepSetSprinting(player, sprinting);
    }
}
