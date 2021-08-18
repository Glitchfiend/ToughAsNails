///*******************************************************************************
// * Copyright 2021, the Glitchfiend Team.
// * All rights reserved.
// ******************************************************************************/
//package toughasnails.mixin;
//
//import net.minecraft.world.entity.player.Player;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import toughasnails.api.thirst.ThirstHelper;
//import toughasnails.config.ServerConfig;
//
//@Mixin(Player.class)
//public abstract class PlayerEntityMixin
//{
//    @Inject(method = "causeFoodExhaustion", at = @At("HEAD"))
//    protected void onCauseFoodExhaustion(float exhaustion, CallbackInfo ci)
//    {
//        if (ServerConfig.enableThirst.get())
//        {
//            ThirstHelper.getThirst((Player) (Object) this).addExhaustion(exhaustion);
//        }
//    }
//}
