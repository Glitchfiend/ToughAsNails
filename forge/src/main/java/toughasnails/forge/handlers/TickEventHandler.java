/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.forge.handlers;

import toughasnails.glitch.event.EventManager;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TickEventHandler
{
  @SubscribeEvent
  public static void onLevelTick(TickEvent.LevelTickEvent event)
  {
    toughasnails.glitch.event.TickEvent.Phase phase = switch (event.phase) {
      case START -> toughasnails.glitch.event.TickEvent.Phase.START;
      case END -> toughasnails.glitch.event.TickEvent.Phase.END;
    };

    EventManager.fire(new toughasnails.glitch.event.TickEvent.Level(phase, event.level));
  }
}