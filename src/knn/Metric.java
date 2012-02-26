package knn;

/**
 * The interface of the similarity calculation metric
 *
 */
public interface Metric {
	/**
	 * Calculate the similarity between two records
	 * @param r1 the first record
	 * @param r2 the second record
	 * @return the similarity
	 */
	double compute(Record r1, Record r2);
}
