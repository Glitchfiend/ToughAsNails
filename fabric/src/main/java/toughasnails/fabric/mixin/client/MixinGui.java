/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.fabric.mixin.client;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.glitch.event.EventManager;
import toughasnails.glitch.event.client.RenderGuiEvent;
import toughasnails.temperature.TemperatureHooksClient;

@Mixin(Gui.class)
public class MixinGui
{
    @Shadow
    private int screenWidth;
    @Shadow
    private int screenHeight;

    @Unique
    private float partialTicks;

    @Inject(method="render", at=@At(value="HEAD"))
    public void onRender(GuiGraphics guiGraphics, float partialTicks, CallbackInfo ci)
    {
        this.partialTicks = partialTicks;
    }

    @Inject(method="render", at=@At(value="INVOKE", target="net/minecraft/client/player/LocalPlayer.getTicksFrozen()I"))
    private void onBeginRenderFrozenOverlay(GuiGraphics guiGraphics, float partialTicks, CallbackInfo ci)
    {
        EventManager.fire(new RenderGuiEvent.Pre(RenderGuiEvent.Type.FROSTBITE, (Gui)(Object)this, guiGraphics, this.partialTicks, this.screenWidth, this.screenHeight));
    }

    @Inject(method="renderPlayerHealth", at=@At(value="INVOKE", target="net/minecraft/client/gui/Gui.getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I"))
    private void onRenderPlayerHealth(GuiGraphics guiGraphics, CallbackInfo ci)
    {
        EventManager.fire(new RenderGuiEvent.Pre(RenderGuiEvent.Type.FOOD, (Gui)(Object)this, guiGraphics, this.partialTicks, this.screenWidth, this.screenHeight));
    }

    @ModifyVariable(method="renderPlayerHealth", at=@At(value="INVOKE", target="net/minecraft/world/entity/player/Player.getMaxAirSupply()I"), ordinal = 10, require = 1)
    private int onBeginRenderAir(int rightTop, GuiGraphics guiGraphics)
    {
        var event = new RenderGuiEvent.Pre(RenderGuiEvent.Type.AIR, (Gui)(Object)this, guiGraphics, this.partialTicks, this.screenWidth, this.screenHeight, rightTop + 10);
        EventManager.fire(event);
        return event.getRowTop() - 10;
    }

    //

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
