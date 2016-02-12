package toughasnails.client.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ISmartItemModel;

public class ModelThermometer extends IFlexibleBakedModel.Wrapper implements ISmartItemModel
{
    private IBakedModel[] frames;
    
    public ModelThermometer(IModel defaultModel, TextureAtlasSprite[] frameTextures)
    {
        super(null, DefaultVertexFormats.ITEM);

        this.frames = ModelUtils.generateModelsForTextures(defaultModel, frameTextures);
     }

    @Override
    public IBakedModel handleItemState(ItemStack stack)
    {
    	return this.frames[9];
    }
}
