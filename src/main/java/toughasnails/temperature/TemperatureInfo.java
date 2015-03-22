package toughasnails.temperature;

import toughasnails.temperature.TemperatureScale.TemperatureRange;

public class TemperatureInfo
{
    private int scalePos;
    private TemperatureRange temperatureRange;
    private int relativeScalePos;
    private float relativeScaleDelta;
    
    private TemperatureInfo()
    {
        this.scalePos = -1;
        this.temperatureRange = null;
        this.relativeScalePos = -1;
        this.relativeScaleDelta = -1F;
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
    
    public int getRelativeScalePos()
    {
        if (relativeScalePos == -1)
            relativeScalePos = TemperatureScale.getRelativeScalePos(getScalePos());
        
        return relativeScalePos;
    }
    
    public float getRelativeScaleDelta()
    {
        if (relativeScaleDelta == -1F)
            relativeScaleDelta = (float)getRelativeScalePos() / (float)getTemperatureRange().getRangeSize();
        
        return relativeScaleDelta;
    }
}
