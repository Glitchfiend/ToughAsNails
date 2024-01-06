/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import toughasnails.init.ModConfig;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class AreaFill
{
    private static final int PROXIMITY_RADIUS = ModConfig.temperature.nearHeatCoolProximity;

    public static void fill(Level level, BlockPos pos, PositionChecker checker)
    {
        Set<BlockPos> checked = Sets.newHashSet();
        Queue<BlockPos> queue = new LinkedList();
        BoundingBox bounds = new BoundingBox(pos.getX() - PROXIMITY_RADIUS, pos.getY() - PROXIMITY_RADIUS, pos.getZ() - PROXIMITY_RADIUS, pos.getX() + PROXIMITY_RADIUS, pos.getY() + PROXIMITY_RADIUS, pos.getZ() + PROXIMITY_RADIUS);

        queue.add(pos);
        while (!queue.isEmpty())
        {
            BlockPos posToCheck = queue.poll();

            // Skip already checked positions
            if (checked.contains(posToCheck))
                continue;

            // Positive x is east, negative x is west
            if (level.isEmptyBlock(posToCheck))
            {
                BlockPos westPos = posToCheck;
                BlockPos eastPos = posToCheck.east();

                while (level.isEmptyBlock(westPos) && westPos.getX() >= bounds.minX())
                {
                    checked.add(westPos);
                    expand(queue, checked, checker, bounds, level, westPos);
                    westPos = westPos.west();
                }

                while (level.isEmptyBlock(eastPos) && eastPos.getX() <= bounds.maxX())
                {
                    checked.add(eastPos);
                    expand(queue, checked, checker, bounds, level, eastPos);
                    eastPos = eastPos.east();
                }

                // Add the first non-air blocks (or nothing if still air)
                if (level.isEmptyBlock(westPos)) checked.add(westPos);
                else checkPos(checked, checker, level, westPos);

                if (level.isEmptyBlock(eastPos)) checked.add(eastPos);
                else checkPos(checked, checker, level, eastPos);
            }
            else
            {
                checkPos(checked, checker, level, posToCheck);
            }
        }
    }

    private static void expand(Queue<BlockPos> queue, Set<BlockPos> checked, PositionChecker checker, BoundingBox bounds, Level level, BlockPos pos)
    {
        BlockPos north = pos.north(); // Negative Z
        BlockPos south = pos.south(); // Positive Z
        BlockPos down = pos.below(); // Negative Y
        BlockPos up = pos.above(); // Positive Y

        if (north.getZ() >= bounds.minZ())
        {
            if (level.isEmptyBlock(north)) queue.add(north);
            else checkPos(checked, checker, level, north);
        }

        if (south.getZ() <= bounds.maxZ())
        {
            if (level.isEmptyBlock(south)) queue.add(south);
            else checkPos(checked, checker, level, south);
        }

        if (down.getY() >= bounds.minY())
        {
            if (level.isEmptyBlock(down)) queue.add(down);
            else checkPos(checked, checker, level, down);
        }

        if (up.getY() <= bounds.maxY())
        {
            if (level.isEmptyBlock(up)) queue.add(up);
            else checkPos(checked, checker, level, up);
        }
    }

    private static void checkPos(Set<BlockPos> checked, PositionChecker checker, Level level, BlockPos pos)
    {
        checked.add(pos);
        checker.check(level, pos);
    }

    public interface PositionChecker
    {
        void check(Level level, BlockPos pos);
    }
}
