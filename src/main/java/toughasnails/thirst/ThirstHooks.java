package toughasnails.thirst;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.config.ServerConfig;

public class ThirstHooks
{
    /*
     * Hooks called by ASM
     */
    public static void onCauseFoodExhaustion(Player player, float exhaustion)
    {
        if (ServerConfig.enableThirst.get())
        {
            ThirstHelper.getThirst(player).addExhaustion(exhaustion);
        }
    }

    public static void doFoodDataTick(FoodData data, Player player)
    {
        Difficulty difficulty = player.level().getDifficulty();
        IThirst thirst = ThirstHelper.getThirst(player);

        data.lastFoodLevel = data.foodLevel;

        if (data.exhaustionLevel > 4.0F)
        {
            data.exhaustionLevel -= 4.0F;

            if (data.saturationLevel > 0.0F)
            {
                data.saturationLevel = Math.max(data.saturationLevel - 1.0F, 0.0F);
            }
            else if (difficulty != Difficulty.PEACEFUL)
            {
                data.foodLevel = Math.max(data.foodLevel - 1, 0);
            }
        }

        boolean naturalRegen = player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);

        if (naturalRegen && data.saturationLevel > 0.0F && thirst.getHydration() > 0.0F && player.isHurt() && data.foodLevel >= 20 && thirst.getThirst() >= 20)
        {
            ++data.tickTimer;

            if (data.tickTimer >= 10)
            {
                // Average the saturation and hydration
                float replenishAmount = (Math.min(data.saturationLevel, 6.0F) + Math.min(thirst.getHydration(), 6.0F)) / 2.0F;

                player.heal(replenishAmount / 6.0F);
                data.addExhaustion(replenishAmount);
                thirst.addExhaustion(replenishAmount);
                data.tickTimer = 0;
            }
        }
        else if (naturalRegen && data.foodLevel >= 18 && thirst.getThirst() >= 18 && player.isHurt())
        {
            ++data.tickTimer;
            if (data.tickTimer >= 80)
            {
                player.heal(1.0F);
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
                    player.hurt(player.damageSources().starve(), 1.0F);
                }

                data.tickTimer = 0;
            }
        }
        else
        {
            data.tickTimer = 0;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onAiStep(LocalPlayer player)
    {
        if (player.isSprinting())
        {
            boolean sprintingAllowable = canSprintWithThirst(player);

            if (player.isSwimming())
            {
                if (!player.onGround() && !player.input.shiftKeyDown && !sprintingAllowable || !player.isInWater())
                {
                    player.setSprinting(false);
                }
            }
            else if (!sprintingAllowable)
            {
                player.setSprinting(false);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onAiStepSetSprinting(LocalPlayer player, boolean sprinting)
    {
        // Don't allow sprinting if the player has insufficient thirst
        if (sprinting && !canSprintWithThirst(player))
            sprinting = false;

        player.setSprinting(sprinting);
    }

    @OnlyIn(Dist.CLIENT)
    private static boolean canSprintWithThirst(LocalPlayer player)
    {
        return ThirstHelper.getThirst(player).getThirst() > 6 || player.getAbilities().mayfly;
    }
}
