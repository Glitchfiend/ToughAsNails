package tan.handler;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Locale;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.client.resources.ResourceManagerReloadListener;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class LocalizationHandler implements ResourceManagerReloadListener
{
    @Override
    public void onResourceManagerReload(ResourceManager resourcemanager)
    {
        StringTranslate stringTranslate = ObfuscationReflectionHelper.getPrivateValue(StringTranslate.class, null, new String[] { "instance" });
        Map languageList = ObfuscationReflectionHelper.getPrivateValue(StringTranslate.class, stringTranslate, new String[] { "languageList" });

        String waterName = Block.waterStill.getLocalizedName();

        Iterator iterator = languageList.entrySet().iterator();

        while (iterator.hasNext())
        {
            Map.Entry pair = (Map.Entry)iterator.next();

            String key = (String)pair.getKey();
            String value = (String)pair.getValue();

            String newValue = value;

            if ((key.startsWith("item.") || key.startsWith("tile.")) && newValue.startsWith(waterName))
            {
                newValue = newValue.replace(waterName, stringTranslate.translateKey("phrase.tan.unclean") + " " + waterName);

                pair.setValue(newValue);
            }
        }

        ObfuscationReflectionHelper.setPrivateValue(StringTranslate.class, stringTranslate, languageList, new String[] { "languageList" });
    }
}
