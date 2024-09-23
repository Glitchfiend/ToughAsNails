/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.fabric.mixin;

import toughasnails.glitch.util.RenderHelper;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = RenderHelper.class, remap = false)
public class MixinRenderHelper
{
  @Overwrite
  public static void setRenderType(Block block, RenderType type)
  {
    BlockRenderLayerMap.INSTANCE.putBlock(block, type);
  }

  @Overwrite
  public static void setRenderType(Fluid fluid, RenderType type)
  {
    BlockRenderLayerMap.INSTANCE.putFluid(fluid, type);
  }

  @Overwrite
  public static <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> blockEntityRendererProvider)
  {
    BlockEntityRenderers.register(blockEntityType, blockEntityRendererProvider);
  }

  @Overwrite
  public static <T extends Entity> void registerEntityRenderer(EntityType<? extends T> entityType, EntityRendererProvider<T> entityRendererProvider)
  {
    EntityRendererRegistry.register(entityType, entityRendererProvider);
  }

  @Overwrite
  public static void registerLayerDefinition(ModelLayerLocation layerLocation, Supplier<LayerDefinition> supplier)
  {
    EntityModelLayerRegistry.registerModelLayer(layerLocation, supplier::get);
  }
}