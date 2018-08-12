package toughasnails.config.json;

import com.google.gson.annotations.SerializedName;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import toughasnails.core.ToughAsNails;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArmorTemperatureData
{
    @SerializedName("names")
    public List<String> names;
    @SerializedName("modifier")
    public int modifier;
    @SerializedName("nbts")
    public List<String> nbts;

    private List<NBTTagCompound> nbtTagCompounds;

    public ArmorTemperatureData(@Nonnull List<String> names, int modifier, @Nonnull List<String> nbts)
    {
        this.names = names;
        this.modifier = modifier;
        this.nbts = nbts;
    }

    public ArmorTemperatureData(@Nonnull List<String> names, int modifier) {
        this(names, modifier, null);
    }

    public List<NBTTagCompound> getNBTTagCompounds() {
        if (nbtTagCompounds == null) {
            ArrayList<NBTTagCompound> compound = new ArrayList<>();

            if (nbts == null) {
                for (int i = 0; i < 4; i++) compound.add(null);
            } else {
                nbts.forEach(nbt -> {
                    try {
                        compound.add(JsonToNBT.getTagFromJson(nbt));
                    } catch (NBTException e) {
                        ToughAsNails.logger.error("Failed to parse NBT tag for ArmorTemperatureData: adding null NBT.  " +
                                "This is likely an error. (First armor name: " + names.get(0) + ")");
                    }
                });
            }
            nbtTagCompounds = Collections.unmodifiableList(compound);
        }
        return nbtTagCompounds;
    }
}
