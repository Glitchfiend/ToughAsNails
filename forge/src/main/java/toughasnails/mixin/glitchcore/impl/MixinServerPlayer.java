/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin.glitchcore.impl;

import com.mojang.authlib.GameProfile;
import glitchcore.event.EventManager;
import glitchcore.event.PlayerEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.ITeleporter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import toughasnails.api.player.ITANPlayer;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.init.ModConfig;
import toughasnails.temperature.TemperatureHandler;

import static toughasnails.temperature.TemperatureHandler.syncTemperature;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Player
{

    public MixinServerPlayer(Level p_250508_, BlockPos p_250289_, float p_251702_, GameProfile p_252153_)
    {
        super(p_250508_, p_250289_, p_251702_, p_252153_);
    }

    @Inject(method="changeDimension", at=@At(value="TAIL"), remap = false)
    public void onChangeDimension(ServerLevel level, ITeleporter teleporter, CallbackInfoReturnable<Entity> cir)
    {
        EventManager.fire(new PlayerEvent.ChangeDimension((ServerPlayer)(Player)this));
    }
}
