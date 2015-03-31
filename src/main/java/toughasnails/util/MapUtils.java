package toughasnails.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MapUtils
{
    public static <K, V extends Comparable<? super V>> Map<K, V> sortMapByValue(Map<K, V> map)
    {
        List<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>(map.size());
        list.addAll(map.entrySet());
        ValueComparator<K , V> comparator = new ValueComparator<K, V>();
        Collections.sort(list, comparator);
        
        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Iterator iterator = list.iterator(); iterator.hasNext();) 
        {
            Map.Entry<K, V> entry = (Map.Entry<K, V>)iterator.next();
            result.put(entry.getKey(), entry.getValue());
        }
        
        return result;
    }
    
    private static class ValueComparator<K, V extends Comparable<? super V>> implements Comparator<Map.Entry<K, V>>
    {
        @Override
        public int compare(Entry<K, V> o1, Entry<K, V> o2)
        {
            return -o1.getValue().compareTo(o2.getValue());
        }
    }
}
