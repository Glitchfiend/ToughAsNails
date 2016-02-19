package toughasnails.init;

import net.minecraft.block.BlockDispenser;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import toughasnails.api.TANItems;
import toughasnails.entities.projectile.DispenserBehaviorTANArrow;
import toughasnails.item.ItemTANArrow;

public class ModVanillaCompat
{
    public static void init()
    {
    	registerDispenserBehaviors();
    	addDungeonLoot();
    }
    
    private static void registerDispenserBehaviors()
    {
    	BlockDispenser.dispenseBehaviorRegistry.putObject(TANItems.arrow, new DispenserBehaviorTANArrow());
    }
    
	private static void addDungeonLoot()
	{
		ChestGenHooks desertTemple = ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST);
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
		village.addItem(new WeightedRandomChestContent(new ItemStack(TANItems.wool_leggings), 1, 1, 5));
		
		dungeon.addItem(new WeightedRandomChestContent(new ItemStack(TANItems.arrow, 1, ItemTANArrow.ArrowType.FIRE_ARROW.ordinal()), 1, 4, 5));
		dungeon.addItem(new WeightedRandomChestContent(new ItemStack(TANItems.arrow, 1, ItemTANArrow.ArrowType.ICE_ARROW.ordinal()), 1, 4, 7));
		dungeon.addItem(new WeightedRandomChestContent(new ItemStack(TANItems.arrow, 1, ItemTANArrow.ArrowType.BOMB_ARROW.ordinal()), 1, 4, 3));
		dungeon.addItem(new WeightedRandomChestContent(new ItemStack(TANItems.arrow, 1, ItemTANArrow.ArrowType.LIGHTNING_ARROW.ordinal()), 1, 4, 1));
	}
}
