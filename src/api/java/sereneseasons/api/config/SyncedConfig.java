package sereneseasons.api.config;

import com.google.common.collect.Maps;

import java.util.Map;

public class SyncedConfig
{
    public static Map<String, SyncedConfigEntry> optionsToSync = Maps.newHashMap();

    public static void addOption(ISyncedOption option, String defaultValue)
    {
        optionsToSync.put(option.getOptionName(), new SyncedConfigEntry(defaultValue));
    }

    public static boolean getBooleanValue(ISyncedOption option)
    {
        return Boolean.valueOf(getValue(option));
    }

    public static int getIntValue(ISyncedOption option)
    {
        return Integer.valueOf(getValue(option));
    }

    public static String getValue(ISyncedOption option)
    {
        return optionsToSync.get(option.getOptionName()).value;
    }

    public static void restoreDefaults()
    {
        for (SyncedConfigEntry entry : optionsToSync.values())
        {
            entry.value = entry.defaultValue;
        }
    }

    public static class SyncedConfigEntry
    {
        public String value;
        public final String defaultValue;

        public SyncedConfigEntry(String defaultValue)
        {
            this.defaultValue = defaultValue;
            this.value = defaultValue;
        }
    }
}
