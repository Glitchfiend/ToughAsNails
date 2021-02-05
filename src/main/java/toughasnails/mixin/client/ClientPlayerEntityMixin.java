/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.MovementInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.api.thirst.ThirstHelper;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity
{
    @Shadow
    public MovementInput input;

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile)
    {
        super(world, profile);
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    protected void onAiStep(CallbackInfo ci)
    {
        if (this.isSprinting())
        {
            boolean sprintingAllowable = canSprintWithThirst((ClientPlayerEntity)(Object)this);

            if (this.isSwimming())
            {
                if (!this.onGround && !this.input.shiftKeyDown && !sprintingAllowable || !this.isInWater())
                {
                    this.setSprinting(false);
                }
            }
            else if (!sprintingAllowable)
            {
                this.setSprinting(false);
            }
        }
    }

    @Redirect(method={"aiStep"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/entity/player/ClientPlayerEntity;setSprinting(Z)V"))
    protected void aiStep_setSprinting(ClientPlayerEntity player, boolean sprinting)
    {
        // Don't allow sprinting if the player has insufficient thirst
        if (sprinting && !canSprintWithThirst(player))
            sprinting = false;

        player.setSprinting(sprinting);
    }

    private boolean canSprintWithThirst(ClientPlayerEntity player)
    {
        return ThirstHelper.getThirst(player).getThirst() > 6;
    }
}
