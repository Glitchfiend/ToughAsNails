/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package glitchcore.gui;

import glitchcore.core.GlitchCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.Set;

public abstract class GFGuiFactory implements IModGuiFactory
{
    public abstract Class<? extends GuiScreen> getGuiClass();

    @Override
    public void initialize(Minecraft minecraftInstance) {}

    @Override
    public boolean hasConfigGui() { return true; }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen)
    {
        GuiScreen configGui = null;

        try
        {
            configGui = getGuiClass().getConstructor(GuiScreen.class).newInstance(parentScreen);
        }
        catch (Exception e)
        {
            GlitchCore.logger.error("An error occurred creating the config gui", e);
        }

        return configGui;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() { return null; }
}
