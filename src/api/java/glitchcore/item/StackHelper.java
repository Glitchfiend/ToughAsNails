/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package glitchcore.item;

import net.minecraft.item.ItemStack;

public class StackHelper
{
    public static boolean isEmpty(ItemStack stack)
    {
        return stack.isEmpty();
    }

    public static void setSize(ItemStack stack, int size)
    {
        stack.setCount(size);
    }

    public static void increment(ItemStack stack, int amount)
    {
        stack.grow(amount);
    }

    public static void decrement(ItemStack stack, int amount)
    {
        stack.shrink(amount);
    }
}
