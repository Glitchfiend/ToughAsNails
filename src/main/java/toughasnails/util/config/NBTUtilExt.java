package toughasnails.util.config;

import net.minecraft.nbt.NBTTagCompound;

public final class NBTUtilExt {

    public static boolean areNBTsEqualOrNull(NBTTagCompound a, NBTTagCompound b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }
}
