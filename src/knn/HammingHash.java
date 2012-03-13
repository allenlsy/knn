package knn;

/**
 * The family H of hash functions is simply the family of all the projections of 
 * 	points on one of the d coordinates 
 * 		
 * 		H = { h:{0,1}^d -> {0,1} | h(x) = x[i], i = 1..d }
 * 
 *  where x[i] is the i-th coordinate of x. A random function h from H simply
 *  selects a random bit from the input point. This family has the following parameters:
 *  	p1 = 1 - R/d, p2 = 1 - cR / d.
 *  
 *  [ http://en.wikipedia.org/wiki/Locality_sensitive_hashing#Bit_sampling_for_Hamming_distance ]
 * 
 * @author allenlsy
 *
 */
public class HammingHash implements Hash<LSHRecord, Integer> {

	/**
	 * @uml.property  name="position"
	 */
	private int position;
	public HammingHash(int _position)
	{
		position = _position;
	}
	
	@Override
	public Integer hash(LSHRecord input) {		
		return new Integer( input.hamming.charAt(position) );
 	}

}
