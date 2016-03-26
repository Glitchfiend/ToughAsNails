package toughasnails.api.temperature;

public class TemperatureScale
{
    private static int scaleTotal = generateTotalScale();
    private static int[] rangeStarts = generateRangeStarts();
    
    /**Get the temperature range this position in the overall temperature scale is
     * located within*/
    public static TemperatureRange getTemperatureRange(int scalePos)
    {
        //Ensure the scale position is within the allowed values
        if (scalePos < 0 || scalePos > scaleTotal)
        {
            return null;
        }
        
        for (TemperatureRange range : TemperatureRange.values())
        {
            if (scalePos <= rangeStarts[range.ordinal()] + range.rangeSize - 1)
            {
                return range;
            }
        }

        throw new RuntimeException("Could not find range for value " + scalePos + ". This should never happen!");
    }

    /**Returns an index within a range, given a position in the overall temperature scale.
     * When reversed, 1.0 values are when the input position is closer to 0 in the overall temperature scale*/
    public static int getRangeIndex(int scalePos, boolean reverseEnd)
    {
        TemperatureRange temperatureRange = getTemperatureRange(scalePos);

        return Math.abs((reverseEnd ? (temperatureRange.getRangeSize() - 1) : 0) - (scalePos - rangeStarts[temperatureRange.ordinal()]));
    }
    
    /**Returns on a scale of 0.0F to 1.0F the location of a temperature within the current range
     * When reversed, 1.0 values are when the input position is closer to 0 in the overall temperature scale*/
    public static float getRangeDelta(int scalePos, boolean reverseEnd)
    {
        return (float)(getRangeIndex(scalePos, reverseEnd) + 1) / (float)getTemperatureRange(scalePos).getRangeSize();
    }
    
    public static boolean isScalePosInRange(int scalePos, TemperatureRange startRange, TemperatureRange endRange)
    {
        return scalePos >= rangeStarts[startRange.ordinal()] && scalePos <= (rangeStarts[endRange.ordinal()] + endRange.rangeSize - 1);
    }
    
    public static boolean isScalePosInRange(int scalePos, TemperatureRange range)
    {
        return isScalePosInRange(scalePos, range, range);
    }
    
    /**Returns the position in the overall temperature scale of this range*/
    public static int getRangeStart(TemperatureRange range)
    {
        return rangeStarts[range.ordinal()];
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
        
        return totalRange - 1;
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
            else generatedStarts[index] = 0;
        }
        
        return generatedStarts;
    }
    
    public static enum TemperatureRange
    {
        ICY(6),
        COOL(5),
        MILD(4),
        WARM(5),
        HOT(6);
        
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
