/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import glitchcore.util.Environment;
import java.util.function.BiConsumer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import toughasnails.api.TANAPI;
import toughasnails.api.container.TANContainerTypes;
import toughasnails.client.gui.ThermoregulatorScreen;
import toughasnails.client.gui.WaterPurifierScreen;
import toughasnails.container.ThermoregulatorContainer;
import toughasnails.container.WaterPurifierContainer;

public class ModContainerTypes
{
    public static void registerContainers(BiConsumer<ResourceLocation, MenuType<?>> func)
    {
        TANContainerTypes.WATER_PURIFIER = register(func, "water_purifier", WaterPurifierContainer::new);
        TANContainerTypes.THERMOREGULATOR = register(func, "thermoregulator", ThermoregulatorContainer::new);

        if (Environment.isClient())
        {
            MenuScreens.register((MenuType<WaterPurifierContainer>) TANContainerTypes.WATER_PURIFIER, WaterPurifierScreen::new);
            MenuScreens.register((MenuType<ThermoregulatorContainer>) TANContainerTypes.THERMOREGULATOR, ThermoregulatorScreen::new);
        }
    }

    public static <T extends AbstractContainerMenu> MenuType<?> register(BiConsumer<ResourceLocation, MenuType<?>> func, String name, MenuType.MenuSupplier<T> factory)
    {
        var menuType = new MenuType<>(factory, FeatureFlags.DEFAULT_FLAGS);
        func.accept(new ResourceLocation(TANAPI.MOD_ID, name), menuType);
        return menuType;
    }
}
