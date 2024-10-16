/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.Gui.HeartType;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.temperature.TemperatureHooksClient;

@Mixin(Gui.class)
public abstract class MixinGui
{
    @Inject(method="renderHeart", at=@At(value="HEAD"), cancellable = true)
    private void onRenderHeart(GuiGraphics gui, Gui.HeartType heartType, int left, int top, int v,
        boolean isBlinking, boolean isHalf, CallbackInfo ci) {
        if (heartType == HeartType.NORMAL && TemperatureHelper.isFullyHyperthermic(Minecraft.getInstance().player)) {
            TemperatureHooksClient.heartBlit(gui, left, top, heartType.getX(isHalf, isBlinking), v, 9, 9);
            ci.cancel();
        }
    }
}