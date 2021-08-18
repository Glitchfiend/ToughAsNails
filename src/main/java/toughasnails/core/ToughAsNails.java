/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.core;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toughasnails.init.*;
import toughasnails.network.PacketHandler;

@Mod(value = ToughAsNails.MOD_ID)
public class ToughAsNails
{
    public static final String MOD_ID = "toughasnails";

    public static ToughAsNails instance;
    public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static Logger logger = LogManager.getLogger(MOD_ID);

    public ToughAsNails()
    {
        instance = this;

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);

        ModApi.init();
        ModConfig.init();
        PacketHandler.init();
        ModHandlers.init();
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        ModCapabilities.init();
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
        ModContainerTypes.registerScreens();
    }

    private void loadComplete(final FMLLoadCompleteEvent event)
    {
        proxy.init();
    }
}
