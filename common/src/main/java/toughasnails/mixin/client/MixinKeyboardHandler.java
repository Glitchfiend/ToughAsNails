/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin.client;

import toughasnails.glitch.event.EventManager;
import toughasnails.glitch.event.client.InputEvent;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class MixinKeyboardHandler
{
  @Shadow
  @Final
  private Minecraft minecraft;

  @Shadow private boolean handledDebugKey;

  @Inject(method = "keyPress", at=@At("TAIL"))
  public void onKeyInput(long window, int key, int scanCode, int action, int modifiers, CallbackInfo ci)
  {
    if (window != this.minecraft.getWindow().getWindow())
      return;

    var event = new InputEvent.Key(key, scanCode, action, modifiers, this.handledDebugKey);
    EventManager.fire(event);
    this.handledDebugKey = event.getHandledDebugKey();
  }
}