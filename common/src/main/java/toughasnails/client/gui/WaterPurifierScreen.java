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
import toughasnails.container.WaterPurifierContainer;

public class WaterPurifierScreen extends AbstractContainerScreen<WaterPurifierContainer>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(TANAPI.MOD_ID, "textures/gui/container/water_purifier.png");

    public WaterPurifierScreen(WaterPurifierContainer screenContainer, Inventory inv, Component titleIn)
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
        this.renderBackground(gui);
        super.render(gui, mouseX, mouseY, partialTicks);
        this.renderTooltip(gui, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics gui, float partialTicks, int mouseX, int mouseY)
    {
        int leftPos = this.leftPos;
        int topPos = this.topPos;
        gui.blit(TEXTURE, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (this.menu.isFiltering())
        {
            int filterProgress = this.menu.getFilterProgress();

            // NOTE: This is moved over right by 1 compared to the furnace
            // stack, x, y, u, v, width, height
            gui.blit(TEXTURE, leftPos + 56 + 1, topPos + 36 + 13 - filterProgress, 176, 13 - filterProgress, 14, filterProgress + 1);
        }

        int purifyProgress = this.menu.getPurifyProgress();
        gui.blit(TEXTURE, leftPos + 79, topPos + 34, 176, 14, purifyProgress + 1, 16);
    }
}
