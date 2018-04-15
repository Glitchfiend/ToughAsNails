package toughasnails.config.json;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nonnull;
import java.util.List;

public class ArmorTemperatureData
{
    @SerializedName("names")
    public List<String> names;
    @SerializedName("modifier")
    public int modifier;

    public ArmorTemperatureData(@Nonnull List<String> names, int modifier)
    {
        this.names = names;
        this.modifier = modifier;
    }
}
