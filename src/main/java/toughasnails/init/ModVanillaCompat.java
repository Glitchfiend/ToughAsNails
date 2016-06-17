package toughasnails.init;

public class ModVanillaCompat
{
    public static void init()
    {
    	addDungeonLoot();
    }
    
	private static void addDungeonLoot()
	{
	    //Requires loot hooks which haven't yet been added
		/*TODO: 1.9 ChestGenHooks desertTemple = ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST);
		ChestGenHooks dungeon = ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST);
		ChestGenHooks jungleTemple = ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST);
		ChestGenHooks mineshaft = ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR);
		ChestGenHooks strongholdCorridor = ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR);
		ChestGenHooks strongholdCrossing = ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING);
		ChestGenHooks strongholdLibrary = ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY);
		ChestGenHooks village = ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH);
		ChestGenHooks bonusChest = ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST);

		bonusChest.addItem(new WeightedRandomChestContent(new ItemStack(TANItems.canteen), 1, 1, 15));
		
		mineshaft.addItem(new WeightedRandomChestContent(new ItemStack(TANItems.jelled_slime_boots), 1, 1, 5));
		mineshaft.addItem(new WeightedRandomChestContent(new ItemStack(TANItems.jelled_slime_chestplate), 1, 1, 5));
		mineshaft.addItem(new WeightedRandomChestContent(new ItemStack(TANItems.jelled_slime_helmet), 1, 1, 5));
		mineshaft.addItem(new WeightedRandomChestContent(new ItemStack(TANItems.jelled_slime_leggings), 1, 1, 5));
		mineshaft.addItem(new WeightedRandomChestContent(new ItemStack(TANItems.respirator), 1, 1, 1));
		mineshaft.addItem(new WeightedRandomChestContent(new ItemStack(TANItems.air_filter), 2, 4, 5));
		
		village.addItem(new WeightedRandomChestContent(new ItemStack(TANItems.wool_boots), 1, 1, 5));
		village.addItem(new WeightedRandomChestContent(new ItemStack(TANItems.wool_helmet), 1, 1, 5));
		village.addItem(new WeightedRandomChestContent(new ItemStack(TANItems.wool_chestplate), 1, 1, 5));
		village.addItem(new WeightedRandomChestContent(new ItemStack(TANItems.wool_leggings), 1, 1, 5));*/
	}
}
