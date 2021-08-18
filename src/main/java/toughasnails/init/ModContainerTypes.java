/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
    public static void registerContainers(RegistryEvent.Register<BlockEntityType<?>> event)
    {
        register("water_purifier", WaterPurifierContainer::new);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens()
    {
        MenuScreens.register((MenuType<WaterPurifierContainer>)TANContainerTypes.WATER_PURIFIER, WaterPurifierScreen::new);
    }

    public static <T extends AbstractContainerMenu> void register(String name, MenuType.MenuSupplier<T> factory)
    {
        MenuType<T> containerType = new MenuType<>(factory);
        containerType.setRegistryName(name);
        ForgeRegistries.CONTAINERS.register(containerType);
    }
}
