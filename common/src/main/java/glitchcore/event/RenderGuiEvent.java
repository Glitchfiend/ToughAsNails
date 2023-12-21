/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.event;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;

public abstract class RenderGuiEvent extends Event
{
    private final Type type;
    private final Gui gui;
    private final GuiGraphics guiGraphics;
    private final float partialTicks;
    private final int screenWidth;
    private final int screenHeight;

    private int rowTop;

    public RenderGuiEvent(Type type, Gui gui, GuiGraphics guiGraphics, float partialTicks, int screenWidth, int screenHeight, int rowTop)
    {
        this.type = type;
        this.gui = gui;
        this.guiGraphics = guiGraphics;
        this.partialTicks = partialTicks;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.rowTop = rowTop;
    }

    public Type getType()
    {
        return this.type;
    }

    public Gui getGui()
    {
        return this.gui;
    }

    public GuiGraphics getGuiGraphics()
    {
        return this.guiGraphics;
    }

    public float getPartialTicks()
    {
        return this.partialTicks;
    }

    public int getScreenWidth()
    {
        return this.screenWidth;
    }

    public int getScreenHeight()
    {
        return this.screenHeight;
    }

    public int getRowTop()
    {
        if (this.rowTop == -1)
            throw new UnsupportedOperationException("Row top is not implemented");

        return this.rowTop;
    }

    public void setRowTop(int value)
    {
        if (this.rowTop == -1)
            throw new UnsupportedOperationException("Row top is not implemented");

        this.rowTop = value;
    }

    public static class Pre extends RenderGuiEvent
    {
        public Pre(Type type, Gui gui, GuiGraphics guiGraphics, float partialTicks, int screenWidth, int screenHeight, int rowTop)
        {
            super(type, gui, guiGraphics, partialTicks, screenWidth, screenHeight, rowTop);
        }

        public Pre(Type type, Gui gui, GuiGraphics guiGraphics, float partialTicks, int screenWidth, int screenHeight)
        {
            this(type, gui, guiGraphics, partialTicks, screenWidth, screenHeight, -1);
        }
    }

    public enum Type
    {
        AIR,
        FOOD,
        FROSTBITE;
    }
}
