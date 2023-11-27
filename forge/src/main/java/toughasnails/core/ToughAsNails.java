/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.core;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.thirst.IThirst;
import toughasnails.init.*;
import toughasnails.network.PacketHandler;

@Mod(value = ToughAsNails.MOD_ID)
public class ToughAsNails
{
    public static final String MOD_ID = "toughasnails";

    public static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(Registries.BLOCK, MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTER = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENU_REGISTER = DeferredRegister.create(Registries.MENU, MOD_ID);
    public static final DeferredRegister<Enchantment> ENCHANTMENT_REGISTER = DeferredRegister.create(Registries.ENCHANTMENT, MOD_ID);
    public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(Registries.ITEM, MOD_ID);
    public static final DeferredRegister<MobEffect> MOB_EFFECT_REGISTER = DeferredRegister.create(Registries.MOB_EFFECT, MOD_ID);
    public static final DeferredRegister<Potion> POTION_REGISTER = DeferredRegister.create(Registries.POTION, MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER_REGISTER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE_REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, MOD_ID);

    public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public ToughAsNails()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::registerCapabilities);
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::loadComplete);

        // Register events for deferred registers
        BLOCK_REGISTER.register(bus);
        BLOCK_ENTITY_REGISTER.register(bus);
        CREATIVE_TAB_REGISTER.register(bus);
        ENCHANTMENT_REGISTER.register(bus);
        ITEM_REGISTER.register(bus);
        MENU_REGISTER.register(bus);
        MOB_EFFECT_REGISTER.register(bus);
        POTION_REGISTER.register(bus);
        RECIPE_SERIALIZER_REGISTER.register(bus);
        RECIPE_TYPE_REGISTER.register(bus);

        // Initialize the config file first so other things can rely on it
        ModConfig.init();

        // Initialize content
        ModContainerTypes.init();
        ModBlockEntities.init();
        ModEnchantments.init();
        ModPotions.init();
        ModCrafting.init();

        PacketHandler.init();
        ModHandlers.init();

        // Initialize the creative tab last after blocks and items have been setup
        ModCreativeTab.setup();
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
