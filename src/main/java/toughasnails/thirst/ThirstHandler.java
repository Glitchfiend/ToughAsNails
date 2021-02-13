/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.thirst;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import toughasnails.api.capability.TANCapabilities;
import toughasnails.api.item.TANItems;
import toughasnails.api.potion.TANEffects;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.api.thirst.IThirst;
import toughasnails.config.ServerConfig;
import toughasnails.config.ThirstConfig;
import toughasnails.core.ToughAsNails;
import toughasnails.network.MessageDrinkInWorld;
import toughasnails.network.MessageUpdateThirst;
import toughasnails.network.PacketHandler;
import toughasnails.util.capability.SimpleCapabilityProvider;

public class ThirstHandler
{
    private int lastSentThirst = -99999999;
    private boolean lastThirstHydrationZero = true;

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        // NOTE: We always attach the thirst capability, regardless of the thirst enabled config option.
        // This is mainly to ensure a consistent working environment
        if (event.getObject() instanceof PlayerEntity)
        {
            event.addCapability(new ResourceLocation(ToughAsNails.MOD_ID, "thirst"), new SimpleCapabilityProvider<IThirst>(TANCapabilities.THIRST, new ThirstData()));
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.getPlayer().level.isClientSide())
            return;

        syncThirst((ServerPlayerEntity)event.getPlayer());
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (!ServerConfig.enableThirst.get() || event.player.level.isClientSide())
            return;

        ServerPlayerEntity player = (ServerPlayerEntity)event.player;
        IThirst thirst = ThirstHelper.getThirst(player);
        Difficulty difficulty = player.level.getDifficulty();
        double exhaustionThreshold = ThirstConfig.thirstExhaustionThreshold.get();

        if (thirst.getExhaustion() > exhaustionThreshold)
        {
            thirst.addExhaustion((float)-exhaustionThreshold);

            if (thirst.getHydration() > 0.0F)
            {
                // Deplete hydration
                thirst.setHydration(Math.max(thirst.getHydration() - 1.0F, 0.0F));
            }
            else if (difficulty != Difficulty.PEACEFUL)
            {
                // Reduce thirst bar once hydration has been depleted
                thirst.setThirst(Math.max(thirst.getThirst() - 1, 0));
            }
        }

