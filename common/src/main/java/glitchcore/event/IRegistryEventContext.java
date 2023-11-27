package glitchcore.event;

import net.minecraft.resources.ResourceLocation;

public interface IRegistryEventContext<T> extends IEventContext
{
    T register(ResourceLocation location, T value);
}
