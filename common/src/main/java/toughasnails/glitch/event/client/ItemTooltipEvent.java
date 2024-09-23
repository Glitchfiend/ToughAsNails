/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.glitch.event.client;

import toughasnails.glitch.event.Event;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ItemTooltipEvent extends Event
{
  private final ItemStack stack;
  private final List<Component> tooltip;

  public ItemTooltipEvent(ItemStack stack, List<Component> tooltip)
  {
    this.stack = stack;
    this.tooltip = tooltip;
  }

  public ItemStack getStack()
  {
    return this.stack;
  }

  public List<Component> getTooltip()
  {
    return this.tooltip;
  }
}
