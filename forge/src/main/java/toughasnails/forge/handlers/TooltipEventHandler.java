/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.forge.handlers;

import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import toughasnails.glitch.event.EventManager;

@Mod.EventBusSubscriber
public class TooltipEventHandler
{
  @SubscribeEvent
  public static void onItemTooltip(ItemTooltipEvent event)
  {
    EventManager.fire(new toughasnails.glitch.event.client.ItemTooltipEvent(
        event.getItemStack(), event.getToolTip()));
  }
}