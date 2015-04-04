package toughasnails.init;

import toughasnails.api.TANItems;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraftforge.client.model.ModelLoader;

public class ModModels
{
    public static final ModelResourceLocation CANTEEN = new ModelResourceLocation("toughasnails:canteen", "inventory");
    public static final ModelResourceLocation CANTEEN_EMPTY = new ModelResourceLocation("toughasnails:canteen_empty", "inventory");
    public static final ModelResourceLocation CANTEEN_FILLED = new ModelResourceLocation("toughasnails:canteen_filled", "inventory");
    
    public static void init()
    {
        ModelBakery.addVariantName(TANItems.canteen, "toughasnails:canteen_empty", "toughasnails:canteen_filled");
        ModelLoader.setCustomModelResourceLocation(TANItems.canteen, 0, CANTEEN);
    }
}
