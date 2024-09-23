/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.glitch.event.player;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;

public abstract class PlayerInteractEvent extends PlayerEvent
{
  private final InteractionHand hand;
  private InteractionResultHolder<ItemStack> cancelResult;

  public PlayerInteractEvent(Player player, InteractionHand hand)
  {
    super(player);
    this.hand = hand;
    this.cancelResult = InteractionResultHolder.pass(player.getItemInHand(hand));
  }

  @Override
  public boolean isCancellable()
  {
    return true;
  }

  public InteractionHand getHand()
  {
    return this.hand;
  }

  public ItemStack getItemStack()
  {
    return getPlayer().getItemInHand(hand);
  }

  public InteractionResultHolder<ItemStack> getCancelResult()
  {
    return this.cancelResult;
  }

  public void setCancelResult(InteractionResultHolder<ItemStack> result)
  {
    this.cancelResult = result;
  }


  public static class UseItem extends PlayerInteractEvent
  {
    public UseItem(Player player, InteractionHand hand)
    {
      super(player, hand);
    }
  }

  public static class UseBlock extends PlayerInteractEvent
  {
    private final BlockHitResult hitResult;

    public UseBlock(Player player, InteractionHand hand, BlockHitResult hitResult)
    {
      super(player, hand);
      this.hitResult = hitResult;
    }

    public BlockHitResult getHitResult()
    {
      return this.hitResult;
    }
  }

  public static class UseEmpty extends PlayerInteractEvent
  {
    public UseEmpty(Player player, InteractionHand hand)
    {
      super(player, hand);
    }

    @Override
    public boolean isCancellable()
    {
      return false;
    }
  }
}
