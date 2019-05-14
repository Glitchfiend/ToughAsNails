package toughasnails.core;

import com.google.common.base.Preconditions;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import toughasnails.api.ITANBlock;
import toughasnails.particle.EntitySnowflakeFX;
import toughasnails.particle.TANParticleTypes;
import toughasnails.util.inventory.CreativeTabTAN;

public class ClientProxy extends CommonProxy
{
	public static ResourceLocation particleTexturesLocation = new ResourceLocation("toughasnails:textures/particles/particles.png");
	
    @Override
    public void registerRenderers()
    {
    }

    @Override
    public void registerItemVariantModel(Item item, String name, int metadata)
    {
        Preconditions.checkNotNull(item, "Cannot register models for null item " + name);
        Preconditions.checkArgument(item != Items.AIR, "Cannot register models for air (" + name + ")");

        ModelLoader.registerItemVariants(item, new ResourceLocation("toughasnails:" + name));
        ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(ToughAsNails.MOD_ID + ":" + name, "inventory"));
    }

    @Override
    public void registerBlockSided(Block block)
    {
        if (block instanceof ITANBlock)
        {
            ITANBlock bopBlock = (ITANBlock) block;

            //Register non-rendering properties
            IProperty[] nonRenderingProperties = bopBlock.getNonRenderingProperties();

            if (nonRenderingProperties != null)
            {
                // use a custom state mapper which will ignore the properties specified in the block as being non-rendering
                IStateMapper custom_mapper = (new StateMap.Builder()).ignore(nonRenderingProperties).build();
                ModelLoader.setCustomStateMapper(block, custom_mapper);
            }
        }
    }

    @Override
    public void registerItemSided(Item item)
    {
        // register sub types if there are any
        if (item.getHasSubtypes())
        {
            NonNullList<ItemStack> subItems = NonNullList.create();
            item.getSubItems(CreativeTabTAN.instance, subItems);
            for (ItemStack subItem : subItems)
            {
                String subItemName = item.getUnlocalizedName(subItem);
                subItemName =  subItemName.substring(subItemName.indexOf(".") + 1); // remove 'item.' from the front

                ModelLoader.registerItemVariants(item, new ResourceLocation(ToughAsNails.MOD_ID, subItemName));
                ModelLoader.setCustomModelResourceLocation(item, subItem.getMetadata(), new ModelResourceLocation(ToughAsNails.MOD_ID + ":" + subItemName, "inventory"));
            }
        }
        else
        {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(ToughAsNails.MOD_ID + ":" + item.delegate.name().getResourcePath(), "inventory"));
        }
    }

    @Override
    public void registerFluidBlockRendering(Block block, String name) 
    {
        final ModelResourceLocation fluidLocation = new ModelResourceLocation(ToughAsNails.MOD_ID.toLowerCase() + ":fluids", name);

        // use a custom state mapper which will ignore the LEVEL property
        ModelLoader.setCustomStateMapper(block, new StateMapperBase()
        {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state)
            {
                return fluidLocation;
            }
        });
    }
    
    @Override
    public void spawnParticle(TANParticleTypes type, World parWorld, double x, double y, double z, Object... info)
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        Particle entityFx = null;
        switch (type)
        {
        case SNOWFLAKE:
            entityFx = new EntitySnowflakeFX(parWorld, x, y, z, MathHelper.nextDouble(parWorld.rand, -0.03, 0.03), -0.02D, MathHelper.nextDouble(parWorld.rand, -0.03, 0.03));
            break;
        default:
            break;
        }

        if (entityFx != null) {minecraft.effectRenderer.addEffect(entityFx);}
    }
    
    // 
    // The below method and class is used as part of Forge 1668+'s workaround for render manager being null during preinit
    //

    private static <E extends Entity> void registerEntityRenderer(Class<E> entityClass, Class<? extends Render<E>> renderClass)
    {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, new EntityRenderFactory<E>(renderClass));
    }

    private static class EntityRenderFactory<E extends Entity> implements IRenderFactory<E>
    {
        private Class<? extends Render<E>> renderClass;

        private EntityRenderFactory(Class<? extends Render<E>> renderClass)
        {
            this.renderClass = renderClass;
        }

        @Override
        public Render<E> createRenderFor(RenderManager manager) 
        {
            Render<E> renderer = null;

            try 
            {
                renderer = renderClass.getConstructor(RenderManager.class).newInstance(manager);
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }

            return renderer;
        }
    }
}
