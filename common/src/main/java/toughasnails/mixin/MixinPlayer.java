/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.api.item.TANItems;
import toughasnails.api.player.ITANPlayer;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.thirst.IThirst;
import toughasnails.temperature.TemperatureData;
import toughasnails.temperature.TemperatureHandler;
import toughasnails.thirst.ThirstData;
import toughasnails.thirst.ThirstHandler;
import toughasnails.thirst.ThirstHooks;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity implements ITANPlayer
{
    @Shadow
    @Final
    private Abilities abilities;

    @Unique
    private TemperatureData temperatureData = new TemperatureData();
    @Unique
    private ThirstData thirstData = new ThirstData();
    @Unique
    private boolean climateClemencyGranted = false;


    private MixinPlayer(EntityType<? extends LivingEntity> type, Level level)
    {
        super(type, level);
    }

    @Inject(method="readAdditionalSaveData", at=@At(value="TAIL"))
    public void onReadAdditionalSaveData(CompoundTag nbt, CallbackInfo ci)
    {
        this.temperatureData.readAdditionalSaveData(nbt);
        this.thirstData.readAdditionalSaveData(nbt);
        this.climateClemencyGranted = nbt.getBoolean("climateClemencyGranted");
    }

    @Inject(method="addAdditionalSaveData", at=@At(value="TAIL"))
    public void onAddAdditionalSaveData(CompoundTag nbt, CallbackInfo ci)
    {
        this.temperatureData.addAdditionalSaveData(nbt);
        this.thirstData.addAdditionalSaveData(nbt);
        nbt.putBoolean("climateClemencyGranted", this.climateClemencyGranted);
    }

    @Inject(method="causeFoodExhaustion", at=@At(value="HEAD"))
    public void onCauseFoodExhaustion(float exhaustion, CallbackInfo ci)
    {
        if (!this.abilities.invulnerable)
        {
            if (!this.level().isClientSide)
            {
                ThirstHooks.onCauseFoodExhaustion((Player)(Object)this, exhaustion);
            }
        }
    }

    @Inject(method="tick", at=@At(value="TAIL"))
    public void onTick(CallbackInfo ci)
    {
        Player player = (Player)(Object)this;
        TemperatureHandler.onPlayerTick(player);
        ThirstHandler.onPlayerTick(player);

        for (int i = 0; i < player.getInventory().getContainerSize(); i++)
        {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() == TANItems.LEAF_BOOTS || stack.getItem() == TANItems.LEAF_LEGGINGS || stack.getItem() == TANItems.LEAF_CHESTPLATE || stack.getItem() == TANItems.LEAF_HELMET)
                stack.set(DataComponents.DYED_COLOR, new DyedItemColor(BiomeColors.getAverageFoliageColor(player.level(), player.blockPosition()), false));
        }
    }

    @Override
    public ITemperature getTemperatureData()
    {
        return this.temperatureData;
    }

    @Override
    public IThirst getThirstData()
    {
        return this.thirstData;
    }

    @Override
    public boolean getClimateClemencyGranted()
    {
        return this.climateClemencyGranted;
    }

    @Override
    public void setClimateClemencyGranted(boolean value)
    {
        this.climateClemencyGranted = value;
    }
}
