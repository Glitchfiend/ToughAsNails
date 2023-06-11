/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.temperature.TemperatureHooks;

@Mixin(Gui.class)
public abstract class MixinGui
{
    @Inject(method="renderHeart", at=@At(value="HEAD"), cancellable = true)
    public void onRenderHeart(GuiGraphics gui, Gui.HeartType heartType, int left, int top, int v, boolean isBlinking, boolean isHalf, CallbackInfo ci)
    {
        TemperatureHooks.heartBlit(gui, left, top, heartType.getX(isHalf, isBlinking), v, 9, 9);
        ci.cancel();
    }
}
