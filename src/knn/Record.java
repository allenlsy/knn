package knn;

/**
 * The record in the dataset
 *
 */
public class Record {
	public int label;
	public double[] attributes = null;
	public Record (int label2, double[] _data)
	{
		label = label2;
		attributes = _data;
	}
	
	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append(label + " ");
		for (double att : attributes)
			ret.append(att + ", ");
		return ret.toString();
	}
	
	
}
