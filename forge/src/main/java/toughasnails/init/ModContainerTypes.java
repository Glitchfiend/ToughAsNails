/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;
import toughasnails.api.inventory.container.TANContainerTypes;
import toughasnails.container.WaterPurifierContainer;
import toughasnails.core.ToughAsNails;
import toughasnails.gui.WaterPurifierScreen;

public class ModContainerTypes
{
    public static void init()
    {
        registerContainers();
    }

    private static void registerContainers()
    {
        TANContainerTypes.WATER_PURIFIER = register("water_purifier", WaterPurifierContainer::new);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens()
    {
        MenuScreens.register((MenuType<WaterPurifierContainer>)TANContainerTypes.WATER_PURIFIER.get(), WaterPurifierScreen::new);
    }

    public static <T extends AbstractContainerMenu> RegistryObject<MenuType<?>> register(String name, MenuType.MenuSupplier<T> factory)
    {
        return ToughAsNails.MENU_REGISTER.register(name, () -> new MenuType<>(factory, FeatureFlags.DEFAULT_FLAGS));
    }
}
