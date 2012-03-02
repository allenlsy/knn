package knn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * The interface of a classification algorithm
 */
public abstract class Classifier {
	protected List<Record> trainDS; // dataset
	protected List<Record> testDS;
	protected Metric metric = null; // similarity calculation metric
	protected int kValue; // k value
	

	/**
	 * Classify a record
	 * @param record
	 * @return the predicted class label of the record
	 */
	protected abstract int classify(Record record);
	
	/**
	 * Train the dataset
	 */
	public abstract void train();
	
	/**
	 * Run the test dataset. Output the result in "prediction.txt"
	 * @return the accuracy
	 */
	public double test()
	{	
		int correct = 0; 
		
		try {
			PrintWriter out = new PrintWriter("prediction.txt");
			// test each data 
			for( Record record: testDS )
			{
				int predictedLabel = classify(record);
				out.println("Predicted: " + predictedLabel + ", Real: " + record.label);
				if (predictedLabel == record.label)
					correct++;
			}		
			out.close();
			
		} catch (Exception e) {
			System.out.println("Prediction file output failed.");
			e.printStackTrace();
		}
		return (double)correct*100/testDS.size();
	}
	
	/**
	 * Read a dataset from a file
	 * @param fileName the file name of the dataset
	 * @return List of Record to indicate the whole dataset
	 */
	protected List<Record> createDataset(String fileName) {
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

	
	/**
	 * Initialize classifier with arguments
	 * @param args > 0
	 */
	public abstract void initialize(String args[]);
	
	/**
	 * Display the usage information
	 */
	public abstract void usage();
	
}
