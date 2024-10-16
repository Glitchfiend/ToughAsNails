/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.network;

import glitchcore.network.CustomPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import toughasnails.api.potion.TANEffects;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.init.ModConfig;
import toughasnails.init.ModTags;

public class DrinkInWorldPacket implements CustomPacket<DrinkInWorldPacket>
{
    private BlockPos pos;

    public DrinkInWorldPacket(BlockPos pos)
    {
        this.pos = pos;
    }

    public DrinkInWorldPacket() {}

    @Override
    public void encode(FriendlyByteBuf buf)
    {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public DrinkInWorldPacket decode(FriendlyByteBuf buf)
    {
        return new DrinkInWorldPacket(buf.readBlockPos());
    }

    @Override
    public void handle(DrinkInWorldPacket packet, Context context)
    {
        context.getPlayer().ifPresent(player -> {
            Level level = player.level();
            IThirst thirst = ThirstHelper.getThirst(player);

            // Whilst we already checked on the client, check again to be sure
            if (level.mayInteract(player, packet.pos) && level.getFluidState(packet.pos).is(FluidTags.WATER))
            {
                thirst.drink(ModConfig.thirst.handDrinkingThirst, (float)ModConfig.thirst.handDrinkingHydration);

                Holder<Biome> biome = level.getBiome(packet.pos);

                if (level.random.nextFloat() < ModTags.Biomes.getBiomeWaterType(biome).getPoisonChance())
                {
                    player.addEffect(new MobEffectInstance(TANEffects.THIRST, 600));
                }
            }
        });
    }
}
