/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.forge.mixin.client;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.temperature.TemperatureHooksClient;

@Mixin(Gui.class)
public class MixinGui
{
    @Inject(method="renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;I)V", at=@At(value="HEAD"), remap = false)
    public void onRenderSelectedItemNameBegin(GuiGraphics guiGraphics, int yShift, CallbackInfo ci)
    {
        guiGraphics.pose().pushPose();
        TemperatureHooksClient.adjustSelectedItemText(guiGraphics);
    }

    @Inject(method="renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;I)V", at=@At(value="TAIL"), remap = false)
    public void onRenderSelectedItemNameEnd(GuiGraphics guiGraphics, int yShift, CallbackInfo ci)
    {
        guiGraphics.pose().popPose();
    }
}
