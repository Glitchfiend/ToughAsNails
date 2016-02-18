package toughasnails.handler;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ItemStackHolderInjector;
import toughasnails.api.TANItems;
import toughasnails.entities.EntityTANArrow;
import toughasnails.item.ItemTANArrow;

public class ArrowEventHandler
{
	@SubscribeEvent
    public void onArrowNock(ArrowNockEvent event)
    {
		EntityPlayer player = event.entityPlayer;
		ItemStack itemstack = event.result;
		Item item = itemstack.getItem();
		
		if (player.inventory.hasItem(TANItems.arrow))
		{
			player.setItemInUse(itemstack, item.getMaxItemUseDuration(itemstack));
		}
    }
	
	@SubscribeEvent
	public void onPlayerStopUsingItem(PlayerUseItemEvent.Stop event)
	{
		EntityPlayer player = event.entityPlayer;
		World world = player.worldObj;
		ItemStack itemstack = event.item;
		Item item = itemstack.getItem();
		int duration = event.duration;
		
		if (itemstack.getItem() == Items.bow)
		{
	        boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemstack) > 0;
	
	        if (flag || player.inventory.hasItem(TANItems.arrow))
	        {
	            int i = item.getMaxItemUseDuration(itemstack) - duration;
	            net.minecraftforge.event.entity.player.ArrowLooseEvent looseevent = new net.minecraftforge.event.entity.player.ArrowLooseEvent(player, itemstack, i);
	            if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(looseevent)) return;
	            i = looseevent.charge;
	            float f = (float)i / 20.0F;
	            f = (f * f + f * 2.0F) / 3.0F;
	
	            if ((double)f < 0.1D)
	            {
	                return;
	            }
	
	            if (f > 1.0F)
	            {
	                f = 1.0F;
	            }
	
	            EntityTANArrow entitytanarrow = new EntityTANArrow(world, player, f * 2.0F);
	
	            if (f == 1.0F)
	            {
	                entitytanarrow.setIsCritical(true);
	            }
	
	            itemstack.damageItem(1, player);
	            world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (world.rand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
	
	            if (flag)
	            {
	                entitytanarrow.canBePickedUp = 2;
	            }
	            else
	            {
	                player.inventory.consumeInventoryItem(TANItems.arrow);
	            }
	
	            //player.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
	
	            if (!world.isRemote)
	            {
	                world.spawnEntityInWorld(entitytanarrow);
	            }
	            
	            event.setCanceled(true);
	        }
		}
	}
}