package toughasnails.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.FoliageColor;
import toughasnails.api.block.TANBlocks;
import toughasnails.api.item.TANItems;
import toughasnails.item.DyeableWoolItem;
import toughasnails.item.LeafArmorItem;

import static toughasnails.api.block.TANBlocks.RAIN_COLLECTOR;
import static toughasnails.api.block.TANBlocks.WATER_PURIFIER;

public class ClientProxy extends CommonProxy
{
    public ClientProxy()
    {

    }

    @Override
    public void init()
    {
        RenderType transparentRenderType = RenderType.cutoutMipped();
        RenderType cutoutRenderType = RenderType.cutout();
        RenderType translucentRenderType = RenderType.translucent();

        ItemBlockRenderTypes.setRenderLayer(RAIN_COLLECTOR.get(), cutoutRenderType);
        ItemBlockRenderTypes.setRenderLayer(WATER_PURIFIER.get(), cutoutRenderType);
    }
}