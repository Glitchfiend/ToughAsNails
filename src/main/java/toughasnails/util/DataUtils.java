package toughasnails.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class DataUtils {
	public static <T extends Serializable> byte[] toBytebufSerialized( List<T> data ) throws IOException {
		ByteArrayOutputStream bos = null; 
		ObjectOutputStream oos = null;
		
		if( data == null )
			throw new IOException("null input.");
		
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream( bos );
			
			oos.writeInt(data.size());
			for( T obj : data ) {
				oos.writeObject(obj);
			}
			
			return bos.toByteArray();
		}
		finally {
			if( oos != null )
				oos.close();
			if( bos != null )
				bos.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> List<T> toListSerialized( @Nonnull byte[] byteBuffer, Class<T> clazz ) throws IOException {
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		
		if( byteBuffer == null )
			throw new IOException("null input.");
		
		try {
			bis = new ByteArrayInputStream( byteBuffer );
			ois = new ObjectInputStream( bis );
			
			int size = ois.readInt();
			ArrayList<T> list = new ArrayList<T>(size);
			for( int i = 0; i < size; i ++ ) {
				Object obj = ois.readObject();
				if( !clazz.isInstance(obj) )
					throw new IOException("Wrong class.");
				list.add((T)obj);
			}
			
			return list;
		} catch (ClassNotFoundException e) {
			throw new IOException("Wrong class.", e);
		}
		finally {
			if( ois != null )
				ois.close();
			if( bis != null )
				bis.close();
		}
	}
	
	public static <T extends IDataStorable> byte[] toBytebufStorable( List<T> data ) throws IOException {
		ByteArrayOutputStream bos = null; 
		ObjectOutputStream oos = null;
		
		if( data == null )
			throw new IOException("null input.");
		
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream( bos );
			
			oos.writeInt(data.size());
			for( T obj : data ) {
				obj.writeToStream(oos);
			}
			
			oos.flush();
			return bos.toByteArray();
		}
		finally {
			if( oos != null )
				oos.close();
			if( bos != null )
				bos.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends IDataStorable> List<T> toListStorable( byte[] byteBuffer, Class<T> clazz ) throws IOException {
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		
		if( byteBuffer == null )
			throw new IOException("null input.");
		
		try {
			bis = new ByteArrayInputStream( byteBuffer );
			ois = new ObjectInputStream( bis );
			
			int size = ois.readInt();
			ArrayList<T> list = new ArrayList<T>(size);
			for( int i = 0; i < size; i ++ ) {
				T obj = clazz.newInstance();
				obj.readFromStream(ois);
				list.add((T)obj);
			}
			
			return list;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IOException("Object instanciation error.", e);
		}
		finally {
			if( ois != null )
				ois.close();
			if( bis != null )
				bis.close();
		}
	}
}
