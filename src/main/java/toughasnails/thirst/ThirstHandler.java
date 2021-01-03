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
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import toughasnails.api.capability.TANCapabilities;
import toughasnails.api.potion.TANEffects;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.api.thirst.IThirst;
import toughasnails.config.ThirstConfig;
import toughasnails.core.ToughAsNails;
import toughasnails.network.MessageUpdateThirst;
import toughasnails.network.PacketHandler;
import toughasnails.util.capability.SimpleCapabilityProvider;

public class ThirstHandler
{
    private static final float THIRST_EXHAUSTION_THRESHOLD = 8.0F;

    private int lastSentThirst = -99999999;
    private boolean lastThirstHydrationZero = true;

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
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
        if (event.player.level.isClientSide())
            return;

        ServerPlayerEntity player = (ServerPlayerEntity)event.player;
        IThirst thirst = ThirstHelper.getThirst(player);
        Difficulty difficulty = player.level.getDifficulty();

        if (thirst.getExhaustion() > THIRST_EXHAUSTION_THRESHOLD)
        {
            thirst.addExhaustion(-THIRST_EXHAUSTION_THRESHOLD);

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

    @SubscribeEvent
    public void onItemUseFinish(LivingEntityUseItemEvent.Finish event)
    {
        if (!(event.getEntityLiving() instanceof PlayerEntity))
            return;

        PlayerEntity player = (PlayerEntity)event.getEntityLiving();
        Item item = event.getItem().getItem();
        IThirst thirst = ThirstHelper.getThirst(player);
        ThirstConfig.DrinkEntry entry = ThirstConfig.getDrinkEntry(item.getRegistryName());

        if (entry != null)
        {
            thirst.addThirst(entry.getThirst());
            thirst.addHydration(entry.getHydration());

            if (player.level.random.nextFloat() > entry.getPoisonChance())
            {
                player.addEffect(new EffectInstance(TANEffects.thirst, 600));
            }
        }
    }

    private static void syncThirst(ServerPlayerEntity player)
    {
        IThirst thirst = ThirstHelper.getThirst(player);
        PacketHandler.HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new MessageUpdateThirst(thirst.getThirst(), thirst.getHydration()));
    }
}
