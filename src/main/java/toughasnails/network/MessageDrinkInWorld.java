/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.event.network.CustomPayloadEvent;
import toughasnails.api.potion.TANEffects;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.config.ThirstConfig;

public class MessageDrinkInWorld
{
    public BlockPos pos;

    public MessageDrinkInWorld(BlockPos pos)
    {
        this.pos = pos;
    }

    public static void encode(MessageDrinkInWorld packet, FriendlyByteBuf buf)
    {
        buf.writeBlockPos(packet.pos);
    }

    public static MessageDrinkInWorld decode(FriendlyByteBuf buf)
    {
        return new MessageDrinkInWorld(buf.readBlockPos());
    }

    public static class Handler
    {
        public static void handle(final MessageDrinkInWorld packet, CustomPayloadEvent.Context context)
        {
            context.enqueueWork(() ->
            {
                ServerPlayer player = context.getSender();
                Level level = player.level();
                IThirst thirst = ThirstHelper.getThirst(player);

                // Whilst we already checked on the client, check again to be sure
                if (level.mayInteract(player, packet.pos) && level.getFluidState(packet.pos).is(FluidTags.WATER))
                {
                    thirst.addThirst(ThirstConfig.handDrinkingThirst.get());
                    thirst.addHydration(ThirstConfig.handDrinkingHydration.get().floatValue());

                    Holder<Biome> biome = level.getBiome(packet.pos);

                    if (level.random.nextFloat() < ThirstConfig.getBiomeWaterType(biome).getPoisonChance())
                    {
                        player.addEffect(new MobEffectInstance(TANEffects.THIRST.get(), 600));
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
