package toughasnails.api.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

public class SyncedConfig {
	public static Map<String, SyncedConfigEntry> optionsToSync = Maps
			.newHashMap();

	public static void addOption(ISyncedOption option, String defaultValue) {
		optionsToSync.put(option.getOptionName(),
				new SyncedConfigEntry(defaultValue));
	}

	public static boolean getBooleanValue(ISyncedOption option) {
		return Boolean.valueOf(optionsToSync.get(option.getOptionName()).value);
	}

	public static int getIntegerValue(ISyncedOption option) {
		return Integer.valueOf(optionsToSync.get(option.getOptionName()).value);
	}

	public static float getFloatValue(ISyncedOption option) {
		return Float.valueOf(optionsToSync.get(option.getOptionName()).value);
	}

	public static List<String> getListValue(ISyncedOption option) {
		SyncedConfigEntry value = optionsToSync.get(option.getOptionName());
		String rawList = value.value;
		List<String> result = new ArrayList<String>();
		for (String drinkEntry : rawList.split(",")) {
			result.add(drinkEntry);
		}
		return result;
	}

	public static void restoreDefaults() {
		for (SyncedConfigEntry entry : optionsToSync.values()) {
			entry.value = entry.defaultValue;
		}
	}

	public static class SyncedConfigEntry {
		public String value;
		public final String defaultValue;

		public SyncedConfigEntry(String defaultValue) {
			this.defaultValue = defaultValue;
			this.value = defaultValue;
		}
	}
}
