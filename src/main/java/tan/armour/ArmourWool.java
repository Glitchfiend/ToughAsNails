package tan.armour;

import tan.ToughAsNails;
import tan.core.TANArmour;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ArmourWool extends ItemArmor
{
    public int textureID = 0;

    public ArmourWool(int id, EnumArmorMaterial enumArmourMaterial, int renderIndex, int armourType) 
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
        if (stack.itemID == TANArmour.helmetWool.itemID || stack.itemID == TANArmour.chestplateWool.itemID || stack.itemID == TANArmour.bootsWool.itemID)
            return "toughasnails:textures/armour/cloth_layer_1.png";
        if (stack.itemID == TANArmour.leggingsWool.itemID)
            return "toughasnails:textures/armour/cloth_layer_2.png";
        
        return null;
    }

    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        if (textureID == 0) { itemIcon = iconRegister.registerIcon("toughasnails:cloth_helmet"); }
        else if (textureID == 1) { itemIcon = iconRegister.registerIcon("toughasnails:cloth_chestplate"); }
        else if (textureID == 2) { itemIcon = iconRegister.registerIcon("toughasnails:cloth_leggings"); }
        else if (textureID == 3) { itemIcon = iconRegister.registerIcon("toughasnails:cloth_boots"); }
    }
}

