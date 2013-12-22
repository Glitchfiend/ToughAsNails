package tan.core;

import static tan.core.TANItems.registerItem;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraftforge.common.EnumHelper;
import tan.ToughAsNails;
import tan.armour.ArmourHeat;
import tan.armour.ArmourWool;
import tan.configuration.TANConfigurationIDs;

public class TANArmour
{
    public static EnumArmorMaterial armourMaterialWool;
    public static EnumArmorMaterial armourMaterialHeat;
    
    public static Item helmetWool;
    public static Item chestplateWool;
    public static Item leggingsWool;
    public static Item bootsWool;
    public static Item helmetHeat;
    public static Item chestplateHeat;
    public static Item leggingsHeat;
    public static Item bootsHeat;
    
    public static void init()
    {
        initializeArmourMaterials();
        initializeItems();
        registerItems();
    }
    
    private static void initializeArmourMaterials()
    {
        armourMaterialWool = EnumHelper.addArmorMaterial("WOOL", 3, new int[]{1, 2, 2, 1}, 4);
        armourMaterialHeat = EnumHelper.addArmorMaterial("HEAT", 8, new int[]{3, 6, 4, 2}, 7);
    }
    
    private static void initializeItems()
    {
        helmetWool = new ArmourWool(TANConfigurationIDs.helmetWoolID, armourMaterialWool, ToughAsNails.proxy.addArmor("wool"), 0).setUnlocalizedName("tan.helmetWool");
        chestplateWool = new ArmourWool(TANConfigurationIDs.chestplateWoolID, armourMaterialWool, ToughAsNails.proxy.addArmor("wool"), 1).setUnlocalizedName("tan.chestplateWool");
        leggingsWool = new ArmourWool(TANConfigurationIDs.leggingsWoolID, armourMaterialWool, ToughAsNails.proxy.addArmor("wool"), 2).setUnlocalizedName("tan.leggingsWool");
        bootsWool = new ArmourWool(TANConfigurationIDs.bootsWoolID, armourMaterialWool, ToughAsNails.proxy.addArmor("wool"), 3).setUnlocalizedName("tan.bootsWool");
        
        helmetHeat = new ArmourHeat(TANConfigurationIDs.helmetHeatID, armourMaterialHeat, ToughAsNails.proxy.addArmor("heat"), 0).setUnlocalizedName("tan.helmetHeat");
        chestplateHeat = new ArmourHeat(TANConfigurationIDs.chestplateHeatID, armourMaterialHeat, ToughAsNails.proxy.addArmor("heat"), 1).setUnlocalizedName("tan.chestplateHeat");
        leggingsHeat = new ArmourHeat(TANConfigurationIDs.leggingsHeatID, armourMaterialHeat, ToughAsNails.proxy.addArmor("heat"), 2).setUnlocalizedName("tan.leggingsHeat");
        bootsHeat = new ArmourHeat(TANConfigurationIDs.bootsHeatID, armourMaterialHeat, ToughAsNails.proxy.addArmor("heat"), 3).setUnlocalizedName("tan.bootsHeat");
    }
    
    private static void registerItems()
    {
        registerItem(helmetWool);
        registerItem(chestplateWool);
        registerItem(leggingsWool);
        registerItem(bootsWool);
        
        registerItem(helmetHeat);
        registerItem(chestplateHeat);
        registerItem(leggingsHeat);
        registerItem(bootsHeat);
    }
}
