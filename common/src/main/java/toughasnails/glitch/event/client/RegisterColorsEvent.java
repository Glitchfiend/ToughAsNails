/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.glitch.event.client;

import toughasnails.glitch.event.Event;
import java.util.function.BiConsumer;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;

public abstract class RegisterColorsEvent<ObjColor, Obj> extends Event
{
  private final BiConsumer<ObjColor, Obj> register;

  public RegisterColorsEvent(BiConsumer<ObjColor, Obj> register)
  {
    this.register = register;
  }

  public void register(ObjColor color, Obj... objs)
  {
    for (var obj : objs)
    {
      this.register.accept(color, obj);
    }
  }

  public static class Item extends RegisterColorsEvent<ItemColor, ItemLike>
  {
    public Item(BiConsumer<ItemColor, ItemLike> register)
    {
      super(register);
    }
  }

  public static class Block extends RegisterColorsEvent<BlockColor, net.minecraft.world.level.block.Block>
  {
    public Block(BiConsumer<BlockColor, net.minecraft.world.level.block.Block> register)
    {
      super(register);
    }
  }
}
