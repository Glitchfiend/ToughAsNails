/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import glitchcore.event.IRegistryEventContext;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import toughasnails.api.TANAPI;
import toughasnails.api.container.TANContainerTypes;
import toughasnails.container.WaterPurifierContainer;
import toughasnails.client.gui.WaterPurifierScreen;

public class ModContainerTypes
{
    public static void registerContainers(IRegistryEventContext<MenuType<?>> context)
    {
        TANContainerTypes.WATER_PURIFIER = register(context, "water_purifier", WaterPurifierContainer::new);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens()
    {
        MenuScreens.register((MenuType<WaterPurifierContainer>)TANContainerTypes.WATER_PURIFIER, WaterPurifierScreen::new);
    }

    public static <T extends AbstractContainerMenu> MenuType<?> register(IRegistryEventContext<MenuType<?>> context, String name, MenuType.MenuSupplier<T> factory)
    {
        return context.register(new ResourceLocation(TANAPI.MOD_ID, name), new MenuType<>(factory, FeatureFlags.DEFAULT_FLAGS));
    }
}
