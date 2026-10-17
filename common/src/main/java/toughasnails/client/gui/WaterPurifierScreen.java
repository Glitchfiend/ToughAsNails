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
import toughasnails.container.WaterPurifierMenu;

public class WaterPurifierScreen extends AbstractContainerScreen<WaterPurifierMenu>
{
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(TANAPI.MOD_ID, "textures/gui/container/water_purifier.png");

    public WaterPurifierScreen(WaterPurifierMenu screenContainer, Inventory inv, Component titleIn)
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

        if (this.menu.isFiltering())
        {
            int filterProgress = this.menu.getFilterProgress();

            // NOTE: This is moved over right by 1 compared to the furnace
            // stack, x, y, u, v, width, height
            gui.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos + 56 + 1, topPos + 36 + 13 - filterProgress, 176, 13 - filterProgress, 14, filterProgress + 1, 256, 256);
        }

        int purifyProgress = this.menu.getPurifyProgress();
        gui.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos + 79, topPos + 34, 176, 14, purifyProgress + 1, 16, 256, 256);
        super.extractContents(gui, mouseX, mouseY, partialTicks);
    }
}
