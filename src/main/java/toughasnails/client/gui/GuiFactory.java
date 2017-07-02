package toughasnails.client.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class GuiFactory implements IModGuiFactory {

    @Override
    public void initialize(Minecraft minecraftInstance) { }

    @Override
    public boolean hasConfigGui() { return true; }
    
    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen)
    {
        return new GuiTANConfig(parentScreen);
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() { return null; }
}