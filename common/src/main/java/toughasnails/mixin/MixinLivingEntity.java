/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin;

import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import toughasnails.api.potion.TANEffects;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isDeadOrDying()Z", ordinal = 0) )
    public boolean onAiStep_isDeadOrDying(LivingEntity instance) {
        // Only apply to players
        // false is the default value
        if (!(instance instanceof Player)) {
            return false;
        }
        Player player = (Player) instance;
        ITemperature data = TemperatureHelper.getTemperatureData(player);

        if (!player.hasEffect(TANEffects.ICE_RESISTANCE))
        {
            if (player.isInPowderSnow && player.canFreeze()) {
                player.setTicksFrozen(Math.min(player.getTicksRequiredToFreeze(), player.getTicksFrozen() + 1));
            } else if (!player.isCreative() && !player.isSpectator() && data.getLevel() == TemperatureLevel.ICY && data.getExtremityDelayTicks() == 0) {
                // Add 2 to the ticksRequiredToFreeze to cause damage
                player.setTicksFrozen(Math.min(player.getTicksRequiredToFreeze() + 2, player.getTicksFrozen() + 2));
            } else {
                player.setTicksFrozen(Math.max(0, player.getTicksFrozen() - 2));
            }
        } else {
            // Set frozen ticks to 0 if ice resistance is active
            player.setTicksFrozen(0);
        }
        // return true to stop the process
        return true;
    }
}
