package tan.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tan.ToughAsNails;

public class ItemTANCanteen extends Item
{
    public ItemTANCanteen(int id)
    {
        super(id);
        this.maxStackSize = 1;
        this.setMaxDamage(4);
        this.setCreativeTab(ToughAsNails.tabToughAsNails);
    }
    
    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
    	return EnumAction.drink;
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {	
        return 48;
    }

    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon("toughasnails:canteenfull");
    }
}
