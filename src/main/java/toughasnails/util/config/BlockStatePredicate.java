/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.util.config;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.gson.annotations.SerializedName;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import toughasnails.util.BlockStateUtils;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class BlockStatePredicate implements Predicate<IBlockState>
{
    @SerializedName("block")
    private String blockRegistryName;
    private Map<String, String> properties;

    public BlockStatePredicate(ResourceLocation blockRegistryLoc, Map<IProperty<?>, Comparable<?>> properties)
    {
        this.blockRegistryName = blockRegistryLoc.toString();
        this.properties = Maps.newHashMap();

        for (Map.Entry<IProperty<?>, Comparable<?>> entry : properties.entrySet())
        {
            this.properties.put(entry.getKey().getName(), entry.getValue().toString());
        }
    }

    public BlockStatePredicate(Block block)
    {
        this(block.getRegistryName(), Maps.newHashMap());
    }

    public BlockStatePredicate(IBlockState state, Set<IProperty<?>> usedProperties)
    {
        this(state.getBlock().getRegistryName(), generatePropertyMap(state, usedProperties));
    }

    @Override
    public boolean apply(@Nullable IBlockState input)
    {
        IBlockState defaultState = input.getBlock().getDefaultState();

        for (IProperty<?> property : defaultState.getPropertyKeys())
        {
            String propName = property.getName();

            if (properties.containsKey(propName))
            {
                String reqValueStr = properties.get(propName);
                Comparable<?> reqValue = BlockStateUtils.getPropertyValueByName(input, property, reqValueStr);

                if (!reqValueStr.equals("*") && (reqValue == null || input.getValue(property) != reqValue))
                {
                    return false;
                }
            }
            // If missing key for this property, allow it to have any value
        }

        return true;
    }

    public Block getBlock()
    {
        return Block.getBlockFromName(blockRegistryName);
    }

    private static Map<IProperty<?>, Comparable<?>> generatePropertyMap(IBlockState state, Set<IProperty<?>> usedProperties)
    {
        Map<IProperty<?>, Comparable<?>> propertyMap = Maps.newHashMap();

        for (IProperty<?> property : usedProperties)
        {
            propertyMap.put(property, state.getValue(property));
        }

        return propertyMap;
    }
}
