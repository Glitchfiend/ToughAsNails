/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import toughasnails.api.TANAPI;
import toughasnails.container.ThermoregulatorContainer;

public class ThermoregulatorScreen extends AbstractContainerScreen<ThermoregulatorContainer>
{
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(TANAPI.MOD_ID, "textures/gui/container/thermoregulator.png");

    public ThermoregulatorScreen(ThermoregulatorContainer screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn);
    }

    public void init()
    {
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void extractContents(GuiGraphicsExtractor gui, int mouseX, int mouseY, float partialTicks)
    {
        int leftPos = this.leftPos;
        int topPos = this.topPos;
        gui.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        if (this.menu.isCooling())
        {
            int progress = this.menu.getCoolingFuelProgress();
            gui.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos + 44 + 1, topPos + 25 + 13 - progress, 176, 13 - progress, 14, progress + 1, 256, 256);
        }

        if (this.menu.isHeating())
        {
            int progress = this.menu.getHeatingFuelProgress();
            gui.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos + 116 + 1, topPos + 25 + 13 - progress, 176, 27 - progress, 14, progress + 1, 256, 256);
        }
        super.extractContents(gui, mouseX, mouseY, partialTicks);
    }
}
