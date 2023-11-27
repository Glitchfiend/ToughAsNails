/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import glitchcore.event.Events;
import glitchcore.forge.GlitchCoreForge;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import toughasnails.temperature.TemperatureHandler;
import toughasnails.thirst.ThirstHandler;

public class ModHandlers
{
    public static void init()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(new ThirstHandler());
        MinecraftForge.EVENT_BUS.register(new TemperatureHandler());

        Events.BLOCK_REGISTRY_EVENT.addListener(ModBlocks::registerBlocks);
        Events.ITEM_REGISTRY_EVENT.addListener(ModItems::registerItems);

        GlitchCoreForge.prepareEventHandlers(bus);
    }
}
