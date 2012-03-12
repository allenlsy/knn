package knn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;


/**
 * LSH classifier.
 * 
 * NOTICE:
 * 	1. For the data in the dataset, we truncate double number into int
 * 	2. LSH can only be applied on dataset with all the attribute values positive 
 * 
 * --------------------------------------------
 * LSH INTRODUCTION:
 * 
 * PROBLEM:
 * 	R>0, approximation faction c>1. p, q are two points in a d-dimension space
 * 		* If dist(p,q) <= R, then h(p)=h(q) with probability at least p1.
 * 		* If dist(p,q) >= cR, then h(p)=h(q) with probability at most p2.
 * 
 * We define a new family g of hash functions, where each function g_ is obtained by 
 * 	concatenating k functions h[1], .., h[k] from H, i.e., g_(p) = [h[1](p), h[2](p) ..,
 * 	h[k](p) ]. In other words, a random hash function g_ is obtained by concatenating k 
 * 	randomly chosen hash functions from H. The algorithm then constructs L hash tables HT[],
 * 	each corresponding to a different randomly chosen hash function g_.
 * 
 * In the preprocessing step we hash all n points from the data set S into each of  
 * 	the L hash tables. Given that the resulting hash tables have only n non-zero entries, 
 * 	one can reduce the amount of memory used per each hash table to O(n) using standard hash 
 * 	functions.
 * 
 * // TODO: change HT type of map to standard hash functions.
 * 
 * 
 * For a fixed approximation ration c = 1 + eps and probabilities p1 and p2, one can set 
 * 	k = log(n) / log(1/p2), and L = n^rho, where rho = log(p1)/log(p2). 
 * When use Hamming distance, rho = 1/c is proved in [2], p120.
 * 
 * * The family H of hash functions is simply the family of all the projections of 
 * 	points on one of the d coordinates 
 * 		
 * 		H = { h:{0,1}^d -> {0,1} | h(x) = x[i], i = 1..d }
 * 
 *  where x[i] is the i-th coordinate of x. A random function h from H simply
 *  selects a random bit from the input point. This family has the following parameters:
 *  	p1 = 1 - R/d, p2 = 1 - cR / d.
 *  
 *  [ http://en.wikipedia.org/wiki/Locality_sensitive_hashing#Bit_sampling_for_Hamming_distance ]
 * -------------------------------------
 * 
 * [1]: Similarity Search in High Dimensions via Hashing
 * [2]: Near-Optimal Hashing Algorithms For Approximate Nearest Neighbor In High Dimensions 
 *
 */

public class LSLSH extends Classifier{

	
	/** range */ 
	private double r;	
	/** approximation */
	private double c;
	/** number of hash tables */
	private int L;
	/** number of hash functions in a g function */
	private int k; 
	
	private double p1;
	private double p2;
	private LSLSHFunction[] g; 		// index starts from 0
	private HashMap<Integer, LinkedList<Record> > HT[]; 
									// index starts from 0
	
	@Override
	protected int getTrainDSSize() {
		return trainDS.size();
	}

	protected List<LSHRecord> trainDS; // dataset
	
	/**
	 * Querying
	 * 
	 * NOTICE: 
	 * 	If the max attribute value of test record exceed M, the classified label
	 *  	may possibly be wrong. 
	 */
	@Override
	public int classify(Record rec) {
		int ret = 0;
		
		LSHRecord record = new LSHRecord( rec, M );
		
		List<Pair<Integer, Double> > candidates = new LinkedList<Pair<Integer, Double> >();
		
		/*
		 * Querying q. 
		 * For each g[i], i = [1,L]
		 */
		for (int i = 0; i < L; i++)
		{		
			/*
			 * 1) Retrieve the points from the bucket g[j](q) in the j-th hash table
			 */
			int hashValue = g[i].hash(record);
			if (!HT[i].containsKey(hashValue))
				continue;
			for ( Record neighbor : HT[i].get(hashValue) )
			{
				double dist = metric.compute(neighbor, record); 
				candidates.add( new Pair<Integer, Double>(neighbor.label, dist) );				
			}		
		}
		
		/*
		 * 2) For each of the retrieved point, compute the distance from q to it, and report 
		 * 	the point if it is a correct answer (cR-near neighbor for Strategy 1, R-near neighbor 
		 * 	for Strategy 2).
		 */
		Collections.sort(candidates);
		
		int stats[] = new int[classes + 1];
		int arrayLength = kValue < candidates.size() ? kValue : candidates.size();
		for (int i =0;i<arrayLength;i++)
		{
			int thisLabel = candidates.get(i).key; 
			stats[ thisLabel ] ++ ;
		}
		int max = 0;
		for (int i = 0; i<=classes; i++)
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

	/**
	 * The pre-processing stage in the paper [2]		
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void train() {
		
		/*
		 * 1. Choose L functions g[j], j = 1..L, by setting g[j] = (h[j,1], h[j,2]..h[j,k]), 
		 * 		where h[j,1], h[j,2]..h[j,k] are chosen at random from the LSH family H
		 * ( Modified from original paper ) 
		 */
		
		g = new LSLSHFunction[L];
		for (int i = 0; i < L; i++ )
		{
			g[i] = new LSLSHFunction(M, d);			
		}
		
		/*
		 * 2. Construct L hash tables, where, for each j=1,..,L, the j-th hash table contains the 
		 * 		dataset points hashed using the function g[j]
		 * 
		 * [2]: Since the total number of buckets may be large, we retain only the nonempty buckets by resorting to hashing.
		 */
		
		HT = new HashMap[L];
		for (int i=0; i<L; i++)
			HT[i] = new HashMap<Integer, LinkedList<Record>>();
		
		int hashValue;
		
		// for each record
		for(LSHRecord r : trainDS)
		{
			
			// hash record using each g
			for(int i = 0; i < L; i++)
			{
				// System.out.println("DEBUG: i = " + i);
				hashValue = g[i].hash(r);
				
				if (!HT[i].containsKey(hashValue))
				{
					HT[i].put(hashValue, new LinkedList<Record>());
				}
				HT[i].get(hashValue).add(r);							
			}			
		}				
	}

