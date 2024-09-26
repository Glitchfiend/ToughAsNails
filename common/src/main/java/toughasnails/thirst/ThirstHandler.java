/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.thirst;

import glitchcore.event.TickEvent;
import glitchcore.event.entity.LivingEntityUseItemEvent;
import glitchcore.event.player.PlayerEvent;
import glitchcore.event.player.PlayerInteractEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import toughasnails.api.damagesource.TANDamageTypes;
import toughasnails.api.item.TANItems;
import toughasnails.api.potion.TANEffects;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.init.ModConfig;
import toughasnails.init.ModPackets;
import toughasnails.init.ModTags;
import toughasnails.network.DrinkInWorldPacket;
import toughasnails.network.UpdateThirstPacket;
import toughasnails.temperature.TemperatureData;

public class ThirstHandler
{
    public static void onPlayerTick(Player player)
    {
        if (!ModConfig.thirst.enableThirst() || player.level().isClientSide())
            return;

        IThirst thirst = ThirstHelper.getThirst(player);
        Difficulty difficulty = player.level().getDifficulty();
        double exhaustionThreshold = ModConfig.thirst.thirstExhaustionThreshold();

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
                    player.hurt(player.damageSources().source(TANDamageTypes.THIRST), 1.0F);
                }

                thirst.setTickTimer(0);
            }
        }
        else
        {
            thirst.setTickTimer(0);
        }

        // Increment thirst if on peaceful mode
        if (difficulty == Difficulty.PEACEFUL && player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION))
        {
            if (thirst.isThirsty() && player.tickCount % 10 == 0)
            {
                thirst.setThirst(thirst.getThirst() + 1);
            }
        }
    }

    public static void onChangeDimension(PlayerEvent.ChangeDimension event)
    {
        ITemperature temperature = TemperatureHelper.getTemperatureData(event.getPlayer());
        temperature.setLastLevel(TemperatureData.DEFAULT_LEVEL);
        temperature.setLastHyperthermiaTicks(0);
    }

    public static void syncThirst(ServerPlayer player)
    {
        IThirst thirst = ThirstHelper.getThirst(player);
        ModPackets.HANDLER.sendToPlayer(new UpdateThirstPacket(thirst.getThirst(), thirst.getHydration()), player);
        thirst.setLastThirst(thirst.getThirst());
        thirst.setLastHydrationZero(thirst.getHydration() == 0.0F);
    }

    // Replenish thirst after drinking from items in the config file
    public static void onItemUseFinish(LivingEntityUseItemEvent.Finish event)
    {
        if (!ModConfig.thirst.enableThirst() || !(event.getEntity() instanceof Player) || event.getEntity().level().isClientSide())
            return;

        Player player = (Player)event.getEntity();
        ItemStack drink = event.getItem();
        IThirst thirst = ThirstHelper.getThirst(player);

        if (drink.is(ModTags.Items.DRINKS))
        {
            int drink_thirst = ModTags.Items.getThirstRestored(drink);
            float drink_hydration = 0.0F;
            float drink_poison_chance = 0.0F;

            ///////////////////

            if (drink.is(ModTags.Items.TEN_HYDRATION_DRINKS)) { drink_hydration = 0.1F; }
            if (drink.is(ModTags.Items.TWENTY_HYDRATION_DRINKS)) { drink_hydration = 0.2F; }
            if (drink.is(ModTags.Items.THIRTY_HYDRATION_DRINKS)) { drink_hydration = 0.3F; }
            if (drink.is(ModTags.Items.FOURTY_HYDRATION_DRINKS)) { drink_hydration = 0.4F; }
            if (drink.is(ModTags.Items.FIFTY_HYDRATION_DRINKS)) { drink_hydration = 0.5F; }
            if (drink.is(ModTags.Items.SIXTY_HYDRATION_DRINKS)) { drink_hydration = 0.6F; }
            if (drink.is(ModTags.Items.SEVENTY_HYDRATION_DRINKS)) { drink_hydration = 0.7F; }
            if (drink.is(ModTags.Items.EIGHTY_HYDRATION_DRINKS)) { drink_hydration = 0.8F; }
            if (drink.is(ModTags.Items.NINETY_HYDRATION_DRINKS)) { drink_hydration = 0.9F; }
            if (drink.is(ModTags.Items.ONE_HUNDRED_HYDRATION_DRINKS)) { drink_hydration = 1.0F; }

            ///////////////////

            if (drink.is(ModTags.Items.TWENTY_FIVE_POISON_CHANCE_DRINKS)) { drink_poison_chance = 0.25F; }
            if (drink.is(ModTags.Items.FIFTY_POISON_CHANCE_DRINKS)) { drink_poison_chance = 0.5F; }
            if (drink.is(ModTags.Items.SEVENTY_FIVE_POISON_CHANCE_DRINKS)) { drink_poison_chance = 0.75F; }
            if (drink.is(ModTags.Items.ONE_HUNDRED_POISON_CHANCE_DRINKS)) { drink_poison_chance = 1.0F; }

            ///////////////////

            // Based on FoodData's eat
            thirst.drink(drink_thirst, drink_hydration);

            if (player.level().random.nextFloat() < drink_poison_chance)
            {
                player.addEffect(new MobEffectInstance(TANEffects.THIRST, 600));
            }
        }
    }

    // Fill bottles with dirty or purified water as appropriate
    public static void onPlayerUseItem(PlayerInteractEvent.UseItem event)
    {
        Player player = event.getPlayer();
        Level level = player.level();
        InteractionHand hand = event.getHand();
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();

        // We only care about the glass bottle
        if (item != Items.GLASS_BOTTLE)
            return;

        HitResult rayTraceResult = Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        // Defer to the default glass bottle use implementation
        if (rayTraceResult.getType() != HitResult.Type.BLOCK)
            return;

        BlockPos pos = ((BlockHitResult)rayTraceResult).getBlockPos();

        if (!level.mayInteract(player, pos) || !level.getFluidState(pos).is(FluidTags.WATER))
            return;

        // Play the filling sound
        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);

        Holder<Biome> biome = level.getBiome(pos);
        ItemStack filledStack;

        // Set the filled stack based on the biome's water type
        if (biome.is(ModTags.Biomes.DIRTY_WATER_BIOMES))
        {
            filledStack = new ItemStack(TANItems.DIRTY_WATER_BOTTLE);
        }
        else if (biome.is(ModTags.Biomes.PURIFIED_WATER_BIOMES))
        {
            filledStack = new ItemStack(TANItems.PURIFIED_WATER_BOTTLE);
        }
        else
        {
            filledStack = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
        }

        player.awardStat(Stats.ITEM_USED.get(item));
        ItemStack replacementStack = ItemUtils.createFilledResult(stack, player, filledStack);

        // Cancel the event, we've taken responsibility for bottle filling
        event.setCancelResult(InteractionResultHolder.sidedSuccess(replacementStack, level.isClientSide()));
        event.setCancelled(true);
    }

    // Hand drinking
    private static final int IN_WORLD_DRINK_COOLDOWN = 3 * 20;
    private static int inWorldDrinkTimer = 0;

    public static void onUseBlock(PlayerInteractEvent.UseBlock event)
    {
        if (canHandDrink() && canHandDrinkInWorld(event.getPlayer(), event.getHand()))
            tryDrinkWaterInWorld(event.getPlayer());
    }

    public static void onUseEmpty(PlayerInteractEvent.UseEmpty event)
    {
        if (canHandDrink() && canHandDrinkInWorld(event.getPlayer(), event.getHand()))
            tryDrinkWaterInWorld(event.getPlayer());
    }

    public static void onClientTick(TickEvent.Client event)
    {
        if (inWorldDrinkTimer > 0)
            inWorldDrinkTimer--;
    }

    private static boolean canHandDrink()
    {
        return ModConfig.thirst.enableThirst() && ModConfig.thirst.enableHandDrinking();
    }

    private static boolean canHandDrinkInWorld(Player player, InteractionHand hand)
    {
        return InteractionHand.MAIN_HAND == hand && player.getMainHandItem().isEmpty() && player.isCrouching() && ThirstHelper.getThirst(player).getThirst() < 20 && player.level().isClientSide() && inWorldDrinkTimer <= 0;
    }

    private static void tryDrinkWaterInWorld(Player player)
    {
        Level world = player.level();
        BlockHitResult rayTraceResult = Item.getPlayerPOVHitResult(player.level(), player, ClipContext.Fluid.SOURCE_ONLY);

        if (rayTraceResult.getType() == HitResult.Type.BLOCK)
        {
            BlockPos pos = ((BlockHitResult) rayTraceResult).getBlockPos();

            if (ThirstHelper.canDrink(player, false) && world.mayInteract(player, pos) && world.getFluidState(pos).is(FluidTags.WATER))
            {
                inWorldDrinkTimer = IN_WORLD_DRINK_COOLDOWN;
                ModPackets.HANDLER.sendToServer(new DrinkInWorldPacket(pos));
                player.playSound(SoundEvents.GENERIC_DRINK, 0.5f, 1.0f);
                player.swing(InteractionHand.MAIN_HAND);
            }
        }
    }
}
