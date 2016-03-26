package toughasnails.api.temperature;

import toughasnails.api.temperature.TemperatureScale.TemperatureRange;

public class Temperature
{
    private int rawValue;
    private TemperatureRange temperatureRange;
    
    private int rangeIndex;
    private float rangeDelta;
    
    public Temperature(int scalePos)
    {
        this.rawValue = scalePos;
        this.temperatureRange = TemperatureScale.getTemperatureRange(getRawValue());
        this.rangeIndex = -1;
        this.rangeDelta = -1F;
    }
    
    /** The raw value of this temperature on a scale*/
    public int getRawValue()
    {
        return rawValue;
    }
    
    /** The range in which this temperature belongs */
    public TemperatureRange getRange()
    {
        return temperatureRange;
    }
    
    public int getRangeIndex(boolean reverseEnd)
    {
        if (rangeIndex == -1)
            return TemperatureScale.getRangeIndex(getRawValue(), reverseEnd);
        
        return rangeIndex;
    }
    
    public float getRangeDelta(boolean reverseEnd)
    {
        if (rangeDelta == -1F)
            rangeDelta = TemperatureScale.getRangeDelta(getRawValue(), reverseEnd);
        
        return rangeDelta;
    }
}
