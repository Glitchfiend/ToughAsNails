/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.forge.handlers;

import toughasnails.glitch.event.EventManager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VillagerTradesEventHandler
{
  @SubscribeEvent
  public static void onWandererTrades(WandererTradesEvent event)
  {
    var gcEvent = new toughasnails.glitch.event.village.WandererTradesEvent();
    EventManager.fire(gcEvent);

    event.getGenericTrades().addAll(gcEvent.getGenericTrades());
    event.getRareTrades().addAll(gcEvent.getRareTrades());
  }

  @SubscribeEvent
  public static void onVillagerTrades(VillagerTradesEvent event)
  {
    for (int level = VillagerData.MIN_VILLAGER_LEVEL; level <= VillagerData.MAX_VILLAGER_LEVEL; level++)
    {
      EventManager.fire(new toughasnails.glitch.event.village.VillagerTradesEvent(event.getType(), level, event.getTrades().get(level)));
    }
  }
}