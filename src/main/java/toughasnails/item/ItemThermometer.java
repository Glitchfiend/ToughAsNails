/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.item;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.TANCapabilities;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureScale;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureHandler;

public class ItemThermometer extends Item
{
    public ItemThermometer()
    {
        this.addPropertyOverride(new ResourceLocation("temperature"), new IItemPropertyGetter()
        {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn)
            {
                if (entityIn == null && !stack.isOnItemFrame())
                    return TemperatureScale.getScaleMidpoint();
                    
                Entity entity = entityIn;
                if (stack.isOnItemFrame())
                    entity = stack.getItemFrame();

                World world = worldIn;
                if (world == null)
                    world = entity.world;

                return (float) TemperatureHelper.getTargetAtPos(world, entity.getPosition(), null).getRawValue() / (float) TemperatureScale.getScaleTotal();
            }
        });
    }
    
    Map<UUID, Long> messageDebounce = new HashMap<UUID, Long>();
        
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
		if (world.isRemote)
		{
			
			// Prevent spam use of this item.
			if (!messageDebounce.containsKey(player.getUniqueID()) || (System.currentTimeMillis() - messageDebounce.get(player.getUniqueID()) > 2000))
			{
				
				// Allow the player to toggle the detailed temperature info menu.
				// TODO: add configuration for allowing this.
				// TODO: resume testing here.
				if (true)
				{			        
			        Minecraft.getMinecraft().addScheduledTask(() ->
	                {
	                    TemperatureHandler temperatureStats = (TemperatureHandler) player.getCapability(TANCapabilities.TEMPERATURE, null);
	                    TemperatureDebugger debugger = temperatureStats.debugger;

	                    debugger.setGuiVisible(!debugger.isGuiVisible());
	                });
				}
				
				//BlockPos playerPosition = player.getPosition();
				//int finalTemperature = TemperatureHandler
				//		.getTargetTemperatureAt(world, playerPosition);

				//player.addChatMessage(new TextComponentTranslation(
				//		"item.thermometer.read", finalTemperature));
				//messageDebounce.put(player.getUniqueID(),
				//		System.currentTimeMillis());
			}
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
	}
}
