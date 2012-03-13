package knn;

import java.util.Map.Entry;

public class Euclidean implements Metric {
	/**
	 * euclidian = sqrt( sigma( (r1[i] - r2[i])^2 ) )
	 */

	@Override
	public double compute(Record r1, Record r2) {
		double res = 0;

		double sum = 0, r1d, r2d;
		/*
		int dimension = r1.attributes.size();
		for (int i = 0; i < dimension; i++) {
			r1d = r1.attributes.get(i);
			r2d = r2.attributes.get(i);
			sum += (r1d-r2d)*(r1d-r2d);
		}
		*/
		for (Entry<Integer, Double> entry : r1.attributes.entrySet())
		{
			int i = entry.getKey();
			if (r2.attributes.containsKey(i))
			{
				r1d = r1.attributes.get(i);
				r2d = r2.attributes.get(i);
				sum += (r1d-r2d)*(r1d-r2d);
			}
		}

		return Math.sqrt(sum);
	}

}
