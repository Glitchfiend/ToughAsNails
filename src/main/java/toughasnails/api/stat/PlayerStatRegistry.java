package toughasnails.api.stat;

import java.util.HashMap;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import toughasnails.api.stat.capability.CapabilityProvider;

public class PlayerStatRegistry
{
    private static HashMap<String, Class<? extends StatHandlerBase>> playerStats = Maps.newHashMap();
    private static HashMap<String, Capability<? extends IPlayerStat>> statCapabilities = Maps.newHashMap();
    
    public static <T> void addStat(Class<T> capabilityClass, IStorage<T> storage, Class<? extends StatHandlerBase> implementationClass)
    {
        String identifier = capabilityClass.getName().intern();
        
        if (identifier == null)
        {
            throw new RuntimeException("Stat identifier cannot be null!");
        }
        else if (playerStats.containsKey(identifier))
        {
            throw new RuntimeException("Stat with identifier " + identifier + " already exists!");
        } 

        try
        {
            CapabilityManager.INSTANCE.register(capabilityClass, storage, (Class<? extends T>)implementationClass);
        }
        catch (ClassCastException e)
        {
            throw new IllegalArgumentException("Player stat must implement capability class!");
        }
        
        playerStats.put(identifier, implementationClass);
    }
    
    public static void registerCapability(Capability<? extends IPlayerStat> capability)
    {
        statCapabilities.put(capability.getName(), capability);
    }

    public static CapabilityProvider<?> createCapabilityProvider(String identifier)
    {
        return new CapabilityProvider(statCapabilities.get(identifier));
    }
    
    public static Capability<?> getCapability(String identifier)
    {
        return statCapabilities.get(identifier);
    }
    
    public static ImmutableMap<String, Capability<? extends IPlayerStat>> getCapabilityMap()
    {
        return ImmutableMap.copyOf(statCapabilities);
    }
}
