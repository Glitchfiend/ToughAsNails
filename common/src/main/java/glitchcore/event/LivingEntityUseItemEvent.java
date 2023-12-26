/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public abstract class LivingEntityUseItemEvent extends Event
{
    private final LivingEntity entity;
    private final ItemStack item;

    public LivingEntityUseItemEvent(LivingEntity entity, ItemStack item)
    {
        this.entity = entity;
        this.item = item;
    }

    public LivingEntity getEntity()
    {
        return this.entity;
    }

    public ItemStack getItem()
    {
        return this.item;
    }

    public static class Finish extends LivingEntityUseItemEvent
    {
        private ItemStack result;

        public Finish(LivingEntity entity, ItemStack item, ItemStack result)
        {
            super(entity, item);
            this.result = result;
        }

        public void setResult(ItemStack result)
        {
            this.result = result;
        }

        public ItemStack getResult()
        {
            return this.result;
        }
    }
}
