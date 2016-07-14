/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class AchievementEventHandler 
{
    @SubscribeEvent
    public void onItemPickup(PlayerEvent.ItemPickupEvent event)
    {
        ItemStack stack = event.pickedUp.getEntityItem();
        Item item = stack.getItem();
        
        Block block = Block.getBlockFromItem(item);
        IBlockState state = block != null ? block.getStateFromMeta(stack.getItemDamage()) : null;
        EntityPlayer player = event.player;

        /*if (block != null && block instanceof BlockBOPLog)
        {
            player.addStat(AchievementList.MINE_WOOD);
        }*/
    }
    
    @SubscribeEvent
    public void onItemUsed(PlayerInteractEvent event)
    {
        /* TODO: 1.9 if (event.action != Action.LEFT_CLICK_BLOCK)
        {
            ItemStack stack = event.entityPlayer.getHeldItem();
            Item item = stack != null ? stack.getItem() : null;
            EntityPlayer player = event.entityPlayer;

            //Gone Home
            if (item == BOPItems.enderporter)
            {
                player.addStat(BOPAchievements.use_enderporter);
            }
        }*/
    }
    
    @SubscribeEvent
    public void onItemUsed(LivingEntityUseItemEvent.Finish event)
    {
        ItemStack stack = event.getItem();
        Item item = stack.getItem();

        if (event.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)event.getEntityLiving();

            //Trippin'
            /*if (item == BOPItems.shroompowder) {
                player.addStat(BOPAchievements.eat_shroom_powder);
            }*/
        }
    }
    
    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.PlaceEvent event)
    {
        ItemStack stack = event.getItemInHand();
        
        //Blocks can be placed by things other than players
        if (stack != null)
        {
            Item item = stack.getItem();
            Block block = Block.getBlockFromItem(item);
            IBlockState state = block != null ? block.getStateFromMeta(stack.getItemDamage()) : null;

            try
            {
                //Yggdrasil
                /*if (state == BlockBOPSapling.paging.getVariantState(BOPTrees.SACRED_OAK))
                {
                    event.getPlayer().addStat(BOPAchievements.grow_sacred_oak);
                }*/
            }
            catch(Exception e) {} //Fail quietly if there's a problem matching metadata to a block state
        }
    }
    
    @SubscribeEvent
    public void onItemCrafted(ItemCraftedEvent event)
    {
        Item item = event.crafting.getItem();
        EntityPlayer player = event.player;
        
        //Nectar of the Gods Achievement
        /*if (item != null && item == BOPItems.ambrosia)
        {
            player.addStat(BOPAchievements.craft_ambrosia);
        }*/
        
    }

    @SubscribeEvent
    public void onPlayerUpdate(LivingUpdateEvent event)
    {
        /* TODO: 1.9 if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer)
        {
            EntityPlayerMP player = (EntityPlayerMP)event.entity;

            //Check every five seconds if the player has entered a new biome, if they haven't already gotten the achievement
            if (player.ticksExisted % 20 * 5 == 0)
            {
                if (!player.getStatFile().hasAchievementUnlocked(BOPAchievements.use_biome_finder))
                {
                    this.updateBiomeRadarExplore(player);
                }
                
                if (!player.getStatFile().hasAchievementUnlocked(BOPAchievements.explore_all_biomes))
                {
                    this.updateBiomesExplored(player);
                }
            }
        }*/
    }
}
