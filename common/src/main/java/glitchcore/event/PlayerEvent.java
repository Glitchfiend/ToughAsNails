/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.event;

import net.minecraft.world.entity.player.Player;

public class PlayerEvent extends Event
{
    protected final Player player;

    public PlayerEvent(Player player)
    {
        this.player = player;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public static class ChangeDimension extends PlayerEvent
    {
        public ChangeDimension(Player player)
        {
            super(player);
        }
    }
}
