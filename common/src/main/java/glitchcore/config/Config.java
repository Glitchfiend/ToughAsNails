/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.utils.CommentedConfigWrapper;
import com.google.common.base.Predicates;
import glitchcore.core.GlitchCore;

import java.nio.file.Path;
import java.util.function.Predicate;

public abstract class Config extends CommentedConfigWrapper<CommentedFileConfig>
{
    private final Path path;

    protected Config(Path path)
    {
        super(CommentedFileConfig.builder(path).sync().autosave().build());
        this.path = path;

        // Attempt to load from disk
        try { this.config.load(); }
        catch (Exception ignored) {}

        // Read the config file
        this.read();
    }

    public <T> T add(String key, T defaultValue, String comment)
    {
        return this.add(key, defaultValue, comment, Predicates.alwaysTrue());
    }

    public <T> T add(String key, T defaultValue, String comment, Predicate<T> validator)
    {
        var value = config.getOrElse(key, defaultValue);

        // Revert to default if validation fails
        if (!validator.test(value))
        {
            GlitchCore.LOGGER.warn("Invalid value {} for key {}. Reverting to default", value, key);
            value = defaultValue;
        }

        config.set(key, value);
        config.setComment(key, comment);
        return value;
    }

    public <T extends Number & Comparable<T>> T addNumber(String key, T defaultValue, T min, T max, String comment)
    {
        return this.add(key, defaultValue, comment, (v) -> v.compareTo(max) <= 0 && v.compareTo(min) >= 0);
    }

    public abstract void read();
}
