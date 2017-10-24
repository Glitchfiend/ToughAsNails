package toughasnails.season;

public enum WeatherEventType
{
    EVENT_UNKNOWN(0), EVENT_TO_COLD_SEASON(1), EVENT_TO_WARM_SEASON(2), EVENT_START_RAINING(3), EVENT_STOP_RAINING(4);

    private int code;

    WeatherEventType(int code)
    {
        this.code = code;
    }

    public static WeatherEventType fromCode(int code)
    {
        for (WeatherEventType etype : values())
        {
            if (etype.code == code)
                return etype;
        }
        return EVENT_UNKNOWN;
    }

    public int getCode()
    {
        return code;
    }
}
