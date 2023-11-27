/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.event;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class Events
{
    public static final Event<IRegistryEventContext<Block>> BLOCK_REGISTRY_EVENT = Event.create();
    public static final Event<IRegistryEventContext<Item>> ITEM_REGISTRY_EVENT = Event.create();
}
