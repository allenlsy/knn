package knn;

/**
 * The record in the dataset
 *
 */
public class Record {
	public String label;
	public double[] attributes = null;
	public Record (String label2, double[] _data)
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
