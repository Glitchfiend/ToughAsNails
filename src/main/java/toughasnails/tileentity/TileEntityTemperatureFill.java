/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.tileentity;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityTemperatureFill extends TileEntity implements ITickable
{
    private TrackedPosition[] trackedPositions;
    
    private int updateTicks;
    
    private int maxSpreadDistance;
    
    public TileEntityTemperatureFill()
    {
        //TODO: This may change, 10 for now
        this.maxSpreadDistance = 10;
        
        //Keep an array of the positions being tracked
        trackedPositions = new TrackedPosition[(int)Math.pow(maxSpreadDistance * 2, 3) + 1];
    }
    
    @Override
    public void update() 
    {
        World world = this.getWorld();
        
        //Don't bother verifying if we aren't tracking any positions
            //Verify every 2 seconds
            if (++updateTicks % 20 == 0)
            {
                // Ensure there has been no changes since last time
                if (!verify())
                {
                    //Refill again
                    fill();
                }
            }

            //TODO: Testing only, Remove this
            //Set all tracked positions to glass
            for (TrackedPosition trackedPosition : trackedPositions)
            {
                if (trackedPosition != null)
                {
                    BlockPos pos = trackedPosition.pos;

                    world.setBlockState(pos, Blocks.glass.getDefaultState());
                }
            }
    }

    public void reset()
    {
        //TODO: Testing only, Remove this
        //Remove any existing glass block indicators
        for (TrackedPosition trackedPosition : trackedPositions)
        {
            if (trackedPosition != null)
            {
                BlockPos pos = trackedPosition.pos;

                //This breaks things
                //this.getWorld().setBlockToAir(pos);
            }
        }
        
        for (int i = 0; i < trackedPositions.length; i++)
        {
            trackedPositions[i] = null;
        }
    }
    
    public void fill()
    {
        reset();
        
        spreadTracked(null, this.getPos(), this.maxSpreadDistance);
    }

    /** Returns true if verified, false if regen is required */
    public boolean verify()
    {
        for (TrackedPosition trackedPosition : this.trackedPositions)
        {
            if (trackedPosition != null)
            {
                UpdateType updateType = trackedPosition.checkForUpdate();

                switch (updateType)
                {
                case REMOVE:
                    trackedPositions[getIndexForPos(trackedPosition.pos)] = null;
                    break;

                case REGEN:
                    return false;

                default:
                    continue;
                }
            }
        }

        return true;
    }
    
    private int getIndexForPos(BlockPos pos)
    {
        BlockPos offset = this.getPos().subtract(pos).add(this.maxSpreadDistance, this.maxSpreadDistance, this.maxSpreadDistance);
        
        return offset.getX() + offset.getZ() * (this.maxSpreadDistance * 2) + offset.getY() * ((int)Math.pow(this.maxSpreadDistance * 2, 2));
    }
    
    private TrackedPosition getTrackedPosition(BlockPos pos)
    {
        return this.trackedPositions[getIndexForPos(pos)];
    }
    
    /** Begins tracking this position or updates its strength. Returns true if changed from before **/
    private boolean setTrackedStrength(BlockPos pos, int strength)
    {
        //Only attempt to update tracking for this position if there is air here.
        //Even positions already being tracked should be filled with air.
        if (!(this.getWorld().isAirBlock(pos) || this.getWorld().getBlockState(pos) == Blocks.glass.getDefaultState())) return false;
        
        TrackedPosition trackedPos = getTrackedPosition(pos);
     
        //This position is already being tracked
        if (trackedPos != null)
        {
            if (trackedPos.strength < strength)
            {
                trackedPos.strength = strength;
                return true;
            }
            
            return false;
        }
        
        trackedPos = new TrackedPosition(pos, strength);
        this.trackedPositions[getIndexForPos(pos)] = trackedPos;
        return true;
    }
    
    /**Source is the direction this was spread fromStrength is the strength of the initial pos, 
     * not what it will spread to its surroundings*/
    private void spreadTracked(EnumFacing source, BlockPos pos, int strength)
    {
        //Don't spread if strength is 0 (or somehow less)
        if (strength > 0)
        {
            for (EnumFacing facing : EnumFacing.values())
            {
                if (facing == source) continue;
                
                BlockPos offsetPos = pos.offset(facing);
                
                //Set suitable adjacent positions as tracked
                if (setTrackedStrength(offsetPos, strength - 1))
                {
                    spreadTracked(facing.getOpposite(), offsetPos, strength - 1);
                }
            }
        }
    }
    
    private void populateTrackedPositionsArray()
    {
        //TODO:
    }
    
    private class TrackedPosition
    {
        public final BlockPos pos;
        private int strength;
        private IBlockState[] adjacentStates = new IBlockState[EnumFacing.values().length];
        
        public TrackedPosition(BlockPos pos, int strength)
        {
            this.pos = pos;
            this.strength = strength;
            
            //Only check adjacents if this position can spread (i.e. its strength is > 0)
            if (strength > 0)
            {
                for (EnumFacing facing : EnumFacing.values())
                {
                    adjacentStates[facing.ordinal()] = TileEntityTemperatureFill.this.getWorld().getBlockState(pos.offset(facing));
                }
            }
        }
        
        public UpdateType checkForUpdate()
        {
            World world = TileEntityTemperatureFill.this.getWorld();
            boolean end = strength > 0;
            
            //Tracked positions should only ever be associated with air
            if (!(world.isAirBlock(pos) || world.getBlockState(pos) == Blocks.glass.getDefaultState()))
            {
                //If this position is the end, remove it from tracking. If not, a full regen is required
                return end ? UpdateType.REMOVE : UpdateType.REGEN;
            }
            
            //End positions don't care about their neighbours
            if (!end)
            {
                for (EnumFacing facing : EnumFacing.values())
                {
                    IBlockState state = world.getBlockState(this.pos.offset(facing));
                    
                    if (state != adjacentStates[facing.ordinal()])
                    {
                        return UpdateType.REGEN;
                    }
                }
            }
            
            return UpdateType.NONE;
        }
    }
    
    private static enum UpdateType
    {
        NONE, REMOVE, REGEN;
    }
}
