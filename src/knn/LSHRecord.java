/**
 * 
 */
package knn;

/**
 * @author allenlsy
 *
 */
public class LSHRecord extends Record {
	public String hamming;
	// private int M; // M: the max value of attribute in the dataset
	public LSHRecord(String label, double[] _data, int _M) {
		super(label, _data);

		/*
		 * Calculate Hamming string
		 */
		hamming = LSHRecord.hamming(_data, _M);	
	}
	
	/**
	 * Create  LSHRecord from Record
	 * @param rec
	 * @param _M the max attribute value
	 */
	public LSHRecord(Record rec, int _M) {
		super(rec.label, rec.attributes);
		
		hamming = LSHRecord.hamming(rec.attributes, _M);
	}

	private static String hamming(double[] _data, int M)
	{
		StringBuilder sb = new StringBuilder();
		for (double d : _data)
		{
			/*
			 * TODO: can be optimized
			 */
			int value = (int) d;
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
