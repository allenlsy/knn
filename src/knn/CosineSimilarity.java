package knn;

import java.util.Map.Entry;

public class CosineSimilarity implements Metric {
	/**
	 * Cos(r1, r2) 	= (r1.*r2) / |r1|*|r2|
 			= ( sigma(r1*r2) ) / sqrt( sigma(r1^2) * sigma(r2^2) ) 
	 */
	
	@Override
	public double compute(Record r1, Record r2) {
		double nominator = 0, denominator = 0;
		
		/* initialize variables */
		int length = r1.attributes.size();
		double nr1 = 0, nr2 = 0; // nr1: norm of r1; nr2: norm of r2 
		/*
		for (int i = 0 ; i<length ; i++)
		{
			// r1v: value of r1 attributes at index i
			// r2v: value of r2 attributes at index i
			double r1v = r1.attributes.get(i), r2v = r2.attributes.get(i);   
			nominator += r1v * r2v ;
			nr1 += r1v * r1v;
			nr2 += r2v * r2v;
		}
		*/
		
		for (Entry<Integer, Double> entry : r1.attributes.entrySet())
		{
			int i = entry.getKey();
			if (r2.attributes.containsKey(i))
			{
				double r1v = r1.attributes.get(i), r2v = r2.attributes.get(i);   
				nominator += r1v * r2v ;
				nr1 += r1v * r1v;
				nr2 += r2v * r2v;
			}
		}
		
		denominator = Math.sqrt(nr1 * nr2);
		
		return nominator / denominator;
	}

}