        if (thirst.getThirst() <= 0)
        {
            thirst.addTicks(1);

            // Cause damage after 80 ticks of an empty thirst bar
            if (thirst.getTickTimer() >= 80)
            {
                if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL)
                {
                    player.hurt(DamageSource.STARVE, 1.0F);
                }

                thirst.setTickTimer(0);
            }
        }
        else
        {
            thirst.setTickTimer(0);
        }

        // Increment thirst if on peaceful mode
        if (difficulty == Difficulty.PEACEFUL && player.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION))
        {
            if (thirst.isThirsty() && player.tickCount % 10 == 0)
            {
                thirst.setThirst(thirst.getThirst() + 1);
            }
        }

        if (this.lastSentThirst != thirst.getThirst() || thirst.getHydration() == 0.0F != this.lastThirstHydrationZero)
        {
            syncThirst(player);
            this.lastSentThirst = thirst.getThirst();
            this.lastThirstHydrationZero = thirst.getHydration() == 0.0F;
        }
    }

    private static void syncThirst(ServerPlayerEntity player)
    {
        IThirst thirst = ThirstHelper.getThirst(player);
        PacketHandler.HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new MessageUpdateThirst(thirst.getThirst(), thirst.getHydration()));
    }

    // Replenish thirst after drinking from items in the config file
    @SubscribeEvent
    public void onItemUseFinish(LivingEntityUseItemEvent.Finish event)
    {
        if (!ServerConfig.enableThirst.get() || !(event.getEntityLiving() instanceof PlayerEntity) || event.getEntity().level.isClientSide())
            return;

        PlayerEntity player = (PlayerEntity)event.getEntityLiving();
        Item item = event.getItem().getItem();
        IThirst thirst = ThirstHelper.getThirst(player);
        ThirstConfig.DrinkEntry entry = ThirstConfig.getDrinkEntry(item.getRegistryName());

        if (entry != null)
        {
            thirst.addThirst(entry.getThirst());
            thirst.addHydration(entry.getHydration());

            if (player.level.random.nextFloat() < entry.getPoisonChance())
            {
                player.addEffect(new EffectInstance(TANEffects.THIRST, 600));
            }
        }
    }

    // Fill bottles with dirty or purified water as appropriate
    @SubscribeEvent
    public void onPlayerInteractItem(PlayerInteractEvent.RightClickItem event)
    {
        PlayerEntity player = event.getPlayer();
        World world = player.level;
        Hand hand = event.getHand();
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();

        // We only care about the glass bottle
        if (item != Items.GLASS_BOTTLE)
            return;

        RayTraceResult rayTraceResult = Item.getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);

        // Defer to the default glass bottle use implementation
        if (rayTraceResult.getType() != RayTraceResult.Type.BLOCK)
            return;

        BlockPos pos = ((BlockRayTraceResult)rayTraceResult).getBlockPos();

        if (!world.mayInteract(player, pos) || !world.getFluidState(pos).is(FluidTags.WATER))
            return;

        // Play the filling sound
        world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);

        RegistryKey<Biome> biome = player.level.getBiomeName(pos).orElse(Biomes.PLAINS);
        ItemStack filledStack;

        // Set the filled stack based on the biome's water type
        switch (ThirstConfig.getBiomeWaterType(biome))
        {
            case PURIFIED:
                filledStack = new ItemStack(TANItems.PURIFIED_WATER_BOTTLE);
                break;

            case DIRTY:
                filledStack = new ItemStack(TANItems.DIRTY_WATER_BOTTLE);
                break;

            case NORMAL:
            default:
                filledStack = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
                break;
        }

        player.awardStat(Stats.ITEM_USED.get(item));
        ItemStack replacementStack = DrinkHelper.createFilledResult(stack, player, filledStack);

        // Replace the glass bottle
        if (stack != replacementStack)
        {
            player.setItemInHand(hand, replacementStack);
            if (replacementStack.isEmpty())
                ForgeEventFactory.onPlayerDestroyItem(player, stack, hand);
        }

        // Cancel the event, we've taken responsibility for bottle filling
        event.setCanceled(true);
        event.setCancellationResult(ActionResult.sidedSuccess(replacementStack, world.isClientSide()).getResult());
    }

    // Hand drinking
    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        if (canHandDrink() && canHandDrinkInWorld(event.getPlayer(), event.getHand()))
            tryDrinkWaterInWorld(event.getPlayer());
    }

    private static final int IN_WORLD_DRINK_COOLDOWN = 3 * 20;
    private static int inWorldDrinkTimer = 0;

    @SubscribeEvent
    public void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event)
    {
        if (canHandDrink() && canHandDrinkInWorld(event.getPlayer(), event.getHand()))
            tryDrinkWaterInWorld(event.getPlayer());
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (inWorldDrinkTimer > 0)
            inWorldDrinkTimer--;
    }

    private static boolean canHandDrink()
    {
        return ServerConfig.enableThirst.get() && ServerConfig.enableHandDrinking.get();
    }

    private static boolean canHandDrinkInWorld(PlayerEntity player, Hand hand)
    {
        return Hand.MAIN_HAND == hand && player.getMainHandItem().isEmpty() && player.isCrouching() && ThirstHelper.getThirst(player).getThirst() < 20 && player.level.isClientSide() && inWorldDrinkTimer <= 0;
    }

    private static void tryDrinkWaterInWorld(PlayerEntity player)
    {
        World world = player.level;
        BlockRayTraceResult rayTraceResult = Item.getPlayerPOVHitResult(player.level, player, RayTraceContext.FluidMode.SOURCE_ONLY);

        if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK)
        {
            BlockPos pos = ((BlockRayTraceResult) rayTraceResult).getBlockPos();

            if (ThirstHelper.canDrink(player, false) && world.mayInteract(player, pos) && world.getFluidState(pos).is(FluidTags.WATER))
            {
                inWorldDrinkTimer = IN_WORLD_DRINK_COOLDOWN;
                PacketHandler.HANDLER.send(PacketDistributor.SERVER.noArg(), new MessageDrinkInWorld(pos));
                player.playSound(SoundEvents.GENERIC_DRINK, 0.5f, 1.0f);
                player.swing(Hand.MAIN_HAND);
            }
        }
    }
}
