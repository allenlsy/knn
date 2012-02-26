package knn;

/**
 * The record in the dataset
 *
 */
public class Record {
	public int label;
	public double[] attributes = null;
	public Record (int _label, double[] _data)
	{
		label = _label;
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
