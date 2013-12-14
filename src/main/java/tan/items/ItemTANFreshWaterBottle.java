package tan.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tan.ToughAsNails;

public class ItemTANFreshWaterBottle extends ItemFood
{
    public ItemTANFreshWaterBottle(int id)
    {
        super(id, 0, 0.0F, false);
        this.maxStackSize = 1;
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
        return 32;
    }
    
	@Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer player)
    {
		--itemstack.stackSize;
		
        world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        this.onFoodEaten(itemstack, world, player);

		if (!player.inventory.addItemStackToInventory(new ItemStack(Item.glassBottle)))
		{
            player.dropPlayerItem(new ItemStack(Item.glassBottle.itemID, 1, 0));
		}
        
        return itemstack;
    }

    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon("toughasnails:freshwaterbottle");
    }
}
