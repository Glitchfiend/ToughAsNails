package toughasnails.init;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
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
	public static final Map<Integer, EntityEggInfo> entityEggs = Maps.<Integer, EntityEggInfo>newLinkedHashMap();
    public static final Map<Integer, String> idToTANEntityName = Maps.<Integer, String>newLinkedHashMap();

    private static int nextTANEntityId = 1;
    
    public static void init()
    {
        // projectiles
        registerTANEntity(EntityIceball.class, "iceball", 80, 3, true);
        
        // mobs
        registerTANEntityWithSpawnEgg(EntityFreeze.class, "freeze", 80, 3, true, 0xECFAF4, 0x439FC3, Biomes.ICE_PLAINS, Biomes.ICE_MOUNTAINS);
    }
    
    // register an entity
    public static int registerTANEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
    {
        int tanEntityId = nextTANEntityId;
        nextTANEntityId++;
        EntityRegistry.registerModEntity(entityClass, entityName, tanEntityId, ToughAsNails.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
        idToTANEntityName.put(tanEntityId, entityName);
        return tanEntityId;
    }
    
    // register an entity and in addition create a spawn egg for it
    public static int registerTANEntityWithSpawnEgg(Class<? extends EntityLiving> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, int eggBackgroundColor, int eggForegroundColor, Biome... entityBiomes)
    {
        int tanEntityId = registerTANEntity(entityClass, entityName, trackingRange, updateFrequency, sendsVelocityUpdates);
        entityEggs.put(Integer.valueOf(tanEntityId), new EntityList.EntityEggInfo(entityName, eggBackgroundColor, eggForegroundColor));
        EntityRegistry.addSpawn(entityClass, 3, 1, 3, EnumCreatureType.MONSTER, entityBiomes);
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