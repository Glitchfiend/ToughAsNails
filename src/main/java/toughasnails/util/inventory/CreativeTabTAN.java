package toughasnails.util.inventory;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.item.TANItems;
import toughasnails.core.ToughAsNails;

public class CreativeTabTAN extends CreativeTabs
{
    public static final CreativeTabs instance = new CreativeTabTAN(CreativeTabs.getNextID(), "tabToughAsNails");

    private CreativeTabTAN(int index, String label)
    {
        super(index, label);
    }

    @Override
    public ItemStack getTabIconItem()
    {
        return new ItemStack(TANItems.tan_icon);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void displayAllRelevantItems(NonNullList<ItemStack> itemList)
    {
        super.displayAllRelevantItems(itemList);
        
        for (Fluid bucketFluid : FluidRegistry.getBucketFluids())
        {
            if (bucketFluid.getBlock() != null && bucketFluid.getBlock().getRegistryName().getResourceDomain().equals(ToughAsNails.MOD_ID))
            {
                ItemStack itemstack = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, bucketFluid);
                itemList.add(itemstack);
            }
        }
        
        for (EntityList.EntityEggInfo eggInfo : EntityList.ENTITY_EGGS.values())
        {
            if (eggInfo.spawnedID.getResourceDomain().equals(ToughAsNails.MOD_ID))
            {
                ItemStack itemstack = new ItemStack(Items.SPAWN_EGG, 1);
                ItemMonsterPlacer.applyEntityIdToItemStack(itemstack, eggInfo.spawnedID);
                itemList.add(itemstack);
            }
        }
    }
}
