package toughasnails.api.stat.capability;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/** Provides instances of our capability when requested where suitable*/
public class CapabilityProvider<C> implements ICapabilityProvider
{
    /** The capability this is for */
    private final Capability<C> capability;
    private final C instance;
    
    public CapabilityProvider(Capability<C> capability)
    {
        this.capability = capability;
        this.instance = capability.getDefaultInstance();
    }
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability != null && capability == this.capability;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        return capability != null && capability == this.capability ? (T)this.instance : null;
    }
}