	/**
	 * Code is almost the same as KNN, except creating LSHRecord data set
	 */
	// @SuppressWarnings({ "unchecked", "unused" })
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(String[] args) {
		/* 0. Initialization: read arguments from command line */
		
		// R-range, c-approximity
		// p1-inside possibility, p2-outside possibility
		//		Since our program uses Hamming distance, p1 and p2 are not user defined.
		String trainFileName = null, testFileName = null;
		
		int argsOffset = 0; // To process the command line arguments
		if (args.length == 6)
		{
			trainFileName = args[0];
			testFileName = args[1];
		}
		else
			if (args.length == 5)
			{
				trainFileName = args[0] + ".train.txt";
				testFileName = args[0] + ".test.txt";
				argsOffset = 1;
			}
			else
				if (args.length == 7)
				{
					trainFileName = args[0] + ".train.txt";
					testFileName = args[0] + ".test.txt";
					k = new Integer( args[5] );
					L = new Integer( args[6] );
					argsOffset = 1;
				}				
				else
				{
					usage();
					System.exit(0); 
				}
		r = new Double(args[2-argsOffset]);
		c = new Double(args[3-argsOffset]);
		kValue = new Integer(args[4-argsOffset]);
		Integer metricValue = new Integer(args[5-argsOffset]);
		
		
		if (metricValue == 0)
			metric = new Euclidean();
		else
			if (metricValue == 1)
				metric = new CosineSimilarity();
		
		/* 1. Read dataset */
		initializeDataset(trainFileName);
		createLSHDataset(trainFileName);
		
		p1 = 1 - (double)r/d;
		p2 = 1 - c*r/d;
		
		if (args.length == 5 || args.length == 6 )
		{
			
			/*
			 * Initialization: calculate L, k
			 * 
			 * 	precision: rho in [2], p119
			 * 	L: number of hash tables
			 * 	k: number of hash functions in every g[j]
			 */
			int n = trainDS.size();	// training dataset size
			int B = 2000/d;			// Bucket size 
			Main.DEBUG("n: " + n);
			Main.DEBUG("B: " + B);
			
			// In Hamming distance
			// double precision = 1 / c;
			double precision = Math.log( 1.0/p1 ) / Math.log( 1.0/p2 );
			L = (int)Math.pow( n/B, precision);
			k = (int) (Math.log( n/B ) / Math.log(1/p2) );
			
			Main.DEBUG("precision: " + precision);
			Main.DEBUG("L: " + L);
			Main.DEBUG("k: " + k);
		}
		else
			if (args.length == 7)
			{
				L = new Integer(args[5]);
				k = new Integer(args[6]);							
			}
			
		HT = new HashMap[L];
		
		testDS = createDataset(testFileName);		
	}

	@Override
	public void usage() {
		System.out.println("lshMain <train file> <test file> <R-range> <c-approximity>" +
				" <k-value> <metric value>");
		System.out.println("OR");
		System.out.println("lshMain <dataset name> <R-range> <c-approximity>" +
				" <k-value> <metric value> [ <k-# hash functions> <L-# LSH functions> ]" +
				" [ <p1> <p2> ]");
		System.out.println();
		System.out.println("metric value: 0-Euclidean distance, 1-Cosine Similarity");
		System.out.println("c: the multiple of R, approximity. c > 1");		
	}

	/**
	 * Create training dataset with LSHRecord inside.
	 * 
	 * Code is almost the same with Classifier.createDataset, except inserting new record.
	 * @param filepath dataset file path
	 */
	private void createLSHDataset(String filepath) {
		trainDS = new LinkedList<LSHRecord>();
		
		try {
			/* 1. Initialize internal parameters required for creating dataset
			 * 		by preprocessing the dataset.
			 * */

			// initializeDataset(filepath);
			if (d == -1)
			{
				System.out.println("Input file error");
				System.out.println("Error in: lsh.createDataset().");

				return;				
			}
			
			/* 2. Read data in the dataset */
			BufferedReader br = new BufferedReader( new FileReader( filepath ));
			String[] words = null; // words stores the different parts in one string
			String word;
			int index; // data dimension
			double value; // value of that dimension

			String line = null;
			while ( br.ready() )
			{
				double[] attributes = new double[d+1];
				line = br.readLine();
				words = line.split(" ");
				if (words[0].startsWith("+") )
					words[0] = words[0].substring(1);
				int label = Integer.parseInt(words[0]);
				for (int i=1;i<words.length;i++)
				{
					word = words[i];
					StringTokenizer st = new StringTokenizer(word, ":");
					index = new Integer(st.nextToken());
					value = new Double(st.nextToken()) * scalingValue;

					attributes[index] = value;
				}
				trainDS.add( new LSHRecord(label + labelOffset, attributes, M) );
			}				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
