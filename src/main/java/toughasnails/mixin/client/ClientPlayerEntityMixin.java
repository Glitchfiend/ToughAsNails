///*******************************************************************************
// * Copyright 2021, the Glitchfiend Team.
// * All rights reserved.
// ******************************************************************************/
//package toughasnails.mixin.client;
//
//import com.mojang.authlib.GameProfile;
//import net.minecraft.client.player.AbstractClientPlayer;
//import net.minecraft.client.player.LocalPlayer;
//import net.minecraft.client.multiplayer.ClientLevel;
//import net.minecraft.client.player.Input;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.Redirect;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import toughasnails.api.thirst.ThirstHelper;
//
//@Mixin(LocalPlayer.class)
//public abstract class ClientPlayerEntityMixin extends AbstractClientPlayer
//{
//    @Shadow
//    public Input input;
//
//    public ClientPlayerEntityMixin(ClientLevel world, GameProfile profile)
//    {
//        super(world, profile);
//    }
//
//    // Stop existing sprinting if there is insufficient thirst
//    @Inject(method = "aiStep", at = @At("HEAD"))
//    protected void onAiStep(CallbackInfo ci)
//    {
//        if (this.isSprinting())
//        {
//            boolean sprintingAllowable = canSprintWithThirst((LocalPlayer)(Object)this);
//
//            if (this.isSwimming())
//            {
//                if (!this.onGround && !this.input.shiftKeyDown && !sprintingAllowable || !this.isInWater())
//                {
//                    this.setSprinting(false);
//                }
//            }
//            else if (!sprintingAllowable)
//            {
//                this.setSprinting(false);
//            }
//        }
//    }
//
//    @Redirect(method={"aiStep"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/entity/player/ClientPlayerEntity;setSprinting(Z)V"))
//    protected void aiStep_setSprinting(LocalPlayer player, boolean sprinting)
//    {
//        // Don't allow sprinting if the player has insufficient thirst
//        if (sprinting && !canSprintWithThirst(player))
//            sprinting = false;
//
//        player.setSprinting(sprinting);
//    }
//
//    private boolean canSprintWithThirst(LocalPlayer player)
//    {
//        return ThirstHelper.getThirst(player).getThirst() > 6 || player.abilities.mayfly;
//    }
//}
