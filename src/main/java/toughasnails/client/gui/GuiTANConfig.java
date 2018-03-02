package toughasnails.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import org.apache.commons.lang3.text.WordUtils;
import toughasnails.config.ConfigHandler;
import toughasnails.config.GameplayConfig;
import toughasnails.core.ToughAsNails;
import toughasnails.init.ModConfig;

public class GuiTANConfig extends GuiConfig
{
    public GuiTANConfig(GuiScreen parentScreen)
    {
        super(parentScreen, GuiTANConfig.getConfigElements(), ToughAsNails.MOD_ID, false, false, "Tough As Nails");
    }

    private static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();

        for (ConfigHandler handler : ModConfig.configHandlers)
        {
            List<IConfigElement> configChildCategories = Lists.newArrayList();

            for (String categoryName : handler.config.getCategoryNames())
            {
                ConfigCategory category = handler.config.getCategory(categoryName);
                List<IConfigElement> elements = new ConfigElement(category).getChildElements();

                configChildCategories.add(new DummyConfigElement.DummyCategoryElement(WordUtils.capitalize(categoryName), "", elements));
            }

            list.add(new DummyConfigElement.DummyCategoryElement(handler.description, "", configChildCategories));
        }

        return list;
    }
}