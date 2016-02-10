package toughasnails.api;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;

public interface ITANBlock {
    
    public Class<? extends ItemBlock> getItemClass();
    public int getItemRenderColor(IBlockState state, int tintIndex);
    public IProperty[] getPresetProperties();
    public IProperty[] getNonRenderingProperties();
    public String getStateName(IBlockState state);
    
}