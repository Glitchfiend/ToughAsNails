package toughasnails.entities;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFreeze extends RenderLiving<EntityFreeze>
{
    private static final ResourceLocation freezeTextures = new ResourceLocation("toughasnails:textures/entity/freeze.png");

    public RenderFreeze(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelFreeze(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityFreeze entity)
    {
        return freezeTextures;
    }
}