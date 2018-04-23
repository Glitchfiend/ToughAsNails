package toughasnails.handler;

import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.item.TANItems;
import toughasnails.init.ModConfig;

public class LootTableEventHandler
{
	@SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event)
    {
		if (event.getName().equals(LootTableList.CHESTS_SPAWN_BONUS_CHEST))
		{
            LootPool main = event.getTable().getPool("main");
            if (main != null)
            {
                main.addEntry(new LootEntryItem(TANItems.canteen, 10, 0, new LootFunction[0], new LootCondition[0], "toughasnails:canteen"));
            }
		}
		
		if (ModConfig.gameplay.lootTableTweaks)
		{
			if (event.getName().equals(LootTableList.CHESTS_VILLAGE_BLACKSMITH))
			{
				LootPool main = event.getTable().getPool("main");
	            if (main != null)
	            {
	                main.removeEntry("minecraft:diamond");
	                main.removeEntry("minecraft:obsidian");
	                main.removeEntry("minecraft:diamond_horse_armor");
	                main.removeEntry("minecraft:golden_horse_armor");
	                main.removeEntry("minecraft:iron_horse_armor");
	                main.removeEntry("minecraft:iron_pickaxe");
	                main.removeEntry("minecraft:iron_axe");
	                main.removeEntry("minecraft:iron_sword");
	                main.removeEntry("minecraft:iron_boots");
	                main.removeEntry("minecraft:iron_chestplate");
	                main.removeEntry("minecraft:iron_helmet");
	                main.removeEntry("minecraft:iron_leggings");
	            }
			}
		}
    }
}