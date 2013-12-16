package tan.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import tan.ToughAsNails;
import tan.api.utils.TANPlayerStatUtils;
import tan.stats.ThirstStat;

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
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitVecX, float hitVecY, float hitVecZ)
    {
        return false;
    }
    
    @Override
    public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player)
    {
        if (!player.capabilities.isCreativeMode)
        {
            itemStack.damageItem(1, player);
        }

        if (!world.isRemote)
        {
            ThirstStat thirstStat = TANPlayerStatUtils.getPlayerStat(player, ThirstStat.class);
            
            thirstStat.addThirst(5);
        }

        return itemStack;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        MovingObjectPosition pos = this.getMovingObjectPositionFromPlayer(world, player, true);
        
        if (getType(itemStack).isEmpty())
        {
            if (pos == null)
            {
                return itemStack;
            }
            else
            {
                int x = pos.blockX;
                int y = pos.blockY;
                int z = pos.blockZ;

                if (world.getBlockId(x, y, z) == Block.waterStill.blockID)
                {
                    ArrayList<BiomeDictionary.Type> typeList = new ArrayList<BiomeDictionary.Type>();

                    BiomeGenBase biome = world.getBiomeGenForCoords(x, z);

                    for (BiomeDictionary.Type type : BiomeDictionary.getTypesForBiome(biome))
                    {
                        typeList.add(type);
                    }

                    if (biome.biomeName.toLowerCase().contains("ocean") || typeList.contains(BiomeDictionary.Type.BEACH))
                    {
                        setType(itemStack, "Salt Water");
                    }
                    else if (typeList.contains(BiomeDictionary.Type.SWAMP) || typeList.contains(BiomeDictionary.Type.WASTELAND))
                    {
                        setType(itemStack, "Dirty Water");
                    }
                    else
                    {
                        setType(itemStack, "Impure Water");
                    }

                    world.setBlockToAir(x, y, z);
                }
            }
        }
        else
        {
            if (itemStack.getItemDamage() == itemStack.getMaxDamage())
            {
                setType(itemStack, "");
                itemStack.setItemDamage(0);
            }
            else
            {
                player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
            }
        }

        return itemStack;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List stringList, boolean showAdvancedInfo) 
    {
        String type = getType(itemStack);
        
        if (!type.isEmpty())
        {
            stringList.add(type);
        }
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
    
    public void setType(ItemStack stack, String type)
    {
        if (stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
        
        NBTTagCompound stackCompound = stack.getTagCompound();
        
        if (stackCompound.hasKey("ToughAsNails")) stackCompound.setCompoundTag("ToughAsNails", new NBTTagCompound());
        
        stack.getTagCompound().getCompoundTag("ToughAsNails").setString("type", type);
    }

    public String getType(ItemStack stack)
    {
        if (stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
        
        NBTTagCompound stackCompound = stack.getTagCompound();
        
        if (!stackCompound.hasKey("ToughAsNails")) stackCompound.setCompoundTag("ToughAsNails", new NBTTagCompound());
        
        return stack.getTagCompound().getCompoundTag("ToughAsNails").getString("type");
    }
}
