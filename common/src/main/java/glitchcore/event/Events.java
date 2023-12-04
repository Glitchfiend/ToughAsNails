/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.event;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class Events
{
    public static final Event<IRegistryEventContext<Block>> BLOCK_REGISTRY_EVENT = Event.create();
    public static final Event<IRegistryEventContext<Item>> ITEM_REGISTRY_EVENT = Event.create();
    public static final Event<IRegistryEventContext<MenuType<?>>> MENU_REGISTRY_EVENT = Event.create();
    public static final Event<IRegistryEventContext<BlockEntityType<?>>> BLOCK_ENTITY_REGISTRY_EVENT = Event.create();
    public static final Event<IRegistryEventContext<RecipeSerializer<?>>> RECIPE_SERIALIZER_REGISTRY_EVENT = Event.create();
    public static final Event<IRegistryEventContext<RecipeType<?>>> RECIPE_TYPE_REGISTRY_EVENT = Event.create();

    // TODO: Eliminate Event<> -> Move towards subclasses
}
