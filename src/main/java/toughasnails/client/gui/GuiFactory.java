package toughasnails.client.gui;

import java.util.Set;

import glitchcore.gui.GFGuiFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class GuiFactory extends GFGuiFactory
{
    @Override
    public Class<? extends GuiScreen> getGuiClass()
    {
        return GuiTANConfig.class;
    }
}