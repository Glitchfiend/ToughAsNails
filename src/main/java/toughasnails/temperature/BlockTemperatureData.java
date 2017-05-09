package toughasnails.temperature;

import net.minecraft.block.state.IBlockState;

public class BlockTemperatureData {

    public IBlockState state;
    public float blockTemperature;
    public String[] useProperties;

    public BlockTemperatureData(IBlockState state, String[] useProperties, float blockTemperature) {
        this.state = state;
        this.useProperties = useProperties;
        this.blockTemperature = blockTemperature;
    }

}
