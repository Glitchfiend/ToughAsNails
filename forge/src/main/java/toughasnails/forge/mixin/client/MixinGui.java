/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.forge.mixin.client;

import net.minecraft.client.gui.Hud;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.temperature.TemperatureHooksClient;

@Mixin(Hud.class)
public class MixinGui
{
    @Inject(method="extractSelectedItemName(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V", at=@At(value="HEAD"), remap = false)
    public void onRenderSelectedItemNameBegin(GuiGraphicsExtractor guiGraphics, CallbackInfo ci)
    {
        guiGraphics.pose().pushMatrix();
        TemperatureHooksClient.adjustSelectedItemText(guiGraphics);
    }

    @Inject(method="extractSelectedItemName(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V", at=@At(value="TAIL"), remap = false)
    public void onRenderSelectedItemNameEnd(GuiGraphicsExtractor guiGraphics, CallbackInfo ci)
    {
        guiGraphics.pose().popMatrix();
    }
}
