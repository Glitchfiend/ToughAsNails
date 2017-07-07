package toughasnails.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import toughasnails.config.GameplayConfigurationHandler;
import toughasnails.core.ToughAsNails;

public class GuiTANConfig extends GuiConfig
{
    public GuiTANConfig(GuiScreen parentScreen)
    {
        super(parentScreen, GuiTANConfig.getConfigElements(), ToughAsNails.MOD_ID, false, false, "/toughasnails");
    }

    private static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();

        List<IConfigElement> SURVIVAL_SETTINGS = new ConfigElement(GameplayConfigurationHandler.config.getCategory(GameplayConfigurationHandler.SURVIVAL_SETTINGS.toLowerCase())).getChildElements();

        List<IConfigElement> DRINK_SETTINGS = new ConfigElement(
				GameplayConfigurationHandler.config.getCategory(
						GameplayConfigurationHandler.DRINKS.toLowerCase()))
								.getChildElements();
        
        list.add(new DummyConfigElement.DummyCategoryElement(I18n.translateToLocal("config.category.survivalSettings.title"), "config.category.arrowSettings", SURVIVAL_SETTINGS));

        list.add(new DummyConfigElement.DummyCategoryElement(
				I18n.translateToLocal("config.category.drinkSettings.title"),
				"config.category.arrowSettings", DRINK_SETTINGS));
        
        return list;
    }
}