package toughasnails.temperature;

import toughasnails.temperature.TemperatureScale.TemperatureRange;

public class TemperatureInfo
{
    private int scalePos;
    private TemperatureRange temperatureRange;
    private int rangeIndex;
    private float rangeDelta;
    
    private TemperatureInfo()
    {
        this.scalePos = -1;
        this.temperatureRange = null;
        this.rangeIndex = -1;
        this.rangeDelta = -1F;
    }
    
    public TemperatureInfo(int scalePos)
    {
        this();
        
        this.scalePos = scalePos;
    }
    
    public int getScalePos()
    {
        return scalePos;
    }
    
    public TemperatureRange getTemperatureRange()
    {
        if (temperatureRange == null)
            temperatureRange = TemperatureScale.getTemperatureRange(getScalePos());
        
        return temperatureRange;
    }
    
    public int getRangeIndex(boolean reverseEnd)
    {
        if (rangeIndex == -1)
            rangeIndex = TemperatureScale.getRangeIndex(getScalePos(), reverseEnd);
        
        return rangeIndex;
    }
    
    public float getRangeDelta(boolean reverseEnd)
    {
        if (rangeDelta == -1F)
            rangeDelta = TemperatureScale.getRangeDelta(getScalePos(), reverseEnd);
        
        return rangeDelta;
    }
}
