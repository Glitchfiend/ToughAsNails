package toughasnails.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelFreeze extends ModelBase
{
    private ModelRenderer[] freezeSticks = new ModelRenderer[12];
    private ModelRenderer freezeHead;

    public ModelFreeze()
    {
        for (int i = 0; i < this.freezeSticks.length; ++i)
        {
            this.freezeSticks[i] = new ModelRenderer(this, 0, 16);
            this.freezeSticks[i].addBox(0.0F, 0.0F, 0.0F, 2, 8, 2);
        }

        this.freezeHead = new ModelRenderer(this, 0, 0);
        this.freezeHead.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
    }

    @Override
    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
    {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        this.freezeHead.render(scale);

        for (int i = 0; i < this.freezeSticks.length; ++i)
        {
            this.freezeSticks[i].render(scale);
        }
    }

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
    {
        float f = p_78087_3_ * (float)Math.PI * -0.1F;

        for (int i = 0; i < 4; ++i)
        {
            this.freezeSticks[i].rotationPointY = -2.0F + MathHelper.cos(((float)(i * 2) + p_78087_3_) * 0.25F);
            this.freezeSticks[i].rotationPointX = MathHelper.cos(f) * 9.0F;
            this.freezeSticks[i].rotationPointZ = MathHelper.sin(f) * 9.0F;
            ++f;
        }

        f = ((float)Math.PI / 4F) + p_78087_3_ * (float)Math.PI * 0.03F;

        for (int j = 4; j < 8; ++j)
        {
            this.freezeSticks[j].rotationPointY = 2.0F + MathHelper.cos(((float)(j * 2) + p_78087_3_) * 0.25F);
            this.freezeSticks[j].rotationPointX = MathHelper.cos(f) * 7.0F;
            this.freezeSticks[j].rotationPointZ = MathHelper.sin(f) * 7.0F;
            ++f;
        }

        f = 0.47123894F + p_78087_3_ * (float)Math.PI * -0.05F;

        for (int k = 8; k < 12; ++k)
        {
            this.freezeSticks[k].rotationPointY = 11.0F + MathHelper.cos(((float)k * 1.5F + p_78087_3_) * 0.5F);
            this.freezeSticks[k].rotationPointX = MathHelper.cos(f) * 5.0F;
            this.freezeSticks[k].rotationPointZ = MathHelper.sin(f) * 5.0F;
            ++f;
        }

        this.freezeHead.rotateAngleY = p_78087_4_ / (180F / (float)Math.PI);
        this.freezeHead.rotateAngleX = p_78087_5_ / (180F / (float)Math.PI);
    }
}