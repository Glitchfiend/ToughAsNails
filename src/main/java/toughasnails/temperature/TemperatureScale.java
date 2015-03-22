package toughasnails.temperature;

public class TemperatureScale
{
    private static int scaleTotal = generateTotalScale();
    private static int[] rangeStarts = generateRangeStarts();
    
    public static TemperatureRange getTemperatureRange(int scalePos)
    {
        if (scalePos < 0 || scalePos > scaleTotal)
        {
            return null;
        }
        
        TemperatureRange currentRange = TemperatureRange.ICY;
        
        for (int index = 0; index < TemperatureRange.values().length; index++)
        {
           currentRange = TemperatureRange.values()[index];
            
            if (scalePos <= rangeStarts[currentRange.ordinal()] + currentRange.rangeSize)
            {
                break;
            }
        }
        
        return currentRange;
    }

    public static int getRelativeScalePos(int scalePos)
    {
        TemperatureRange temperatureRange = getTemperatureRange(scalePos);
        
        return (temperatureRange.rangeSize - 1) - (Math.abs(scalePos - (rangeStarts[temperatureRange.ordinal()] + temperatureRange.rangeSize)));
    }
    
    public static int getScaleTotal()
    {
        return scaleTotal;
    }
    
    private static int generateTotalScale()
    {
        int totalRange = 0;
        
        for (TemperatureRange range : TemperatureRange.values())
        {
            totalRange += range.getRangeSize();
        }
        
        return totalRange;
    }
    
    private static int[] generateRangeStarts()
    {
        int[] generatedStarts = new int[TemperatureRange.values().length];
        
        for (int index = 0; index < TemperatureRange.values().length; index++)
        {
            if (index > 0)
            {
                TemperatureRange previousRange = TemperatureRange.values()[index - 1];
                
                generatedStarts[index] = generatedStarts[previousRange.ordinal()] + previousRange.rangeSize;
            }
            else generatedStarts[index] = -1;
        }
        
        return generatedStarts;
    }
    
    public static enum TemperatureRange
    {
        ICY(16),
        COOL(8),
        MILD(4),
        WARM(8),
        HOT(16);
        
        private int rangeSize;
        
        private TemperatureRange(int rangeSize)
        {
            this.rangeSize = rangeSize;
        }
        
        public int getRangeSize()
        {
            return this.rangeSize;
        }
    }
}
