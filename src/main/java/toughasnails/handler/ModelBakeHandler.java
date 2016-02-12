package toughasnails.handler;

import java.io.IOException;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.client.texture.ModelThermometer;
import toughasnails.client.texture.TextureAnimationFrame;
import toughasnails.client.texture.TextureUtils;
import toughasnails.init.ModModels;
import toughasnails.model.ModelCanteen;

public class ModelBakeHandler
{
	public static final ModelResourceLocation THERMOMETER_LOC = new ModelResourceLocation("toughasnails:thermometer", "inventory");
	
	private TextureAnimationFrame[] thermometerFrames;
	
    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event)
    {
        TextureMap map = event.map;
        thermometerFrames = TextureUtils.splitAnimatedTexture(map, "toughasnails:items/thermometer", 21);
    }
    
    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Post event)
    {
        TextureMap map = event.map;
    }
	
    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) throws IOException
    {
        IRegistry modelRegistry = event.modelRegistry;
        ModelLoader modelLoader = event.modelLoader;
        
        IBakedModel emptyCanteenModel = (IBakedModel)modelRegistry.getObject(ModModels.CANTEEN_EMPTY);
        IBakedModel filledCanteenModel = (IBakedModel)modelRegistry.getObject(ModModels.CANTEEN_FILLED);
        
        modelRegistry.putObject(ModModels.CANTEEN, new ModelCanteen(emptyCanteenModel, filledCanteenModel));
        
    	IModel thermometerModel = modelLoader.getModel(THERMOMETER_LOC);
        modelRegistry.putObject(THERMOMETER_LOC, new ModelThermometer(thermometerModel, thermometerFrames));
    }
}
