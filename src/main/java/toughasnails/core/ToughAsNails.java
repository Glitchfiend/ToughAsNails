/*******************************************************************************
 * Copyright 2014-2020, the Tough As Nails Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.core;

import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toughasnails.config.ThirstConfig;
import toughasnails.init.ModCapabilities;
import toughasnails.init.ModConfig;
import toughasnails.init.ModHandlers;
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
    }

    private void loadComplete(final FMLLoadCompleteEvent event)
    {
        proxy.init();
    }
}
