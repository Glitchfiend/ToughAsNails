/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin.glitchcore.client;

import glitchcore.event.EventManager;
import glitchcore.event.TickEvent;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft
{
    @Inject(method="tick", at=@At(value="HEAD"))
    public void onBeginTick(CallbackInfo ci)
    {
        EventManager.fire(new TickEvent.Client(TickEvent.Phase.START));
    }

    @Inject(method="tick", at=@At(value="TAIL"))
    public void onEndTick(CallbackInfo ci)
    {
        EventManager.fire(new TickEvent.Client(TickEvent.Phase.END));
    }
}
