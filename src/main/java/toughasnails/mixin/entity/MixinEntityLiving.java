/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.mixin.entity;

import java.util.Iterator;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Sets;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving
{
    @Inject(method = "<init>(Lnet/minecraft/world/World;)V", at = @At("RETURN"))
    public void onInit(World world, CallbackInfo ci)
    {
        if (world != null && !world.isRemote)
        {
            reprioritiseTasks();
        }
    }
    
    private void reprioritiseTasks()
    {
        if ((Object)this instanceof EntityAnimal)
        {
            EntityAnimal animal = (EntityAnimal)(Object)this;
            int lowestTaskPriority = getLowestTaskPriority(animal);
            int nextTemptPriority = 0;
            
            //Adjust priorities for tempt tasks
            for (int i = 0; i <= lowestTaskPriority; i++)
            {
                boolean temptTaskInPriority = false;
                
                for (EntityAITaskEntry entry : animal.tasks.taskEntries)
                {
                    if (entry.action instanceof EntityAITempt)
                    {
                        entry.priority = nextTemptPriority;
                    }
                }
                
                if (temptTaskInPriority) nextTemptPriority++;
            }
            
            //Avoid task should have a priority one above tempt tasks
            nextTemptPriority++;
            //For reference, Ocelots have it as new EntityAIAvoidEntity(this, EntityPlayer.class, 16.0F, 0.8D, 1.33D);
            double movementSpeed = animal.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
            animal.tasks.addTask(nextTemptPriority, new EntityAIAvoidEntity(animal, EntityPlayer.class, 40.0F, 0.8D, 1.33D));
            nextTemptPriority++;
            
            for (EntityAITaskEntry entry : animal.tasks.taskEntries)
            {
                if (entry.action instanceof EntityAIWander)
                {
                    entry.action.setMutexBits(0);
                }
                
                if (!(entry.action instanceof EntityAITempt) && !(entry.action instanceof EntityAIAvoidEntity))
                    entry.priority += nextTemptPriority;
            }
        }
    }
    
    private int getLowestTaskPriority(EntityLiving entity)
    {
        int lowestPriority = 0;
        
        for (EntityAITaskEntry entry : entity.tasks.taskEntries)
        {
            if (entry.priority > lowestPriority)
            {
                lowestPriority = entry.priority;
            }
        }
        
        return lowestPriority;
    }
}
