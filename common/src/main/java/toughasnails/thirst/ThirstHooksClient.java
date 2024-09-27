/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.thirst;

import net.minecraft.client.player.LocalPlayer;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.init.ModConfig;

public class ThirstHooksClient
{
    public static void onAiStepSetSprinting(LocalPlayer player, boolean sprinting)
    {
        // Don't allow sprinting if the player has insufficient thirst
        if (sprinting && !canSprintWithThirst(player))
            sprinting = false;

        player.setSprinting(sprinting);
    }

    private static boolean canSprintWithThirst(LocalPlayer player)
    {
        return !ModConfig.thirst.thirstPreventSprint || ThirstHelper.getThirst(player).getThirst() > 6 || player.getAbilities().mayfly;
    }
}
