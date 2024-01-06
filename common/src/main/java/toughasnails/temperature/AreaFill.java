/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import toughasnails.init.ModConfig;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class AreaFill
{
    public static void fill(Level level, BlockPos pos, PositionChecker checker)
    {
        Set<PosAndDepth> checked = Sets.newHashSet();
        Queue<PosAndDepth> queue = new LinkedList();

        final int maxDepth = ModConfig.temperature.nearHeatCoolProximity;

        queue.add(new PosAndDepth(pos, 1));
        while (!queue.isEmpty())
        {
            PosAndDepth posToCheck = queue.poll();

            // Skip already checked positions
            if (checked.contains(posToCheck))
                continue;

            // Positive x is east, negative x is west
            if (isPassable(level, posToCheck))
            {
                PosAndDepth westPos = posToCheck;
                PosAndDepth eastPos = posToCheck.east();

                while (isPassable(level, westPos))
                {
                    checked.add(westPos);

                    if (westPos.depth() < maxDepth)
                    {
                        expand(queue, checked, checker, level, westPos);
                        westPos = westPos.west();
                    }
                    else break;
                }

                while (isPassable(level, eastPos))
                {
                    checked.add(eastPos);

                    if (eastPos.depth() < maxDepth)
                    {
                        expand(queue, checked, checker, level, eastPos);
                        eastPos = eastPos.east();
                    }
                    else break;
                }

                // Add the first non-air blocks (or nothing if still air)
                if (!isPassable(level, westPos)) checkPos(checked, checker, level, westPos);
                if (!isPassable(level, eastPos)) checkPos(checked, checker, level, eastPos);
            }
            else
            {
                checkPos(checked, checker, level, posToCheck);
            }
        }
    }

    private static void expand(Queue<PosAndDepth> queue, Set<PosAndDepth> checked, PositionChecker checker, Level level, PosAndDepth pos)
    {
        PosAndDepth north = pos.north(); // Negative Z
        PosAndDepth south = pos.south(); // Positive Z
        PosAndDepth down = pos.below(); // Negative Y
        PosAndDepth up = pos.above(); // Positive Y

        if (isPassable(level, north)) queue.add(north);
        else checkPos(checked, checker, level, north);

        if (isPassable(level, south)) queue.add(south);
        else checkPos(checked, checker, level, south);

        if (isPassable(level, down)) queue.add(down);
        else checkPos(checked, checker, level, down);

        if (isPassable(level, up)) queue.add(up);
        else checkPos(checked, checker, level, up);
    }

    private static void checkPos(Set<PosAndDepth> checked, PositionChecker checker, Level level, PosAndDepth pos)
    {
        checked.add(pos);
        checker.check(level, pos);
    }
    
    private static boolean isPassable(Level level, PosAndDepth pos)
    {
        BlockState state = level.getBlockState(pos.pos());
        return state.isAir() || !state.isSolid();
    }

    public interface PositionChecker
    {
        void check(Level level, PosAndDepth pos);
    }

    public record PosAndDepth(BlockPos pos, int depth)
    {
        public PosAndDepth north()
        {
            return new PosAndDepth(pos().north(), depth() + 1);
        }

        public PosAndDepth south()
        {
            return new PosAndDepth(pos().south(), depth() + 1);
        }

        public PosAndDepth east()
        {
            return new PosAndDepth(pos().east(), depth() + 1);
        }

        public PosAndDepth west()
        {
            return new PosAndDepth(pos().west(), depth() + 1);
        }

        public PosAndDepth above()
        {
            return new PosAndDepth(pos().above(), depth() + 1);
        }

        public PosAndDepth below()
        {
            return new PosAndDepth(pos().below(), depth() + 1);
        }
    }
}
