/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.fabric.mixin.client;

import toughasnails.glitch.event.EventManager;
import toughasnails.glitch.event.client.RenderTooltipEvent;
import toughasnails.fabric.gui.IExtendedGuiGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiGraphics.class)
public abstract class MixinGuiGraphics implements IExtendedGuiGraphics
{
  @Unique
  private ItemStack currentTooltipStack = ItemStack.EMPTY;

  @Shadow public abstract int guiWidth();

  @Shadow public abstract int guiHeight();

  @Inject(method="renderTooltip(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V", at=@At("HEAD"))
  private void onRenderTooltipHead(Font font, ItemStack itemStack, int i, int j, CallbackInfo ci)
  {
    this.currentTooltipStack = itemStack;
  }

  @Inject(method="renderTooltip(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V", at=@At("TAIL"))
  private void onRenderTooltipTail(Font font, ItemStack itemStack, int i, int j, CallbackInfo ci)
  {
    this.currentTooltipStack = ItemStack.EMPTY;
  }

  @Inject(method = "renderTooltipInternal", at=@At("HEAD"))
  private void onRenderTooltipInternal(Font fallbackFont, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, CallbackInfo ci)
  {
    EventManager.fire(new RenderTooltipEvent(this.currentTooltipStack, (GuiGraphics)(Object)this, x, y, this.guiWidth(), this.guiHeight(), components, fallbackFont, positioner));
  }

  @Override
  public ItemStack getCurrentTooltipStack()
  {
    return this.currentTooltipStack;
  }

  public void setCurrentTooltipStack(ItemStack stack)
  {
    this.currentTooltipStack = stack;
  }
}