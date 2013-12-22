package tan.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import tan.ToughAsNails;

public class ItemTANMiscItems extends Item
{
	private static String[] items = {"charcoalfilter", "hardenedslime"};
	@SideOnly(Side.CLIENT)
	private Icon[] textures;
	
    public ItemTANMiscItems(int id)
    {
    	super(id);
    	setMaxDamage(0);
    	setHasSubtypes(true);
        this.setCreativeTab(ToughAsNails.tabToughAsNails);
    }

	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		textures = new Icon[items.length];

		for (int i = 0; i < items.length; ++i) {
			textures[i] = iconRegister.registerIcon("toughasnails:"+items[i]);
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		int meta = itemStack.getItemDamage();
		if (meta < 0 || meta >= items.length) {
			meta = 0;
		}

		return super.getUnlocalizedName() + "." + items[meta];
	}

	@Override
	public Icon getIconFromDamage(int meta)
	{
		if (meta < 0 || meta >= textures.length) {
			meta = 0;
		}

		return textures[meta];
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void getSubItems(int itemId, CreativeTabs creativeTab, List subTypes)
	{
		for(int meta = 0; meta < items.length; ++meta) {
			subTypes.add(new ItemStack(itemId, 1, meta));
		}
	}
}
