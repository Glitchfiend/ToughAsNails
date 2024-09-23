/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.forge.handlers;

import toughasnails.glitch.event.EventManager;
import toughasnails.glitch.event.client.LevelRenderEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraftforge.client.event.RenderLevelStageEvent.Stage.AFTER_PARTICLES;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LevelRenderEventHandler
{
  @SubscribeEvent
  public static void onRender(RenderLevelStageEvent event)
  {
    if (event.getStage().equals(AFTER_PARTICLES))
    {
      fireStage(LevelRenderEvent.Stage.AFTER_PARTICLES, event);
    }
  }

  private static void fireStage(LevelRenderEvent.Stage stage, RenderLevelStageEvent event)
  {
    EventManager.fire(new LevelRenderEvent(stage, event.getLevelRenderer(), event.getPoseStack(), event.getProjectionMatrix(), event.getRenderTick(), event.getPartialTick(), event.getCamera(), event.getFrustum()));
  }
}