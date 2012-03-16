package knn;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

/**
 * LSHFunction, hash function based on Hamming distance 
 * 
 * NOTICE:
 * 	1. 	g function talked about in papers, [2].
 * 	2. 	Hamming function selection: http://en.wikipedia.org/wiki/Locality_sensitive_hashing 
 * 		Bit sampling for Hamming distance 
 * 
 * 
 * @author allenlsy
 *
 */

public class LSHFunction implements Hash<LSHRecord, Integer >{

	/**
	 * @uml.property  name="h"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	public Hash<LSHRecord, Integer> h[]; // hash functions of family H, index starts from 0
	
	/*
	 * M:	[2], p120, the maximum attribute value
	 * 		[1], p520, d'
	 * 		Here it is Hamming string length
	 */
	/**
	 * @uml.property  name="m"
	 */
	private int M;
	
	private int d;
	
	private int a[];
	
	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @param size dimension of the record
	 * @param range maximum value of the record
	 */
	public LSHFunction(int size, int range)
	{
	/*
		h = new Hash[size];
		M = range;
		Random rand = new Random(); 
		for (int i = 0;i<size; i++)
			h[i] = new HammingHash(rand.nextInt(M));
	*/
		d = size;
		a = new int[size+1];
		Random rand = new Random();
		
		for (int i=1; i<=size;i++)
			a[i] = rand.nextInt();
		// for (int i )
		
		
	}
	
	
	
	
	@SuppressWarnings("rawtypes")
	@Override
	/*
	public String hash(LSHRecord input) {
		// int[] ret = new int[ h.length ];
		StringBuilder ret = new StringBuilder();
		
		for (int i = 0; i < h.length; i++ )
			ret.append( h[i].hash(input) );
		return ret.toString();
	}
	*/
	
	public Integer hash(LSHRecord input) {
		// return 0;
		double sum = 0;
		for (int i = 1;i<=d; i++)
			sum += input.attributes[i] * a[i];
		if (sum < 0)
			sum += (-sum/M +1)*M;
		return new Integer( (int)sum % M ); 
	}


}
