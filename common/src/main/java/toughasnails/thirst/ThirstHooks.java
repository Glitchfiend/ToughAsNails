package toughasnails.thirst;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
import toughasnails.api.damagesource.TANDamageTypes;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.core.ToughAsNails;
import toughasnails.init.ModConfig;

public class ThirstHooks
{
    /*
     * Hooks called by ASM
     */
    public static void onCauseFoodExhaustion(Player player, float exhaustion)
    {
        if (ModConfig.thirst.enableThirst)
        {
            ThirstHelper.getThirst(player).addExhaustion(exhaustion);
        }
    }

    public static void doFoodDataTick(FoodData data, Player player)
    {
        Difficulty difficulty = player.level().getDifficulty();
        IThirst thirst = ThirstHelper.getThirst(player);

        data.lastFoodLevel = data.foodLevel;

        boolean naturalRegen = player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);

        if (naturalRegen && data.saturationLevel > 0.0F && player.isHurt() && data.foodLevel >= 20 && (!ModConfig.thirst.thirstPreventHealthRegen || (thirst.getHydration() > 0.0F && thirst.getThirst() >= 20)))
        {
            ++data.tickTimer;

            if (data.tickTimer >= 10)
            {
                // Derive the replenish amount only from hunger to cause less confusion
                float replenishAmount = Math.min(data.saturationLevel, 6.0F);

                player.heal(replenishAmount / 6.0F);

                // Add exhaustion to thirst regardless of whether the thirst health regen option is enabled/disabled
                data.addExhaustion(replenishAmount);
                thirst.addExhaustion(replenishAmount);
                data.tickTimer = 0;
            }
        }
        else if (naturalRegen && data.foodLevel >= 18 && (!ModConfig.thirst.thirstPreventHealthRegen || thirst.getThirst() >= 18) && player.isHurt())
        {
            ++data.tickTimer;
            if (data.tickTimer >= 80)
            {
                player.heal(1.0F);

                // Add exhaustion to thirst regardless of whether the thirst health regen option is enabled/disabled
                data.addExhaustion(6.0F);
                thirst.addExhaustion(6.0F);
                data.tickTimer = 0;
            }
        }
        else if (data.foodLevel <= 0)
        {
            ++data.tickTimer;
            if (data.tickTimer >= 80)
            {
                if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL)
                {
                    player.hurt(player.damageSources().source(TANDamageTypes.THIRST), 1.0F);
                }

                data.tickTimer = 0;
            }
        }
        else
        {
            data.tickTimer = 0;
        }
    }

}
