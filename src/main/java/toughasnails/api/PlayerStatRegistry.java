package toughasnails.api;

import java.util.HashMap;

import com.google.common.collect.ImmutableMap;

public class PlayerStatRegistry
{
    private static HashMap<String, Class<? extends PlayerStat>> playerStats = new HashMap<String, Class<? extends PlayerStat>>();
    
    public static void addStat(String identifier, Class<? extends PlayerStat> statClass)
    {
        if (identifier == null)
        {
            throw new RuntimeException("Stat identifier cannot be null!");
        }
        else if (playerStats.containsKey(identifier))
        {
            throw new RuntimeException("Stat with identifier " + identifier + " already exists!");
        } 

        playerStats.put(identifier, statClass);
    }
    
    public static Class<? extends PlayerStat> getStatClass(String identifier)
    {
        return playerStats.get(identifier);
    }

    public static PlayerStat createStat(String identifier)
    {
        try
        {
            return getStatClass(identifier).getConstructor(String.class).newInstance(identifier);
        } 
        catch (Exception e)
        {
            throw new RuntimeException("Stat with identifier " + identifier + " must have id constructor!");
        }
    }
    
    public static ImmutableMap<String, Class<? extends PlayerStat>> getStatMap()
    {
        return ImmutableMap.copyOf(playerStats);
    }
}
