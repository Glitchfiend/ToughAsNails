/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package glitchcore.util;

import net.minecraft.util.NonNullList;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GFNonNullList<T> extends NonNullList<T>
{
    public static <T> GFNonNullList<T> create()
    {
        return new GFNonNullList<T>();
    }

    public static <T> GFNonNullList<T> withSize(int size, T fill)
    {
        Validate.notNull(fill);
        Object[] aobject = new Object[size];
        Arrays.fill(aobject, fill);
        return new GFNonNullList<T>(Arrays.asList((T[])aobject), fill);
    }

    public static <T> GFNonNullList<T> from(T defaultElement, T... elements)
    {
        return new GFNonNullList(Arrays.asList(elements), defaultElement);
    }

    protected GFNonNullList()
    {
        this(new ArrayList(), null);
    }

    protected GFNonNullList(List<T> delegateIn, @Nullable T listType)
    {
        super(delegateIn, listType);
    }
}
