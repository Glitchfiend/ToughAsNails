package toughasnails.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import toughasnails.config.GameplayConfig;
import toughasnails.core.ToughAsNails;
import toughasnails.init.ModConfig;

public class GuiTANConfig extends GuiConfig
{
    public GuiTANConfig(GuiScreen parentScreen)
    {
        super(parentScreen, GuiTANConfig.getConfigElements(), ToughAsNails.MOD_ID, false, false, "/toughasnails");
    }

    private static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();

        List<IConfigElement> SURVIVAL_SETTINGS = new ConfigElement(ModConfig.gameplay.config.getCategory(GameplayConfig.SURVIVAL_SETTINGS.toLowerCase())).getChildElements();

        list.add(new DummyConfigElement.DummyCategoryElement(I18n.translateToLocal("config.category.survivalSettings.title"), "config.category.arrowSettings", SURVIVAL_SETTINGS));

        return list;
    }
}