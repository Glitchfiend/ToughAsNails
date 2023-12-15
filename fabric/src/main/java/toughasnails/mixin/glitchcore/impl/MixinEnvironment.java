package toughasnails.mixin.glitchcore.impl;

import glitchcore.fabric.util.EnvironmentImpl;
import glitchcore.util.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = Environment.class, remap = false)
public abstract class MixinEnvironment
{
    @Overwrite
    public static boolean isClient()
    {
        return EnvironmentImpl.isClient();
    }
}
