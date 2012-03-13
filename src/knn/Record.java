package knn;

import java.util.HashMap;

/**
 * The record in the dataset
 *
 */
public class Record {
	public int label; 
	public HashMap<Integer, Double> attributes = null;
	public Record (int label2, HashMap<Integer, Double> _data)
	{
		label = label2;
		attributes = _data;
	}
	
	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append(label + " ");
		for (double att : attributes.values())
			ret.append(att + ", ");
		return ret.toString();
	}
	
	
}
