/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import toughasnails.container.WaterPurifierContainer;
import toughasnails.core.ToughAsNails;

@OnlyIn(Dist.CLIENT)
public class WaterPurifierScreen extends ContainerScreen<WaterPurifierContainer>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(ToughAsNails.MOD_ID, "textures/gui/container/water_purifier.png");

    public WaterPurifierScreen(WaterPurifierContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
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
    public void tick()
    {
        super.tick();
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(TEXTURE);
        int leftPos = this.leftPos;
        int topPos = this.topPos;
        this.blit(stack, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (this.menu.isFiltering())
        {
            int filterProgress = this.menu.getFilterProgress();

            // NOTE: This is moved over right by 1 compared to the furnace
            this.blit(stack, leftPos + 56 + 1, topPos + 36 + 12 - filterProgress, 176, 12 - filterProgress, 14, filterProgress + 1);
        }

        int purifyProgress = this.menu.getPurifyProgress();
        this.blit(stack, leftPos + 79, topPos + 34, 176, 14, purifyProgress + 1, 16);
    }
}
