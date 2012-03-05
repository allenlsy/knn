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
	protected Metric metric = null; // similarity calculation metric
	protected int kValue; // k value
	protected int dimension;
	protected List<Record> testDS;

	/** max attribute value in the dataset */
	protected int M; 	
	/** dimension of the dataset */
	protected int d = -1; 
	/** offset added to label to ensure all the labels are positive */
	protected int labelOffset = 0;
	/** For scaling up those datasets with very small attribute values */
	protected int scalingValue = 1;
	/** number of classes, 0 is not counted as a class, otherwise #labels should increase */
	protected int classes;
	
	/**
	 * Classify a record
	 * @param record
	 * @return the predicted class label of the record
	 */
	protected abstract int classify(Record record);
	
	/**
	 * Get training data set size
	 * @return the training data set size
	 */
	protected abstract int getTrainDSSize();
	
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
				out.println("Predicted: " + (predictedLabel-labelOffset) + ", Real: " 
						+ (record.label-labelOffset) );
				if (predictedLabel == record.label )
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
	 * Read a normal dataset from a file
	 * @param fileName the file name of the dataset
	 * @return List of Record to indicate the whole dataset
	 */
	protected List<Record> createDataset(String fileName) {
		List<Record> ret = new LinkedList<Record>();
		
		try {
			/* 1. Get max dimension in the dataset*/
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
				if (words[0].startsWith("+") )
					words[0] = words[0].substring(1);
				int label = Integer.parseInt(words[0]);
				for (int i=1;i<words.length;i++)
				{
					word = words[i];
					StringTokenizer st = new StringTokenizer(word, ":");
					index = new Integer(st.nextToken());
					value = new Double(st.nextToken());
									
					attributes[index] = value;
				}
				ret.add( new Record(label + labelOffset, attributes) );
				// ret.add( new Record(label, attributes) );
				
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return ret;
	}

	
	/**
	 * Initialize the :	max dimension
	 * 					max attribute value
	 * 					label offset
	 * 					scaling value
	 * @param filepath
	 */
	protected void initializeDataset(String filepath) {
		int minLabel = 10000, maxLabel = -10000;
		try {
			/** temp M that store the max abs value of attributes */
			double _M = 0;
			BufferedReader br = new BufferedReader( new FileReader( filepath ));
			String[] words = null; // words stores the different parts in one string
			String word;
			int index; // data dimension
			double value; // value of that dimension
			
			String line = null;
			while ( br.ready() )
			{
				line = br.readLine();
				words = line.split(" ");
				
				
				if (words[0].startsWith("+") )
					words[0] = words[0].substring(1);
				int label=Integer.parseInt(words[0]);
				minLabel = minLabel > label ? label : minLabel;
				maxLabel = maxLabel < label ? label : maxLabel;
				for (int i=1;i<words.length;i++)
				{
					word = words[i];
					StringTokenizer st = new StringTokenizer(word, ":");
					index = Integer.parseInt(st.nextToken());
					value = Math.abs( Double.parseDouble(st.nextToken()) );
					
					_M = _M <  value ?  value : _M;
					d = d < index ? index : d;
				}
			}
			
			/* Scaling atrribute values */
			M = (int)_M;
			
			/* label offset to ensure that all the labels are positive */
			if (minLabel <= 0)
			{
				labelOffset = -minLabel;
				classes = maxLabel + labelOffset;
			}
			else
				classes = maxLabel;
			
			Main.DEBUG("minLabel: " + minLabel);
			Main.DEBUG("maxLabel: " + maxLabel);
			Main.DEBUG("labelOffset: " + labelOffset);
			Main.DEBUG("classes: " + classes);
			Main.DEBUG("M: " + M);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
