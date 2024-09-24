/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.fabric.mixin.client;

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
    @Inject(method="renderSelectedItemName", at=@At(value="HEAD"))
    public void onRenderSelectedItemNameBegin(GuiGraphics guiGraphics, CallbackInfo ci)
    {
        guiGraphics.pose().pushPose();
        TemperatureHooksClient.adjustSelectedItemText(guiGraphics);
    }

    @Inject(method="renderSelectedItemName", at=@At(value="TAIL"))
    public void onRenderSelectedItemNameEnd(GuiGraphics guiGraphics, CallbackInfo ci)
    {
        guiGraphics.pose().popPose();
    }
}
