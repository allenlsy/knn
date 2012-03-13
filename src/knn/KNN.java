package knn;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.Map.Entry;


/**
 * The naive KNN algorithm  
 *
 */
public class KNN extends Classifier{
		
	protected List<Record> trainDS; // dataset
	
	@Override
	public int classify(Record record) {
		int ret = 0;
		/* Compute all the similarities */

		// list<instance id, similarity>, used to store the calculated similarity among 
		//   the data in the dataset
		/*
		List< Pair<Integer, Double> > candidates = new LinkedList<Pair<Integer,Double>>();   
		for (Record trained : trainDS)
		{
			double similarity = metric.compute(trained, record);
			candidates.add( new Pair<Integer, Double> (trained.label, similarity) );
		}
		*/
		
		@SuppressWarnings("unchecked")
		Pair<Integer, Double> candidates[] = new Pair[trainDS.size()];
		for (int i = 0;i<trainDS.size();i++)
		{
			Record trained = trainDS.get(i);
			double similarity = metric.compute(trained, record);
			candidates[i] = new Pair<Integer, Double> (trained.label, similarity);
		}
		
		
		/* sort the similarities */
		/*
		 * 2) For each of the retrieved point, compute the distance from q to it, and report 
		 * 	the point if it is a correct answer (cR-near neighbor for Strategy 1, R-near neighbor 
		 * 	for Strategy 2).
		 */
		// Collections.sort(candidates);
		Arrays.sort(candidates);
		
		int stats[] = new int[classes+1];
		
		int arrayLength = kValue < candidates.length ? kValue :candidates.length;
		for (int i = 0; i<arrayLength; i++)
		{
			int thisLabel = candidates[i].key; // this Label is the label of current examining record
			stats[ thisLabel ]++;			
		}
		int max = 0;
		for (int i = 0; i<= classes; i++)
		{
			if (stats[i] > max)
			{
				max = stats[i];
				ret = i;
			}			
		}
		// return ret - labelOffset;
		return ret;
	}
	
	@Override
	protected int getTrainDSSize() {
		return trainDS.size();
	}

	@Override
	public void train() {
		missingDataProcessing();		
	}

	private void missingDataProcessing() {
		
	}
	
	public List<Record> getTrainDS() {
		return trainDS;
	}

	/**
	 * Code is almost the same as LSH, except creating normal training and testing datasets
	 */
	@Override
	public void initialize(String[] args) {
		
		/*
		 * 1. Initialize arguments
		 */
		String trainFileName = null, testFileName = null;
		metric = null;
		int argsOffset = 0;
		
		if (args.length == 3)
		{
			argsOffset = 1;
			trainFileName = args[0] + ".train.txt";
			testFileName = args[0] + ".test.txt";
		}
		else
			if (args.length != 4)
			{
				usage();
				System.exit(0); 
			}
	
		kValue = new Integer(args[2 - argsOffset]);
		Integer metricValue = new Integer(args[3 - argsOffset]);
		if (metricValue == 0)
			metric = new Euclidean();
		else if (metricValue == 1)
			metric = new CosineSimilarity();
		
		/* 
		 * 2. create train & test datasets
		 */
		initializeDataset(trainFileName);
		trainDS = createDataset(trainFileName);
		testDS  = createDataset(testFileName);
	}
	
	@Override
	public void usage() {
		System.out.println("KNN <train file> <test file> <k value> <metric value>");
		System.out.println("OR");
		System.out.println("KNN <dataset name> <k value> <metric value>");
		System.out.println();
		System.out.println("metric value: 0-Euclidean distance, 1-Cosine Similarity");		
		
	}
		
}
