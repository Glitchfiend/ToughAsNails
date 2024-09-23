package toughasnails.forge.mixin;

import toughasnails.glitch.util.Environment;
import java.nio.file.Path;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = Environment.class, remap = false)
public abstract class ForgeEnvironmentMixin
{
  @Overwrite
  public static boolean isClient()
  {
    return FMLEnvironment.dist.isClient();
  }

  @Overwrite
  public static Path getConfigPath()
  {
    return FMLPaths.CONFIGDIR.get();
  }
}