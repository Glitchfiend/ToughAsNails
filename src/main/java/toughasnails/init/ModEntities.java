package toughasnails.init;

import java.util.Map;

import com.google.common.collect.Maps;

import glitchcore.entity.EntityHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry.EntityRegistration;
import toughasnails.core.ToughAsNails;
import toughasnails.entities.EntityFreeze;
import toughasnails.entities.projectile.EntityIceball;

public class ModEntities
{
    public static final Map<Integer, String> idToTANEntityName = Maps.<Integer, String>newLinkedHashMap();

    private static int nextTANEntityId = 1;
    
    public static void init()
    {
        // projectiles
        registerTANEntity(EntityIceball.class, "iceball", 64, 10, true);
        
        // mobs
        registerTANEntityWithSpawnEgg(EntityFreeze.class, "freeze", 80, 3, true, 0xECFAF4, 0x439FC3, 3, 1, 3, EnumCreatureType.MONSTER, Biomes.ICE_PLAINS, Biomes.ICE_MOUNTAINS);
    }
    
    // register an entity
    public static int registerTANEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
    {
        int tanEntityId = nextTANEntityId;
        nextTANEntityId++;
        EntityHelper.registerEntity(new ResourceLocation(ToughAsNails.MOD_ID, entityName), entityClass, entityName, tanEntityId, ToughAsNails.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
        idToTANEntityName.put(tanEntityId, entityName);
        return tanEntityId;
    }
    
    // register an entity and in addition create a spawn egg for it
    public static int registerTANEntityWithSpawnEgg(Class<? extends EntityLiving> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, int eggBackgroundColor, int eggForegroundColor, int spawnWeight, int spawnMin, int spawnMax, EnumCreatureType enumCreatureType, Biome... entityBiomes)
    {
        int tanEntityId = registerTANEntity(entityClass, entityName, trackingRange, updateFrequency, sendsVelocityUpdates);
        EntityHelper.registerEgg(new ResourceLocation(ToughAsNails.MOD_ID, entityName), eggBackgroundColor, eggForegroundColor);
        EntityRegistry.addSpawn(entityClass, spawnWeight, spawnMin, spawnMax, enumCreatureType, entityBiomes);
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
    
    
}