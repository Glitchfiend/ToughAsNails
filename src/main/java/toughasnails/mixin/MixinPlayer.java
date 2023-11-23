/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.thirst.ThirstHooks;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity
{
    @Shadow
    protected FoodData foodData = new FoodData();

    @Shadow
    @Final
    private Abilities abilities;

    private MixinPlayer(EntityType<? extends LivingEntity> type, Level level)
    {
        super(type, level);
    }

    @Inject(method="causeFoodExhaustion", at=@At(value="HEAD"))
    public void onCauseFoodExhaustion(float exhaustion, CallbackInfo ci)
    {
        if (!this.abilities.invulnerable)
        {
            if (!this.level().isClientSide)
            {
                ThirstHooks.onCauseFoodExhaustion((Player)(Object)this, exhaustion);
            }
        }
    }
}
