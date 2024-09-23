/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.api.player.ITANPlayer;
import toughasnails.api.potion.TANEffects;
import toughasnails.glitch.event.EventManager;
import toughasnails.glitch.event.player.PlayerEvent;
import toughasnails.init.ModConfig;

@Mixin(ServerLevel.class)
public class MixinServerLevel
{
    @Inject(method="addPlayer", at=@At(value="HEAD"))
    public void onAddPlayer(ServerPlayer player, CallbackInfo ci)
    {
        EventManager.fire(new PlayerEvent.JoinLevel(player));
        ITANPlayer tanPlayer = (ITANPlayer)player;

        if (ModConfig.temperature.enableTemperature && ModConfig.temperature.climateClemencyDuration > 0 && !tanPlayer.getClimateClemencyGranted() && !player.isCreative())
        {
            tanPlayer.setClimateClemencyGranted(true);
            player.addEffect(new MobEffectInstance(TANEffects.CLIMATE_CLEMENCY, ModConfig.temperature.climateClemencyDuration, 0, false, false, true));
        }
    }
}
