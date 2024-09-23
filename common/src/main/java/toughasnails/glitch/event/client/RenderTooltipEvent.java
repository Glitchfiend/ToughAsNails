/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.glitch.event.client;

import toughasnails.glitch.event.Event;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class RenderTooltipEvent extends Event
{
  private final ItemStack stack;
  private final GuiGraphics graphics;
  private final int x;
  private final int y;
  private final int screenWidth;
  private final int screenHeight;
  private final List<ClientTooltipComponent> components;
  private final Font fallbackFont;
  private final ClientTooltipPositioner positioner;

  public RenderTooltipEvent(ItemStack stack, GuiGraphics graphics, int x, int y, int screenWidth, int screenHeight, List<ClientTooltipComponent> components, Font fallbackFont, ClientTooltipPositioner positioner)
  {
    this.stack = stack;
    this.graphics = graphics;
    this.x = x;
    this.y = y;
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
    this.components = components;
    this.fallbackFont = fallbackFont;
    this.positioner = positioner;
  }

  public ItemStack getStack()
  {
    return this.stack;
  }

  public GuiGraphics getGraphics()
  {
    return graphics;
  }

  public int getX()
  {
    return x;
  }

  public int getY()
  {
    return y;
  }

  public int getScreenWidth()
  {
    return screenWidth;
  }

  public int getScreenHeight()
  {
    return screenHeight;
  }

  public List<ClientTooltipComponent> getComponents()
  {
    return components;
  }

  public Font getFallbackFont()
  {
    return fallbackFont;
  }

  public ClientTooltipPositioner getPositioner()
  {
    return positioner;
  }
}
