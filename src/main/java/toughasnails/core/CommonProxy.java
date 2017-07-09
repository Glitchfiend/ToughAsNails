package toughasnails.core;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import toughasnails.particle.TANParticleTypes;

public class CommonProxy
{
    public void registerRenderers() {}
    public void registerItemVariantModel(Item item, String name, int metadata) {}
    public void registerNonRenderingProperties(Block block) {}
    public void registerFluidBlockRendering(Block block, String name) {}
    public void spawnParticle(TANParticleTypes type, World parWorld, double x, double y, double z, Object... info) {}
}