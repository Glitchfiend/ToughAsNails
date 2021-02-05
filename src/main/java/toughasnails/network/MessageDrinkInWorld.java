/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.fml.network.NetworkEvent;
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

    public static void encode(MessageDrinkInWorld packet, PacketBuffer buf)
    {
        buf.writeBlockPos(packet.pos);
    }

    public static MessageDrinkInWorld decode(PacketBuffer buf)
    {
        return new MessageDrinkInWorld(buf.readBlockPos());
    }

    public static class Handler
    {
        public static void handle(final MessageDrinkInWorld packet, Supplier<NetworkEvent.Context> context)
        {
            context.get().enqueueWork(() ->
            {
                ServerPlayerEntity player = context.get().getSender();
                World world = player.level;
                IThirst thirst = ThirstHelper.getThirst(player);

                // Whilst we already checked on the client, check again to be sure
                if (world.mayInteract(player, packet.pos) && world.getFluidState(packet.pos).is(FluidTags.WATER))
                {
                    thirst.addThirst(ThirstConfig.handDrinkingThirst.get());
                    thirst.addHydration(ThirstConfig.handDrinkingHydration.get().floatValue());

                    RegistryKey<Biome> biome = player.level.getBiomeName(packet.pos).orElse(Biomes.PLAINS);

                    if (player.level.random.nextFloat() < ThirstConfig.getBiomeWaterType(biome).getPoisonChance())
                    {
                        player.addEffect(new EffectInstance(TANEffects.THIRST, 600));
                    }
                }
            });
            context.get().setPacketHandled(true);
        }
    }
}
