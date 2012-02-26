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

public class LSHFunction implements Hash<LSHRecord, String>{

	public Hash<LSHRecord, Integer> h[]; // hash functions of family H, index starts from 0
	
	/*
	 * M:	[2], p120, the maximum attribute value
	 * 		[1], p520, d'
	 * 		Here it is Hamming string length
	 */
	private int M;
	
	@SuppressWarnings("unchecked")
	public LSHFunction(int size, int range)
	{
		h = new Hash[size];
		M = range;
		Random rand = new Random(); 
		for (int i = 0;i<size; i++)
			h[i] = new HammingHash(rand.nextInt(M));		
	}
	
	@Override
	public String hash(LSHRecord input) {
		// int[] ret = new int[ h.length ];
		StringBuilder ret = new StringBuilder();
		
		for (int i = 0; i < h.length; i++ )
			ret.append( h[i].hash(input) );
		return ret.toString();
	}


}
