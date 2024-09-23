/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.forge.mixin.client;

import toughasnails.glitch.event.EventManager;
import toughasnails.glitch.event.client.RenderGuiEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = VanillaGuiOverlay.class, remap = false)
public abstract class MixinVanillaGuiOverlay
{
  @Mutable
  @Final
  @Shadow
  IGuiOverlay overlay;

  @Inject(method = "<init>", at=@At("RETURN"))
  private void onInit(String enumName, int ordinal, String id, IGuiOverlay overlay, CallbackInfo ci)
  {
    this.overlay = switch (enumName) {
      case "FROSTBITE" -> wrapRenderer(overlay, RenderGuiEvent.Type.FROSTBITE);
      case "FOOD_LEVEL" -> wrapRenderer(overlay, RenderGuiEvent.Type.FOOD);
      case "AIR_LEVEL" -> wrapRendererWithRightModification(overlay, RenderGuiEvent.Type.AIR);
      default -> overlay;
    };
  }

  @Unique
  private static IGuiOverlay wrapRenderer(IGuiOverlay original, RenderGuiEvent.Type type)
  {
    return (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
      EventManager.fire(new RenderGuiEvent.Pre(type, gui, guiGraphics, partialTick, screenWidth, screenHeight));
      original.render(gui, guiGraphics, partialTick, screenWidth, screenHeight);
    };
  }

  @Unique
  private static IGuiOverlay wrapRendererWithRightModification(IGuiOverlay original, RenderGuiEvent.Type type)
  {
    return (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
      int rightTop = screenHeight - gui.rightHeight;
      var event = new RenderGuiEvent.Pre(type, gui, guiGraphics, partialTick, screenWidth, screenHeight, rightTop);
      EventManager.fire(event);
      gui.rightHeight = screenHeight - event.getRowTop();
      original.render(gui, guiGraphics, partialTick, screenWidth, screenHeight);
    };
  }
}
