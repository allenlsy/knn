package knn;

public class CosineSimilarity implements Metric {
	/**
	 * Cos(r1, r2) 	= (r1.*r2) / |r1|*|r2|
 			= ( sigma(r1*r2) ) / ( sigma(r1^2) * sigma(r2^2) ) 
	 */
	
	@Override
	public double compute(Record r1, Record r2) {
		double nominator = 0, denominator = 0;
		
		/* initialize variables */
		int length = r1.attributes.length;
		double nr1 = 0, nr2 = 0; // nr1: norm of r1; nr2: norm of r2 
		
		for (int i = 0 ; i<length ; i++)
		{
			// r1v: value of r1 attributes at index i
			// r2v: value of r2 attributes at index i
			double r1v = r1.attributes[i], r2v = r2.attributes[i];   
			nominator += r1v * r2v ;
			nr1 += r1v * r1v;
			nr2 += r2v * r2v;
		}
		
		denominator = Math.sqrt(nr1) * Math.sqrt(nr2);
		
		return nominator / denominator;
	}

}
