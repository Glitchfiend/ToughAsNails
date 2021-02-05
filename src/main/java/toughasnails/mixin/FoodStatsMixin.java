/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;

@Mixin(FoodStats.class)
public abstract class FoodStatsMixin
{
    @Shadow
    private int foodLevel;
    @Shadow
    private float saturationLevel;
    @Shadow
    private float exhaustionLevel;
    @Shadow
    private int tickTimer;
    @Shadow
    private int lastFoodLevel;

    @Shadow
    abstract void addExhaustion(float exhaustion);

    // Modify tick to also check for thirst before healing
    @Overwrite
    public void tick(PlayerEntity player)
    {
        Difficulty difficulty = player.level.getDifficulty();
        IThirst thirst = ThirstHelper.getThirst(player);

        this.lastFoodLevel = this.foodLevel;

        if (this.exhaustionLevel > 4.0F)
        {
            this.exhaustionLevel -= 4.0F;

            if (this.saturationLevel > 0.0F)
            {
                this.saturationLevel = Math.max(this.saturationLevel - 1.0F, 0.0F);
            }
            else if (difficulty != Difficulty.PEACEFUL)
            {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }

        boolean naturalRegen = player.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);

        if (naturalRegen && this.saturationLevel > 0.0F && thirst.getHydration() > 0.0F && player.isHurt() && this.foodLevel >= 20 && thirst.getThirst() >= 20)
        {
            ++this.tickTimer;

            if (this.tickTimer >= 10)
            {
                // Average the saturation and hydration
                float replenishAmount = (Math.min(this.saturationLevel, 6.0F) + Math.min(thirst.getHydration(), 6.0F)) / 2.0F;

                player.heal(replenishAmount / 6.0F);
                this.addExhaustion(replenishAmount);
                thirst.addExhaustion(replenishAmount);
                this.tickTimer = 0;
            }
        }
        else if (naturalRegen && this.foodLevel >= 18 && thirst.getThirst() >= 18 && player.isHurt())
        {
            ++this.tickTimer;
            if (this.tickTimer >= 80)
            {
                player.heal(1.0F);
                this.addExhaustion(6.0F);
                thirst.addExhaustion(6.0F);
                this.tickTimer = 0;
            }
        }
        else if (this.foodLevel <= 0)
        {
            ++this.tickTimer;
            if (this.tickTimer >= 80)
            {
                if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL)
                {
                    player.hurt(DamageSource.STARVE, 1.0F);
                }

                this.tickTimer = 0;
            }
        }
        else
        {
            this.tickTimer = 0;
        }
    }
}
