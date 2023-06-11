/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.tags.FluidTags;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.network.NetworkEvent;
import toughasnails.api.potion.TANEffects;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.config.ThirstConfig;

import java.util.function.Supplier;

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
        public static void handle(final MessageDrinkInWorld packet, Supplier<NetworkEvent.Context> context)
        {
            context.get().enqueueWork(() ->
            {
                ServerPlayer player = context.get().getSender();
                Level world = player.level();
                IThirst thirst = ThirstHelper.getThirst(player);

                // Whilst we already checked on the client, check again to be sure
                if (world.mayInteract(player, packet.pos) && world.getFluidState(packet.pos).is(FluidTags.WATER))
                {
                    thirst.addThirst(ThirstConfig.handDrinkingThirst.get());
                    thirst.addHydration(ThirstConfig.handDrinkingHydration.get().floatValue());

                    ResourceKey<Biome> biome = player.level().getBiome(packet.pos).unwrapKey().orElse(Biomes.PLAINS);

                    if (player.level().random.nextFloat() < ThirstConfig.getBiomeWaterType(biome).getPoisonChance())
                    {
                        player.addEffect(new MobEffectInstance(TANEffects.THIRST.get(), 600));
                    }
                }
            });
            context.get().setPacketHandled(true);
        }
    }
}
