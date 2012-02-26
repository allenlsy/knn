package knn;

@SuppressWarnings("rawtypes")
public class Pair<K,V extends Comparable> implements Comparable< Pair<K,V> >
{
	public K key;
	public V value;
	public Pair(K _key, V _value)
	{
		key = _key;
		value = _value;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Pair<K, V> o) {
		return value.compareTo(o.value);
	}
	
	@Override
	public String toString() {
		return "Key: " + key + ",  Value: " + value;
	}
	
}