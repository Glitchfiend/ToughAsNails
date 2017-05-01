package toughasnails.temperature;

import net.minecraft.block.state.IBlockState;

public class BlockTemperatureData {

    public IBlockState state;
    public float blockTemperature;
    public String[] use_properties;

    public BlockTemperatureData(IBlockState state, String[] use_properties, float blockTemperature) {
        this.state = state;
        this.use_properties = use_properties;
        this.blockTemperature = blockTemperature;
    }

}
