package knn;

import java.util.LinkedList;
import java.util.List;
import java.io.PrintWriter;

public class e2lsh {

	public static void usage()
	{
		String text = "Convert assignment 1 dataset format to E2LSH dataset format\n" +
				"e2lsh <dataset name>";
		System.out.println(text);		
	}
	
	public static void main(String args[])
	{
		if (args.length != 1)
		{
			usage();
			return ;
		}
		try {
			String datasetName = args[0];
			String trainFilename = datasetName + ".train.txt";
			String testFileName = datasetName + ".test.txt";
			// KNN ds = new KNN(trainFilename, 0, null);
			KNN ds = new KNN();
			ds.createDataset(trainFilename);
			PrintWriter out = null;
			
			/*
			 * output train data file
			 */
			LinkedList<Record> list = (LinkedList<Record>) ds.getTrainDS();
			
			out = new PrintWriter(datasetName + ".d");
			for (Record rec : list)
			{
				// for (double value : rec.attributes)
				for (int i = 1; i< rec.attributes.length; i++)
					out.print( rec.attributes[i] + " ");
				out.println();
			}
			out.close();
			
			/*
			 * output test data file
			 */
			List<Record> testDS = ds.createDataset(testFileName);
			out = new PrintWriter(datasetName + ".t");
			for (Record rec : list)
			{
				// for (double value : rec.attributes)
				for (int i = 1; i< rec.attributes.length; i++)
					out.print( rec.attributes[i] + " ");
				out.println();
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
