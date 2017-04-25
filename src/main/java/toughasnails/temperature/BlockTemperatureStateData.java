package toughasnails.temperature;

public class BlockTemperatureStateData {
	public String propertyName;
	public String propertyValue;
	public float blockTemperature;
	
	public BlockTemperatureStateData(String propertyName, String propertyValue, float blockTemperature) {
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
		this.blockTemperature = blockTemperature;
	}
	
}
