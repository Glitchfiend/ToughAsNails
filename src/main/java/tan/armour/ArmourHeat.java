package tan.armour;

import tan.ToughAsNails;
import tan.core.TANArmour;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ArmourHeat extends ItemArmor
{
    public int textureID = 0;

    public ArmourHeat(int id, EnumArmorMaterial enumArmourMaterial, int renderIndex, int armourType) 
    {
        super(id, enumArmourMaterial, renderIndex, armourType);
        this.textureID = armourType;
        this.setCreativeTab(ToughAsNails.tabToughAsNails);
    }

    @Override
    public boolean getIsRepairable(ItemStack itemToRepair, ItemStack itemToRepairWith)
    {
        return false;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) 
    {
        if (stack.itemID == TANArmour.helmetHeat.itemID || stack.itemID == TANArmour.chestplateHeat.itemID || stack.itemID == TANArmour.bootsHeat.itemID)
            return "toughasnails:textures/armour/heat_layer_1.png";
        if (stack.itemID == TANArmour.leggingsHeat.itemID)
            return "toughasnails:textures/armour/heat_layer_2.png";
        
        return null;
    }

    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        if (textureID == 0) { itemIcon = iconRegister.registerIcon("toughasnails:heat_helmet"); }
        else if (textureID == 1) { itemIcon = iconRegister.registerIcon("toughasnails:heat_chestplate"); }
        else if (textureID == 2) { itemIcon = iconRegister.registerIcon("toughasnails:heat_leggings"); }
        else if (textureID == 3) { itemIcon = iconRegister.registerIcon("toughasnails:heat_boots"); }
    }
}

