package toughasnails.handler;

import toughasnails.init.ModModels;
import toughasnails.model.ModelCanteen;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModelBakeHandler
{
    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event)
    {
        IRegistry modelRegistry = event.modelRegistry;
        
        IBakedModel emptyCanteenModel = (IBakedModel)modelRegistry.getObject(ModModels.CANTEEN_EMPTY);
        IBakedModel filledCanteenModel = (IBakedModel)modelRegistry.getObject(ModModels.CANTEEN_FILLED);
        
        modelRegistry.putObject(ModModels.CANTEEN, new ModelCanteen(emptyCanteenModel, filledCanteenModel));
    }
}
