package toughasnails.season;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import toughasnails.util.IDataStorable;

public class WeatherJournalEvent implements IDataStorable
{
    private long timeStamp;
    private WeatherEventType eventType;

    public WeatherJournalEvent()
    {
    }

    public WeatherJournalEvent(long timeStamp, WeatherEventType eventType)
    {
        this.timeStamp = timeStamp;
        this.eventType = eventType;
    }

    public long getTimeStamp()
    {
        return timeStamp;
    }

    public WeatherEventType getEventType()
    {
        return eventType;
    }

    @Override
    public void writeToStream(ObjectOutputStream os) throws IOException
    {
        os.writeLong(timeStamp);
        os.writeInt(eventType.getCode());
    }

    @Override
    public void readFromStream(ObjectInputStream is) throws IOException
    {
        timeStamp = is.readLong();
        eventType = WeatherEventType.fromCode(is.readInt());
    }
}