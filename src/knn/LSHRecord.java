/**
 * 
 */
package knn;

import java.util.HashMap;

/**
 * @author allenlsy
 *
 */
public class LSHRecord extends Record {
	public String hamming;
	/** dimension */
	public int d; 
	// private int M; // M: the max value of attribute in the dataset
	public LSHRecord(int label, HashMap<Integer, Double> _data, int _M, int _d) {
		super(label, _data);
		d = _d;
		/*
		 * Calculate Hamming string
		 */
		hamming = LSHRecord.hamming(_data, _M, d);	
	}
	
	/**
	 * Create  LSHRecord from Record
	 * @param rec
	 * @param _M the max attribute value
	 */
	public LSHRecord(Record rec, int _M) {
		super(rec.label, rec.attributes);
		
		hamming = LSHRecord.hamming(rec.attributes, _M, d);
	}

	private static String hamming(HashMap<Integer, Double> _data, int M, int d)
	{
		StringBuilder sb = new StringBuilder();
		for (int j = 1; j<=d; j++)
		{
			/*
			 * TODO: can be optimized
			 */
			int value = 0;
			if (_data.containsKey(j))
				value = (int)((double)_data.get(j));
			
			if (value>=0)
			{
				for (int i = 0; i < value; i++)
					sb.append('1');
				for (int i = value; i < M; i++)
					sb.append('0');
			}
			else
			{
				value = -value;
				for (int i = 0; i < value; i++)
					sb.append('0');
				for (int i = value; i < M; i++)
					sb.append('1');
			}
		
		}
		return sb.toString();
	}
	
}
