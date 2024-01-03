/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.api.potion.TANEffects;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.core.ToughAsNails;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements Attackable
{
    public MixinLivingEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Redirect(method="aiStep", at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/LivingEntity;setTicksFrozen(I)V"))
    public void onAiStep_setTicksFrozen(LivingEntity instance, int ticks)
    {
        if (!((Object)this instanceof Player))
        {
            this.setTicksFrozen(ticks);
        }
    }

    @Inject(method="aiStep", at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/LivingEntity;getTicksFrozen()I"))
    public void onAiStep_getTicksFrozen(CallbackInfo ci)
    {
        // Only apply to players
        if (!((Object)this instanceof Player))
        {
            return;
        }

        Player player = (Player)(Object)this;
        ITemperature data = TemperatureHelper.getTemperatureData(player);
        int prevTicksFrozen = player.getTicksFrozen();

        if (!player.hasEffect(TANEffects.ICE_RESISTANCE))
        {
            if (!player.isCreative() && !player.isSpectator() && data.getLevel() == TemperatureLevel.ICY && data.getExtremityDelayTicks() == 0)
            {
                // Add 2 to the ticksRequiredToFreeze to cause damage
                player.setTicksFrozen(Math.min(player.getTicksRequiredToFreeze() + 2, player.getTicksFrozen() + 2));
            }
            else if (this.isInPowderSnow && this.canFreeze())
            {
                this.setTicksFrozen(Math.min(this.getTicksRequiredToFreeze(), player.getTicksFrozen() + 1));
            }
        }
        else
        {
            // Set frozen ticks to 0 if ice resistance is active
            player.setTicksFrozen(0);
        }

        // If the ticksFrozen hasn't changed, do melting
        if (prevTicksFrozen == player.getTicksFrozen())
        {
            this.setTicksFrozen(Math.max(0, player.getTicksFrozen() - 2));
        }
    }
}
