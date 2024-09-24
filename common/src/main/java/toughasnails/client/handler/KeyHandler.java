/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.client.handler;

import com.mojang.blaze3d.platform.InputConstants;
import glitchcore.event.client.InputEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class KeyHandler
{
    public static void onKeyPress(InputEvent.Key event)
    {
        Minecraft minecraft = Minecraft.getInstance();
        boolean isF3Down = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_F3);

        if (minecraft.screen == null && event.getAction() != InputConstants.RELEASE && isF3Down)
        {
            boolean handledDebugKey = false;

            switch (event.getKey())
            {
                case InputConstants.KEY_7 -> {
                    LevelRenderHandler.enableDebug = !LevelRenderHandler.enableDebug;

                    if (LevelRenderHandler.enableDebug) debugFeedbackTranslated("debug.temperature_fill.on");
                    else debugFeedbackTranslated("debug.temperature_fill.off");
                    
                    handledDebugKey = true;
                }
                case InputConstants.KEY_Q -> {
                    ChatComponent component = minecraft.gui.getChat();
                    component.addMessage(Component.translatable("debug.temperature_fill.help"));
                    // Should already be marked as handled by Vanilla
                }
            }

            event.setHandledDebugKey(event.getHandledDebugKey() | handledDebugKey);
        }
    }


    private static void debugComponent(ChatFormatting formatting, Component component) {
        Minecraft.getInstance()
            .gui
            .getChat()
            .addMessage(
                    Component.empty()
                            .append(Component.translatable("debug.prefix").withStyle(formatting, ChatFormatting.BOLD))
                            .append(CommonComponents.SPACE)
                            .append(component)
            );
    }

    private static void debugFeedbackComponent(Component $$0) {
        debugComponent(ChatFormatting.YELLOW, $$0);
    }

    private static void debugFeedbackTranslated(String $$0, Object... $$1) {
        debugFeedbackComponent(Component.translatableEscape($$0, $$1));
    }
}
