package toughasnails.mixin.glitchcore.impl;

import glitchcore.util.Environment;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.nio.file.Path;

@Mixin(value = Environment.class, remap = false)
public abstract class MixinEnvironment
{
    @Overwrite
    public static boolean isClient()
    {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Overwrite
    public static Path getConfigPath()
    {
        return FabricLoader.getInstance().getConfigDir();
    }
}
