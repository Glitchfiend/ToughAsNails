/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin.client;

import net.minecraft.client.gui.Hud;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.temperature.TemperatureHooksClient;

@Mixin(value = Hud.class, remap = false)
public abstract class MixinGui
{
    @Inject(method="extractHeart", at=@At(value="HEAD"), cancellable = true)
    public void onRenderHeart(GuiGraphicsExtractor gui, Hud.HeartType heartType, int x, int y, boolean isHardcore, boolean isBlinking, boolean isHalf, CallbackInfo ci)
    {
        TemperatureHooksClient.heartBlit(gui, heartType, x, y, isHardcore, isBlinking, isHalf);
        ci.cancel();
    }
}
