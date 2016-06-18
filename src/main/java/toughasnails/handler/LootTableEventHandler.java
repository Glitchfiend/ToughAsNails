package toughasnails.handler;

import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.item.TANItems;

public class LootTableEventHandler
{
	@SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event)
    {
		if (event.getName().equals(LootTableList.CHESTS_NETHER_BRIDGE))
		{
            LootPool main = event.getTable().getPool("main");
            if (main != null)
            {
                main.addEntry(new LootEntryItem(TANItems.lifeblood_crystal, 1, 0, new LootFunction[0], new LootCondition[0], "toughasnails:lifeblood_crystal"));
            }
		}
		if (event.getName().equals(LootTableList.CHESTS_SPAWN_BONUS_CHEST))
		{
            LootPool main = event.getTable().getPool("main");
            if (main != null)
            {
                main.addEntry(new LootEntryItem(TANItems.canteen, 10, 0, new LootFunction[0], new LootCondition[0], "toughasnails:canteen"));
            }
		}
		if (event.getName().equals(LootTableList.CHESTS_IGLOO_CHEST))
		{
            LootPool main = event.getTable().getPool("main");
            if (main != null)
            {
                main.addEntry(new LootEntryItem(TANItems.ice_cube, 8, 5, new LootFunction[0], new LootCondition[0], "toughasnails:ice_cube"));
                main.addEntry(new LootEntryItem(TANItems.freeze_powder, 4, 3, new LootFunction[0], new LootCondition[0], "toughasnails:freeze_powder"));
                main.addEntry(new LootEntryItem(TANItems.freeze_rod, 2, 1, new LootFunction[0], new LootCondition[0], "toughasnails:freeze_rod"));
            }
		}
    }
}