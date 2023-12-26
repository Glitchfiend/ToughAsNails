/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin.glitchcore.impl;

import glitchcore.util.RenderTypeHelper;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = RenderTypeHelper.class, remap = false)
public class MixinRenderTypeHelper
{
    @Overwrite
    public static void setRenderType(Block block, RenderType type)
    {
        BlockRenderLayerMap.INSTANCE.putBlock(block, type);
    }
}
