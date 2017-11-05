package toughasnails.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface IDataStorable
{
    public void writeToStream(ObjectOutputStream os) throws IOException;

    public void readFromStream(ObjectInputStream is) throws IOException;
}
