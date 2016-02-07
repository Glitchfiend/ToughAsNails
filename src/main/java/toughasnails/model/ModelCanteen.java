package toughasnails.model;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ItemFluidContainer;

public class ModelCanteen implements ISmartItemModel
{
    private IBakedModel emptyModel;
    private IBakedModel filledModel;
    
    public ModelCanteen(IBakedModel emptyModel, IBakedModel filledModel)
    {
        this.emptyModel = emptyModel;
        this.filledModel = filledModel;
    }
    
    @Override
    public List getFaceQuads(EnumFacing face)
    {
        return Collections.emptyList();
    }

    @Override
    public List getGeneralQuads()
    {
        return Collections.emptyList();
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return false;
    }

    @Override
    public boolean isGui3d()
    {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return null;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack)
    {
        ItemFluidContainer container = (ItemFluidContainer)stack.getItem();
        FluidStack fluid = container.getFluid(stack);
        
        if (fluid != null && fluid.amount > 0)
            return this.filledModel;
        
        return this.emptyModel;
    }
}
