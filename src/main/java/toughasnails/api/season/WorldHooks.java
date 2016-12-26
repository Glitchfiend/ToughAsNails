package toughasnails.api.season;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldHooks
{
    /**
     * An override of {@link World#canSnowAt(BlockPos, boolean)}
     */
    public static boolean canSnowAtInSeason(World world, BlockPos pos, boolean checkLight, Season season)
    {
        try
        {
            return (Boolean)Class.forName("toughasnails.season.SeasonASMHelper").getMethod("canSnowAtInSeason", World.class, BlockPos.class, Boolean.class, Season.class).invoke(null, world, pos, checkLight, season);
        }
        catch (Exception e)
        {
            throw new RuntimeException("An error occurred calling canSnowAtInSeason", e);
        }
    }

    /**
     * An override of {@link World#canBlockFreeze(BlockPos, boolean)}
     */
    public static boolean canBlockFreezeInSeason(World world, BlockPos pos, boolean noWaterAdj, Season season)
    {
        try
        {
            return (Boolean)Class.forName("toughasnails.season.SeasonASMHelper").getMethod("canBlockFreezeInSeason", World.class, BlockPos.class, Boolean.class, Season.class).invoke(null, world, pos, noWaterAdj, season);
        }
        catch (Exception e)
        {
            throw new RuntimeException("An error occurred calling canBlockFreezeInSeason", e);
        }
    }

    /**
     * An override of {@link World#isRainingAt(BlockPos)}
     */
    public static boolean isRainingAtInSeason(World world, BlockPos pos, Season season)
    {
        try
        {
            return (Boolean)Class.forName("toughasnails.season.SeasonASMHelper").getMethod("isRainingAtInSeason", World.class, BlockPos.class, Season.class).invoke(null, world, pos, season);
        }
        catch (Exception e)
        {
            throw new RuntimeException("An error occurred calling isRainingAtInSeason", e);
        }
    }
}
