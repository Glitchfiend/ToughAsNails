package toughasnails.init;

import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry.EntityRegistration;
import toughasnails.core.ToughAsNails;
import toughasnails.entities.EntityTANArrow;

import com.google.common.collect.Maps;

public class ModEntities
{
    
    public static final Map<Integer, String> idToTANEntityName = Maps.<Integer, String>newLinkedHashMap();

    private static int nextTANEntityId = 1;
    
    public static void init()
    {
        // projectiles
        registerTANEntity(EntityTANArrow.class, "arrow", 80, 3, true);
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