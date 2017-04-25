package toughasnails.temperature;

public class BlockTemperatureData {
	public String blockName;
	public float blockBaseTemperature;
	public BlockTemperatureStateData[] stateTemperatures;
	
	public BlockTemperatureData(String blockName, float blockBaseTemperature, BlockTemperatureStateData[] stateTemperatures) {
		this.blockName = blockName;
		this.blockBaseTemperature = blockBaseTemperature;
		this.stateTemperatures = stateTemperatures;
	}
	
}
