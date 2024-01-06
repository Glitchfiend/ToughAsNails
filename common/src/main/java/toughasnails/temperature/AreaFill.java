/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import toughasnails.core.ToughAsNails;
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
            if (checkPassable(checker, level, posToCheck))
            {
                PosAndDepth westPos = posToCheck;
                while (westPos.depth() < maxDepth && checkPassable(checker, level, westPos))
                {
                    checked.add(westPos);
                    expand(queue, checked, checker, level, westPos);
                    westPos = westPos.west();
                }

                checked.add(westPos);
                if (!checkPassable(checker, level, westPos)) checkSolid(checked, checker, level, westPos);

                // Don't spread further or else we'll exceed the max depth
                if (posToCheck.depth() >= maxDepth)
                    continue;

                PosAndDepth eastPos = posToCheck.east();
                while (eastPos.depth() < maxDepth && checkPassable(checker, level, eastPos))
                {
                    checked.add(eastPos);
                    expand(queue, checked, checker, level, eastPos);
                    eastPos = eastPos.east();
                }

                checked.add(eastPos);
                if (!checkPassable(checker, level, eastPos)) checkSolid(checked, checker, level, eastPos);
            }
            else
            {
                checkSolid(checked, checker, level, posToCheck);
            }
        }
    }

    private static void expand(Queue<PosAndDepth> queue, Set<PosAndDepth> checked, PositionChecker checker, Level level, PosAndDepth pos)
    {
        PosAndDepth north = pos.north(); // Negative Z
        PosAndDepth south = pos.south(); // Positive Z
        PosAndDepth down = pos.below(); // Negative Y
        PosAndDepth up = pos.above(); // Positive Y

        if (checkPassable(checker, level, north)) queue.add(north);
        else checkSolid(checked, checker, level, north);

        if (checkPassable(checker, level, south)) queue.add(south);
        else checkSolid(checked, checker, level, south);

        if (checkPassable(checker, level, down)) queue.add(down);
        else checkSolid(checked, checker, level, down);

        if (checkPassable(checker, level, up)) queue.add(up);
        else checkSolid(checked, checker, level, up);
    }

    private static void checkSolid(Set<PosAndDepth> checked, PositionChecker checker, Level level, PosAndDepth pos)
    {
        checked.add(pos);
        checker.onSolid(level, pos);
    }
    
    private static boolean checkPassable(PositionChecker checker, Level level, PosAndDepth pos)
    {
        BlockState state = level.getBlockState(pos.pos());
        boolean passable = state.isAir() || (!state.isSolid() && !state.liquid());
        if (passable) checker.onPassable(level, pos);
        return passable;
    }

    public interface PositionChecker
    {
        void onSolid(Level level, PosAndDepth pos);
        default void onPassable(Level level, PosAndDepth pos) {}
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
