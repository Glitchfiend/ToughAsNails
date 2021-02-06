/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import toughasnails.api.inventory.container.TANContainerTypes;
import toughasnails.gui.WaterPurifierScreen;
import toughasnails.container.WaterPurifierContainer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModContainerTypes
{
    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<TileEntityType<?>> event)
    {
        register("water_purifier", WaterPurifierContainer::new);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens()
    {
        ScreenManager.register((ContainerType<WaterPurifierContainer>)TANContainerTypes.WATER_PURIFIER, WaterPurifierScreen::new);
    }

    public static <T extends Container> void register(String name, ContainerType.IFactory<T> factory)
    {
        ContainerType<T> containerType = new ContainerType<>(factory);
        containerType.setRegistryName(name);
        ForgeRegistries.CONTAINERS.register(containerType);
    }
}
