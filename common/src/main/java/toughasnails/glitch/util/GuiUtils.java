/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.glitch.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;

public class GuiUtils
{
  public static void setupOverlayRenderState(boolean blend, boolean depthTest)
  {
    if (blend)
    {
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
    }
    else RenderSystem.disableBlend();

    if (depthTest)
      RenderSystem.enableDepthTest();
    else
      RenderSystem.disableDepthTest();

    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
  }

  public static boolean shouldDrawSurvivalElements()
  {
    var minecraft = Minecraft.getInstance();
    return minecraft.gameMode.canHurtPlayer() && minecraft.getCameraEntity() instanceof Player;
  }
}
