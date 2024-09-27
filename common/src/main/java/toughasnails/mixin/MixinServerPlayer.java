/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin;

import static toughasnails.temperature.TemperatureHandler.syncTemperature;
import static toughasnails.thirst.ThirstHandler.syncThirst;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.api.player.ITANPlayer;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.init.ModConfig;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Player implements ITANPlayer
{
    public MixinServerPlayer(Level $$0, BlockPos $$1, float $$2, GameProfile $$3)
    {
        super($$0, $$1, $$2, $$3);
    }

    @Inject(method="doTick", at=@At(value="TAIL"))
    public void onDoTick(CallbackInfo ci)
    {
        ServerPlayer player = (ServerPlayer)(Player)this;
        ITemperature data = TemperatureHelper.getTemperatureData(player);
        IThirst thirst = ThirstHelper.getThirst(player);

        // Update the temperature if it has changed
        if (data.getLastLevel() != data.getLevel() || data.getLastHyperthermiaTicks() != data.getHyperthermiaTicks() || !data.getLastNearbyThermoregulators().equals(data.getNearbyThermoregulators()))
        {
            syncTemperature(player);
        }

        // Update thirst if it has changed
        if (thirst.getLastThirst() != thirst.getThirst() || thirst.getHydration() == 0.0F != thirst.getLastHydrationZero())
        {
            syncThirst(player);
        }
    }

    @Inject(method="restoreFrom", at=@At(value="TAIL"))
    public void onRestoreFrom(ServerPlayer player, boolean $$1, CallbackInfo ci)
    {
        if (!ModConfig.temperature.climateClemencyRespawning)
        {
            this.setClimateClemencyGranted(((ITANPlayer)player).getClimateClemencyGranted());
        }
    }
}
