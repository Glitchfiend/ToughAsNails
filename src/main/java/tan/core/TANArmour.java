package tan.core;

import static tan.core.TANItems.registerItem;
import tan.ToughAsNails;
import tan.armour.ArmourWool;
import tan.configuration.TANConfigurationIDs;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraftforge.common.EnumHelper;

public class TANArmour
{
    public static EnumArmorMaterial armourMaterialWool;
    
    public static Item helmetWool;
    public static Item chestplateWool;
    public static Item leggingsWool;
    public static Item bootsWool;
    
    public static void init()
    {
        initializeArmourMaterials();
        initializeItems();
        registerItems();
    }
    
    private static void initializeArmourMaterials()
    {
        armourMaterialWool = EnumHelper.addArmorMaterial("WOOL", 3, new int[]{1, 2, 2, 1}, 4);
    }
    
    private static void initializeItems()
    {
        helmetWool = new ArmourWool(TANConfigurationIDs.helmetWoolID, armourMaterialWool, ToughAsNails.proxy.addArmor("wool"), 0).setUnlocalizedName("tan.helmetWool");
        chestplateWool = new ArmourWool(TANConfigurationIDs.chestplateWoolID, armourMaterialWool, ToughAsNails.proxy.addArmor("wool"), 1).setUnlocalizedName("tan.chestplateWool");
        leggingsWool = new ArmourWool(TANConfigurationIDs.leggingsWoolID, armourMaterialWool, ToughAsNails.proxy.addArmor("wool"), 2).setUnlocalizedName("tan.leggingsWool");
        bootsWool = new ArmourWool(TANConfigurationIDs.bootsWoolID, armourMaterialWool, ToughAsNails.proxy.addArmor("wool"), 3).setUnlocalizedName("tan.bootsWool");
    }
    
    private static void registerItems()
    {
        registerItem(helmetWool);
        registerItem(chestplateWool);
        registerItem(leggingsWool);
        registerItem(bootsWool);
    }
}
