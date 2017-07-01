package toughasnails.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class RegistryEventHandler {
	
	public static final List<Potion> POTIONS = new ArrayList<Potion>();
	public static final List<PotionType> POTION_TYPES = new ArrayList<PotionType>();
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
    @SubscribeEvent
    public static void onBlockRegister(Register<Block> event){
    	event.getRegistry().registerAll(BLOCKS.toArray(new Block[0]));
    	BLOCKS.clear();
    }
    
    @SubscribeEvent
    public static void onItemRegister(Register<Item> event){
    	event.getRegistry().registerAll(ITEMS.toArray(new Item[0]));
    	ITEMS.clear();
    }
	
    @SubscribeEvent
    public static void onPotionRegister(Register<Potion> event){
    	event.getRegistry().registerAll(POTIONS.toArray(new Potion[0]));
    	POTIONS.clear();
    }
    
    @SubscribeEvent
    public static void onPotionTypeRegister(Register<PotionType> event){
    	event.getRegistry().registerAll(POTION_TYPES.toArray(new PotionType[0]));
    	POTION_TYPES.clear();
    }
    
    @SubscribeEvent
    public static void onRecipeRegister(Register<IRecipe> event){
    	ModCrafting.init();
    }

}
