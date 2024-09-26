/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import com.google.common.collect.Sets;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.init.ModConfig;
import toughasnails.init.ModTags;

public class AreaFill
{
    public static void fill(Level level, BlockPos pos, PositionChecker checker)
    {
        fill(level, pos, checker, ModConfig.temperature.nearHeatCoolProximity());
    }

    public static void fill(Level level, BlockPos pos, PositionChecker checker, final int maxDepth)
    {
        Set<FillPos> checked = Sets.newHashSet();
        Queue<FillPos> queue = new LinkedList();

        queue.add(new FillPos(pos, 1, Direction.DOWN));
        while (!queue.isEmpty())
        {
            FillPos posToCheck = queue.poll();

            // Skip already checked positions
            if (checked.contains(posToCheck))
                continue;

            // Positive x is east, negative x is west
            if (checkPassable(checker, level, posToCheck))
            {
                FillPos westPos = posToCheck;
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

                FillPos eastPos = posToCheck.east();
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

    private static void expand(Queue<FillPos> queue, Set<FillPos> checked, PositionChecker checker, Level level, FillPos pos)
    {
        FillPos north = pos.north(); // Negative Z
        FillPos south = pos.south(); // Positive Z
        FillPos down = pos.below(); // Negative Y
        FillPos up = pos.above(); // Positive Y

        if (checkPassable(checker, level, north)) queue.add(north);
        else checkSolid(checked, checker, level, north);

        if (checkPassable(checker, level, south)) queue.add(south);
        else checkSolid(checked, checker, level, south);

        if (checkPassable(checker, level, down)) queue.add(down);
        else checkSolid(checked, checker, level, down);

        if (checkPassable(checker, level, up)) queue.add(up);
        else checkSolid(checked, checker, level, up);
    }

    private static void checkSolid(Set<FillPos> checked, PositionChecker checker, Level level, FillPos pos)
    {
        checked.add(pos);
        checker.onSolid(level, pos);
    }
    
    private static boolean checkPassable(PositionChecker checker, Level level, FillPos pos)
    {
        boolean passable = checker.isPassable(level, pos);
        if (passable) checker.onPassable(level, pos);
        return passable;
    }

    public interface PositionChecker {
        void onSolid(Level level, FillPos pos);

        default void onPassable(Level level, FillPos pos) {
        }

        default boolean isPassable(Level level, FillPos pos)
        {
            BlockState state = level.getBlockState(pos.pos());
            return state.isAir() || state.is(ModTags.Blocks.PASSABLE_BLOCKS) || (!isFlowBlocking(level, pos, state) && !TemperatureHelper.isHeatingBlock(state) && !TemperatureHelper.isCoolingBlock(state));
        }

        default boolean isConfined(Level level, BlockPos pos)
        {
            return pos.getY() < level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos).below().getY();
        }

        default boolean isFlowBlocking(Level level, FillPos pos, BlockState state)
        {
            return state.isFaceSturdy(level, pos.pos(), pos.source()) || state.isFaceSturdy(level, pos.pos(), pos.source().getOpposite());
        }
    }

    public record FillPos(BlockPos pos, int depth, Direction source)
    {
        public FillPos north()
        {
            return new FillPos(pos().north(), depth() + 1, Direction.SOUTH);
        }

        public FillPos south()
        {
            return new FillPos(pos().south(), depth() + 1, Direction.NORTH);
        }

        public FillPos east()
        {
            return new FillPos(pos().east(), depth() + 1, Direction.WEST);
        }

        public FillPos west()
        {
            return new FillPos(pos().west(), depth() + 1, Direction.EAST);
        }

        public FillPos above()
        {
            return new FillPos(pos().above(), depth() + 1, Direction.DOWN);
        }

        public FillPos below()
        {
            return new FillPos(pos().below(), depth() + 1, Direction.UP);
        }
    }
}
