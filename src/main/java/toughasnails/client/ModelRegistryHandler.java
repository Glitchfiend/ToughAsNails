package toughasnails.client;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

@EventBusSubscriber @SuppressWarnings("unchecked")
public class ModelRegistryHandler {
	
	//I really don't like this, but Its working, and to do it otherwise would require a bunch of other changes.
	
	private static final ArrayList<?>[] MODELS_SET = {new ArrayList<IForgeRegistryEntry<?>>(), new ArrayList<Integer>(), new ArrayList<ModelResourceLocation>()};
	
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event){
		for(int i = 0; i < MODELS_SET[0].size(); i++){
			IForgeRegistryEntry<?> k = ((ArrayList<IForgeRegistryEntry<?>>)MODELS_SET[0]).get(i);
			if(k instanceof Block)ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock((Block) k), (Integer) MODELS_SET[1].get(i), (ModelResourceLocation) MODELS_SET[2].get(i));
			else if(k instanceof Item)ModelLoader.setCustomModelResourceLocation((Item) k, (Integer) MODELS_SET[1].get(i), (ModelResourceLocation) MODELS_SET[2].get(i));
		}
		MODELS_SET[0].clear();
		MODELS_SET[1].clear();
		MODELS_SET[2].clear();
	}
	
	public static void queueForModelRegistry(Item item, int meta, ModelResourceLocation loc){
		((ArrayList<IForgeRegistryEntry<?>>)MODELS_SET[0]).add(item);
		((ArrayList<Integer>)MODELS_SET[1]).add(meta);
		((ArrayList<ModelResourceLocation>)MODELS_SET[2]).add(loc);
	}
	
	public static void queueForModelRegistry(Block block, int meta, ModelResourceLocation loc){
		((ArrayList<IForgeRegistryEntry<?>>)MODELS_SET[0]).add(block);
		((ArrayList<Integer>)MODELS_SET[1]).add(meta);
		((ArrayList<ModelResourceLocation>)MODELS_SET[2]).add(loc);
	}
	
	

}
