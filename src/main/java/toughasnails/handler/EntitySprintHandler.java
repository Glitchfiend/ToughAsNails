/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntitySprintHandler 
{
    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event)
    {
        if (event.getEntity() instanceof EntityAnimal)
        {
            EntityAnimal animal = (EntityAnimal)event.getEntity();
            double speed = animal.getMoveHelper().getSpeed();

            if (speed == 1.33D)
            {
                animal.setSprinting(true);
            }
            else
            {
                animal.setSprinting(false);
            }
        }
    }
}
