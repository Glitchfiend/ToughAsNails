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
import net.minecraft.network.play.server.SUpdateHealthPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import toughasnails.api.TANCapabilities;
import toughasnails.api.ThirstHelper;
import toughasnails.api.capability.IThirst;
import toughasnails.core.ToughAsNails;
import toughasnails.network.MessageUpdateThirst;
import toughasnails.network.PacketHandler;
import toughasnails.util.capability.SimpleCapabilityProvider;

public class ThirstHandler
{
    private static final float THIRST_EXHAUSTION_THRESHOLD = 4.0F;

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

        if (thirst.getExhaustionLevel() > THIRST_EXHAUSTION_THRESHOLD)
        {
            thirst.addExhaustion(-THIRST_EXHAUSTION_THRESHOLD);

            if (thirst.getHydrationLevel() > 0.0F)
            {
                // Deplete hydration
                thirst.setHydrationLevel(Math.max(thirst.getHydrationLevel() - 1.0F, 0.0F));
            }
            else if (difficulty != Difficulty.PEACEFUL)
            {
                // Reduce thirst bar once hydration has been depleted
                thirst.setThirstLevel(Math.max(thirst.getThirstLevel() - 1, 0));
            }
        }

        if (thirst.getThirstLevel() <= 0)
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

        if (this.lastSentThirst != thirst.getThirstLevel() || thirst.getHydrationLevel() == 0.0F != this.lastThirstHydrationZero)
        {
            syncThirst(player);
            this.lastSentThirst = thirst.getThirstLevel();
            this.lastThirstHydrationZero = thirst.getHydrationLevel() == 0.0F;
        }
    }

    private static void syncThirst(ServerPlayerEntity player)
    {
        IThirst thirst = ThirstHelper.getThirst(player);
        PacketHandler.HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new MessageUpdateThirst(thirst.getThirstLevel(), thirst.getHydrationLevel()));
    }
}
