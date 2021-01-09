/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.thirst;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import toughasnails.api.capability.TANCapabilities;
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

    @SubscribeEvent
    public void onItemUseFinish(LivingEntityUseItemEvent.Finish event)
    {
        if (!ServerConfig.enableThirst.get() || !(event.getEntityLiving() instanceof PlayerEntity))
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
                player.addEffect(new EffectInstance(TANEffects.thirst, 600));
            }
        }
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        if (ServerConfig.enableThirst.get() && canDrinkInWorld(event.getPlayer(), event.getHand()))
            tryDrinkWaterInWorld(event.getPlayer());
    }

    @SubscribeEvent
    public void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event)
    {
        if (ServerConfig.enableThirst.get() && canDrinkInWorld(event.getPlayer(), event.getHand()))
            tryDrinkWaterInWorld(event.getPlayer());
    }

    private static boolean canDrinkInWorld(PlayerEntity player, Hand hand)
    {
        return Hand.MAIN_HAND == hand && player.getMainHandItem().isEmpty() && player.isCrouching() && ThirstHelper.getThirst(player).getThirst() < 20 && player.level.isClientSide();
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
                PacketHandler.HANDLER.send(PacketDistributor.SERVER.noArg(), new MessageDrinkInWorld(pos));
                player.playSound(SoundEvents.GENERIC_DRINK, 0.5f, 1.0f);
                player.swing(Hand.MAIN_HAND);
            }
        }
    }
}
