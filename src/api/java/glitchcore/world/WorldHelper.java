/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package glitchcore.world;

import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class WorldHelper
{
    public static <T> T getOrLoadSavedData(World world, Class<T> clazz, String identifier)
    {
        MapStorage mapStorage = world.getPerWorldStorage();
        return (T)mapStorage.getOrLoadData((Class<? extends WorldSavedData>)clazz, identifier);
    }

    public static <T> void setData(World world, String identifier, T data)
    {
        MapStorage mapStorage = world.getPerWorldStorage();
        mapStorage.setData(identifier, (WorldSavedData)data);
    }
}
