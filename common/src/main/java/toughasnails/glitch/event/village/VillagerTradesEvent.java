/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.glitch.event.village;

import toughasnails.glitch.event.Event;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.List;

public class VillagerTradesEvent extends Event
{
  private final VillagerProfession profession;
  private final int level;
  private final List<VillagerTrades.ItemListing> trades;

  public VillagerTradesEvent(VillagerProfession profession, int level, List<VillagerTrades.ItemListing> trades)
  {
    this.profession = profession;
    this.level = level;
    this.trades = trades;
  }

  public VillagerProfession getProfession()
  {
    return this.profession;
  }

  public int getLevel()
  {
    return this.level;
  }

  public List<VillagerTrades.ItemListing> getTrades()
  {
    return this.trades;
  }
}
