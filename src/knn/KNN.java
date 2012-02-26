package knn;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.StringTokenizer;


/**
 * The naive KNN algorithm  
 *
 */
public class KNN extends Classifier{
		
	@Override
	public int classify(Record record) {
		
		/* Compute all the similarities */

		// list<instance id, similarity>, used to store the calculated similarity among 
		//   the data in the dataset
		List< Pair<Integer, Double> > list = new LinkedList<Pair<Integer,Double>>();   
		for (Record trained : trainDS)
		{
			double similarity = metric.compute(trained, record);
			list.add( new Pair<Integer, Double> (trained.label, similarity) );
		}
		
		/* sort the similarities */
		Collections.sort(list);
				
		/* get the most frequent label of the top k records */
		int stats[] = new int[30];	// @SuppressWarnings("unchecked")

		for (int i = 0;i<kValue;i++)
			stats[ list.get(i).key ]++;
		int max = 0, res = 0;
		for (int i = 0; i<30;i++)
			if (stats[i]>max)
			{
				max = stats[i];
				res = i;
			}
		
		return res;
	}
	
	@Override
	public void train() {
		missingDataProcessing();		
	}

	private void missingDataProcessing() {
		
	}

	/**
	 * Read a dataset from a file
	 * @param fileName the file name of the dataset
	 * @return List of Record to indicate the whole dataset
	 */
	public List<Record> createDataset(String fileName) {
		List<Record> ret = new LinkedList<Record>();
		
		try {
			/* 1. Get max dimension in the dataset*/
			int d = getDatasetDimension(fileName); // dimension
			if (d == -1)
				throw new Exception("Input file error.\nError in: createDataset().");				
			
			/* 2. Read data in the dataset */
			BufferedReader br = new BufferedReader( new FileReader( fileName ));
			String[] words = null; 		// words stores the different parts in one string
			String word;
			int index; 					// data dimension
			double value; 				// value of that dimension
			
			String line = null;
			while ( br.ready() )
			{
				double[] attributes = new double[d+1];
				
				/*
				 * TODO: empty attribute should be assigned default value.
				 */
				
				line = br.readLine();
				words = line.split(" ");
				int label = new Integer(words[0]);
				for (int i=1;i<words.length;i++)
				{
					word = words[i];
					StringTokenizer st = new StringTokenizer(word, ":");
					index = new Integer(st.nextToken());
					value = new Double(st.nextToken());
									
					attributes[index] = value;
				}
				ret.add( new Record(label, attributes) );
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return ret;
	}

	/**
	 * Find the dimension of dataset
	 * @param fileName
	 * @return the dimension
	 */
	private int getDatasetDimension(String fileName) {
		int ret = -1;
		
		try {
			BufferedReader br = new BufferedReader( new FileReader( fileName ));
			String[] words = null; // words stores the different parts in one string
			String word;
			int index;
			
			String line = null;
			while ( br.ready() )
			{
				line = br.readLine();
				words = line.split(" ");
				for (int i=1;i<words.length;i++)
				{
					word = words[i];
					StringTokenizer st = new StringTokenizer(word, ":");
					index = new Integer(st.nextToken());
					
					ret = ret < index ? index :ret;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}

	public List<Record> getTrainDS() {
		return trainDS;
	}

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
