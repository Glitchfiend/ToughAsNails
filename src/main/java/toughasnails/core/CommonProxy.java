package toughasnails.core;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class CommonProxy
{
    public void registerRenderers() {}
    public void registerItemVariantModel(Item item, String name, int metadata) {}
    public void registerNonRenderingProperties(Block block) {}
    public void registerFluidBlockRendering(Block block, String name) {}
}