/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin.glitchcore.client;

import glitchcore.event.EventManager;
import glitchcore.event.PlayerInteractEvent;
import glitchcore.event.RenderGuiEvent;
import glitchcore.event.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public class MixinMinecraft
{
    @Shadow
    public HitResult hitResult;

    @Shadow @Nullable public LocalPlayer player;

    @Inject(method="tick", at=@At(value="HEAD"))
    public void onBeginTick(CallbackInfo ci)
    {
        EventManager.fire(new TickEvent.Client(TickEvent.Phase.START));
    }

    @Inject(method="tick", at=@At(value="TAIL"))
    public void onEndTick(CallbackInfo ci)
    {
        EventManager.fire(new TickEvent.Client(TickEvent.Phase.END));
    }

    @Unique
    private ItemStack startUseItem_stack;
    @Unique
    private InteractionHand startUseItem_hand;

    @Redirect(method="startUseItem", at=@At(value = "INVOKE", target = "net/minecraft/client/player/LocalPlayer.getItemInHand (Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;", ordinal = 0), require = 1)
    public ItemStack startUseItem_getItemInHand(LocalPlayer instance, InteractionHand hand)
    {
        this.startUseItem_hand = hand;
        return this.startUseItem_stack = instance.getItemInHand(hand);
    }

    @Inject(method="startUseItem",
            slice = @Slice(from = @At(value = "INVOKE", target = "net/minecraft/client/renderer/ItemInHandRenderer.itemUsed (Lnet/minecraft/world/InteractionHand;)V", ordinal = 0)),
            at=@At(value = "INVOKE", target = "net/minecraft/world/item/ItemStack.isEmpty()Z", ordinal = 0))
    public void onStartUseItem(CallbackInfo ci)
    {
        if (this.startUseItem_stack.isEmpty() && (this.hitResult == null || this.hitResult.getType() == HitResult.Type.MISS))
        {
            EventManager.fire(new PlayerInteractEvent.UseEmpty(this.player, this.startUseItem_hand));
        }
    }
}
