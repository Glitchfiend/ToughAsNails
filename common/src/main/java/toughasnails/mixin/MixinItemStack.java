/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin;

import toughasnails.glitch.event.EventManager;
import toughasnails.glitch.event.entity.LivingEntityUseItemEvent;
import toughasnails.glitch.event.player.PlayerInteractEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class MixinItemStack
{
  @Shadow public abstract ItemStack copy();

  @Unique
  private ItemStack finishUsingItemCopy;

  @Inject(method="use", at=@At("HEAD"), cancellable = true)
  public void onUse(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir)
  {
    var event = new PlayerInteractEvent.UseItem(player, hand);
    EventManager.fire(event);

    if (event.isCancelled())
    {
      cir.setReturnValue(event.getCancelResult());
    }
  }

  @Inject(method="finishUsingItem", at=@At("HEAD"))
  public void onFinishUsingItemBegin(Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir)
  {
    // Store a copy of the item before it is used (tags are wiped after)
    this.finishUsingItemCopy = this.copy();
  }

  @Inject(method="finishUsingItem", at=@At("TAIL"), cancellable = true)
  public void onFinishUsingItem(Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir)
  {
    var event = new LivingEntityUseItemEvent.Finish(entity, this.finishUsingItemCopy, cir.getReturnValue());
    EventManager.fire(event);
    cir.setReturnValue(event.getResult());
  }
}