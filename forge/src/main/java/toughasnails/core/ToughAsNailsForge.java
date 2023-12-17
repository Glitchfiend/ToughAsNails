/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.core;

import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.thirst.IThirst;
import toughasnails.init.*;

@Mod(value = ToughAsNailsForge.MOD_ID)
public class ToughAsNailsForge
{
    public static final String MOD_ID = "toughasnails";

    public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public ToughAsNailsForge()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::registerCapabilities);
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::loadComplete);

        ToughAsNails.init();
        ModHandlers.init();
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event)
    {
        event.register(IThirst.class);
        event.register(ITemperature.class);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            // Initialize here to ensure the config has already been setup. Forge now enforces this annoyingly.
            ModApi.init();
        });
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            ModContainerTypes.registerScreens();
        });
    }

    private void loadComplete(final FMLLoadCompleteEvent event)
    {
        event.enqueueWork(() ->
        {
            proxy.init();
            ModCrafting.registerPotionRecipes();
            ModTags.init();
            ModCompatibility.init();
        });
    }
}
