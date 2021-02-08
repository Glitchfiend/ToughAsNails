package toughasnails.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.entity.player.PlayerEntity;
import toughasnails.api.block.TANBlocks;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;

public class ClientProxy extends CommonProxy
{
    public ClientProxy()
    {

    }

    @Override
    public void init()
    {
        BlockColors blockColors = Minecraft.getInstance().getBlockColors();

        blockColors.register((state, world, pos, tintIndex) -> 0x47DAFF, TANBlocks.RAIN_COLLECTOR);
    }
}