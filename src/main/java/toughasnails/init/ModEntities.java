package toughasnails.init;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry.EntityRegistration;
import toughasnails.core.ToughAsNails;

public class ModEntities
{
    public static final Map<Integer, String> idToTANEntityName = Maps.<Integer, String>newLinkedHashMap();

    private static int nextTANEntityId = 1;
    
    public static void init()
    {
    }
    
    // register an entity
    public static int registerTANEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
    {
        int tanEntityId = nextTANEntityId;
        nextTANEntityId++;
        EntityRegistry.registerModEntity(new ResourceLocation(ToughAsNails.MOD_ID, entityName), entityClass, entityName, tanEntityId, ToughAsNails.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
        idToTANEntityName.put(tanEntityId, entityName);
        return tanEntityId;
    }
    
    // register an entity and in addition create a spawn egg for it
    public static int registerTANEntityWithSpawnEgg(Class<? extends EntityLiving> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, int eggBackgroundColor, int eggForegroundColor, int spawnWeight, int spawnMin, int spawnMax, EnumCreatureType enumCreatureType, Biome... entityBiomes)
    {
        int tanEntityId = registerTANEntity(entityClass, entityName, trackingRange, updateFrequency, sendsVelocityUpdates);
        EntityRegistry.registerEgg(new ResourceLocation(ToughAsNails.MOD_ID, entityName), eggBackgroundColor, eggForegroundColor);
        addSpawn(entityClass, spawnWeight, spawnMin, spawnMax, enumCreatureType, entityBiomes);
        return tanEntityId;
    }
    
    public static Entity createEntityByID(int tanEntityId, World worldIn)
    {
        Entity entity = null;
        ModContainer mc = FMLCommonHandler.instance().findContainerFor(ToughAsNails.instance);
        EntityRegistration er = EntityRegistry.instance().lookupModSpawn(mc, tanEntityId);
        if (er != null)
        {
            Class<? extends Entity> clazz = er.getEntityClass();
            try
            {
                if (clazz != null)
                {
                    entity = (Entity)clazz.getConstructor(new Class[] {World.class}).newInstance(new Object[] {worldIn});
                }
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }            
        }
        if (entity == null)
        {
            ToughAsNails.logger.warn("Skipping TAN Entity with id " + tanEntityId);
        }        
        return entity;
    }
    
    public static void addSpawn(Class <? extends EntityLiving > entityClass, int weightedProb, int min, int max, EnumCreatureType typeOfCreature, Biome... biomes)
    {
        for (Biome biome : biomes)
        {
            if (biome != null)
            {
                List<SpawnListEntry> spawns = biome.getSpawnableList(typeOfCreature);
    
                boolean found = false;
                for (SpawnListEntry entry : spawns)
                {
                    //Adjusting an existing spawn entry
                    if (entry.entityClass == entityClass)
                    {
                        entry.itemWeight = weightedProb;
                        entry.minGroupCount = min;
                        entry.maxGroupCount = max;
                        found = true;
                        break;
                    }
                }
    
                if (!found)
                    spawns.add(new SpawnListEntry(entityClass, weightedProb, min, max));
            }
        }
    }
}