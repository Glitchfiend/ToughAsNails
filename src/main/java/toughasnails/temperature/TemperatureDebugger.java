package toughasnails.temperature;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import net.minecraft.entity.player.EntityPlayerMP;
import toughasnails.handler.PacketHandler;
import toughasnails.network.message.MessageTemperatureDebug;
import toughasnails.network.message.MessageToggleUI;
import toughasnails.util.MapUtils;

import com.google.common.base.Functions;
import com.google.common.collect.Ordering;

public class TemperatureDebugger
{
    public Map<Modifier, Integer>[] modifiers = new LinkedHashMap[ModifierType.values().length];
    
    /**
     * Determines whether or not updates about the various modifiers should be sent to the player.
     * Also determines whether or not they see the debug ui.
     */
    private boolean showGui = false;
    public int debugTimer;
    
    private boolean currentlyMeasuring = false;
    private Modifier currentModifier;
    private int currentLevel = -1;
    
    public TemperatureDebugger()
    {
        for (int i = 0; i < ModifierType.values().length; i++)
        {
            modifiers[i] = new LinkedHashMap();
        }
    }
    
    public void start(Modifier modifier, int startLevel)
    {
        if (!currentlyMeasuring)
        {
            this.currentModifier = modifier;
            this.currentLevel = startLevel;
            this.currentlyMeasuring = true;
        }
        else
        {
            throw new RuntimeException("Already measuring!");
        }
    }
    
    public void end(int endLevel)
    {
        if (currentlyMeasuring)
        {
            int difference = -(currentLevel - endLevel);
            
            if (difference != 0)
            {
                modifiers[currentModifier.modifierType.ordinal()].put(currentModifier, difference);
            }
            
            currentlyMeasuring = false;
        }
        else
        {
            throw new RuntimeException("No measurement has been started!");
        }
    }
    
    /**
     * Sorts the modifier maps and sends them to the client
     */
    public void finalize(EntityPlayerMP player)
    {
        this.debugTimer = 0;
        
        if (this.showGui)
        {
            sortModifiers();
        }
        
        PacketHandler.instance.sendTo(new MessageTemperatureDebug(modifiers), player);
        clearModifiers();
    }
    
    private void sortModifiers()
    {
        for (int i = 0; i < modifiers.length; i++)
        {
            modifiers[i] = MapUtils.sortMapByValue(modifiers[i]);
        }
    }
    
    public void clearModifiers()
    {
        for (int i = 0; i < modifiers.length; i++)
        {
            modifiers[i].clear();
        }
    }
    
    public void setGuiVisible(boolean state, EntityPlayerMP updatePlayer)
    {
        this.showGui = state;
        this.debugTimer = 0;
        
        if (updatePlayer != null)
        {
            PacketHandler.instance.sendTo(new MessageToggleUI(state), updatePlayer);
        }
    }
    
    public void setGuiVisible(boolean state)
    {
        setGuiVisible(state, null);
    }
    
    public boolean isGuiVisible()
    {
        return this.showGui;
    }
    
    public static enum ModifierType
    {
        RATE, TARGET;
    }
    
    public static enum Modifier
    {
        BIOME_HUMIDITY_RATE("Biome Humidity", ModifierType.RATE),
        BIOME_TEMPERATURE_TARGET("Biome Temperature", ModifierType.TARGET),
        NEARBY_ENTITIES_RATE("Nearby Entities", ModifierType.RATE),
        NEARBY_BLOCKS_RATE("Nearby Blocks", ModifierType.RATE),
        NEARBY_ENTITIES_TARGET("Nearby Entities", ModifierType.TARGET),
        NEARBY_BLOCKS_TARGET("Nearby Blocks", ModifierType.TARGET),
        SPRINTING_RATE("Sprinting", ModifierType.RATE),
        HEALTH_RATE("Health", ModifierType.RATE),
        LIGHT_LEVEL_TARGET("Light Level", ModifierType.TARGET),
        HEALTH_TARGET("Health", ModifierType.TARGET),
        SPRINTING_TARGET("Sprinting", ModifierType.TARGET),
        TIME_TARGET("Time", ModifierType.TARGET),
        DAY_RANDOM_TARGET("Day Random", ModifierType.TARGET),
        WET_RATE("Wet", ModifierType.RATE),
        WET_TARGET("Wet", ModifierType.TARGET),
        SNOW_TARGET("Snow", ModifierType.TARGET);
        
        public final String name;
        public final ModifierType modifierType;
        
        private Modifier(String name, ModifierType modifierType)
        {
            this.name = name;
            this.modifierType = modifierType;
        }
    }
}
