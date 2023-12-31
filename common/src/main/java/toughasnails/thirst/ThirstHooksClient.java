/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.thirst;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import toughasnails.api.thirst.ThirstHelper;

public class ThirstHooksClient {
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

    public static void onAiStepSetSprinting(LocalPlayer player, boolean sprinting)
    {
        // Don't allow sprinting if the player has insufficient thirst
        if (sprinting && !canSprintWithThirst(player))
            sprinting = false;

        player.setSprinting(sprinting);
    }

    private static boolean canSprintWithThirst(LocalPlayer player)
    {
        return ThirstHelper.getThirst(player).getThirst() > 6 || player.getAbilities().mayfly;
    }
}
