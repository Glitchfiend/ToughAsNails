package sereneseasons.api;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;

public interface ISSBlock {
    
    public Class<? extends ItemBlock> getItemClass();
    public IProperty[] getPresetProperties();
    public IProperty[] getNonRenderingProperties();
    public String getStateName(IBlockState state);
    
}