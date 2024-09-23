/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.forge.handlers;

import toughasnails.glitch.event.EventManager;
import toughasnails.glitch.event.client.RegisterColorsEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ColorsEventHandler
{
  @SubscribeEvent
  public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event)
  {
    EventManager.fire(new RegisterColorsEvent.Block(event::register));
  }

  @SubscribeEvent
  public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event)
  {
    EventManager.fire(new RegisterColorsEvent.Item(event::register));
  }
}