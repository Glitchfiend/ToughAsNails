/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import toughasnails.api.TANAPI;
import toughasnails.container.ThermoregulatorContainer;
import toughasnails.container.WaterPurifierContainer;

public class ThermoregulatorScreen extends AbstractContainerScreen<ThermoregulatorContainer>
{
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TANAPI.MOD_ID, "textures/gui/container/thermoregulator.png");

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
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(gui, mouseX, mouseY, partialTicks);
        super.render(gui, mouseX, mouseY, partialTicks);
        this.renderTooltip(gui, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics gui, float partialTicks, int mouseX, int mouseY)
    {
        int leftPos = this.leftPos;
        int topPos = this.topPos;
        gui.blit(TEXTURE, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (this.menu.isCooling())
        {
            int progress = this.menu.getCoolingFuelProgress();
            gui.blit(TEXTURE, leftPos + 44 + 1, topPos + 25 + 13 - progress, 176, 13 - progress, 14, progress + 1);
        }

        if (this.menu.isHeating())
        {
            int progress = this.menu.getHeatingFuelProgress();
            gui.blit(TEXTURE, leftPos + 116 + 1, topPos + 25 + 13 - progress, 176, 27 - progress, 14, progress + 1);
        }
    }
}
