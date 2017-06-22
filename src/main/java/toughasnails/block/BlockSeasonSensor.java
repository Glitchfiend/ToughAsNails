/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import toughasnails.api.ITANBlock;
import toughasnails.api.TANBlocks;
import toughasnails.api.season.SeasonHelper;
import toughasnails.item.ItemTANBlock;
import toughasnails.season.SeasonTime;
import toughasnails.tileentity.TileEntitySeasonSensor;

public class BlockSeasonSensor extends BlockContainer implements ITANBlock
{
    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
    public static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D);

    // implement ITANBlock
    @Override
    public Class<? extends ItemBlock> getItemClass() { return ItemTANBlock.class; }
    @Override
    public IProperty[] getPresetProperties() { return new IProperty[] {}; }
    @Override
    public IProperty[] getNonRenderingProperties() { return new IProperty[] { POWER }; }
    @Override
    public String getStateName(IBlockState state)
    {
        return type.getName();
    }
    
    private final DetectorType type;
    
    public BlockSeasonSensor(DetectorType type)
    {
        super(Material.WOOD);
        this.type = type;
        this.setHardness(0.2F);
        this.setSoundType(SoundType.WOOD);
        this.setDefaultState( this.blockState.getBaseState().withProperty(POWER, Integer.valueOf(0)) );        
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BOUNDING_BOX;
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return ((Integer)blockState.getValue(POWER)).intValue();
    }

    public void updatePower(World world, BlockPos pos)
    {
        //Seasons currently only work in the overworld
        if (world.provider.getDimension() == 0)
        {
            IBlockState currentState = world.getBlockState(pos);

            int power = 0;
            int startTicks = this.type.ordinal() * SeasonTime.ZERO.getSeasonDuration();
            int endTicks = (this.type.ordinal() + 1) * SeasonTime.ZERO.getSeasonDuration();
            int currentTicks = SeasonHelper.getSeasonData(world).getSeasonCycleTicks();
            
            if (currentTicks >= startTicks && currentTicks <= endTicks)
            {
                float delta = (float)(currentTicks - startTicks) / (float)SeasonTime.ZERO.getSeasonDuration();
                //Delta adjusted so that it peaks at 0.5 (the middle of the month)
                float peak = 2.0F * (-Math.abs(delta - 0.5F) + 0.5F);
                //Add one so at the start of the season it is powered at least a little
                power = (int)Math.min(peak * 15.0F + 1.0F, 15.0F);
            }
            
            //Only update the state if the power level has actually changed
            if (((Integer)currentState.getValue(POWER)).intValue() != power)
            {
                world.setBlockState(pos, currentState.withProperty(POWER, Integer.valueOf(power)), 3);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (player.isAllowEdit())
        {
            if (world.isRemote)
            {
                return true;
            }
            else
            {
                Block nextBlock = TANBlocks.season_sensors[(this.type.ordinal() + 1) % DetectorType.values().length];
                world.setBlockState(pos, nextBlock.getDefaultState().withProperty(POWER, state.getValue(POWER)), 4);
                ((BlockSeasonSensor)nextBlock).updatePower(world, pos);
                return true;
            }
        }
        else
        {
            return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(TANBlocks.season_sensors[0]);
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntitySeasonSensor();
    }

    // map from state to meta and vice verca
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(POWER, meta);
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(POWER);
    }
    
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] { POWER });
    }
    
    public static enum DetectorType implements IStringSerializable
    {
        SPRING, SUMMER, AUTUMN, WINTER;
        @Override
        public String getName()
        {
            return this.name().toLowerCase();
        }
        @Override
        public String toString()
        {
            return this.getName();
        }
    };
}
