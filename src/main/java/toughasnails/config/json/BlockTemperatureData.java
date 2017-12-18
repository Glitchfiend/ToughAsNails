package toughasnails.config.json;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nonnull;

public class BlockTemperatureData
{
    @SerializedName("state")
    public BlockStatePredicate predicate;
    @SerializedName("temperature")
    public float blockTemperature;

    public BlockTemperatureData(@Nonnull BlockStatePredicate predicate, float blockTemperature)
    {
        this.predicate = predicate;
        this.blockTemperature = blockTemperature;
    }
}
