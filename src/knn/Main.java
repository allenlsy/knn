package knn;

import java.io.PrintWriter;
import java.util.Arrays;

public class Main {
	
	private static boolean debugMode;
	
	public static void DEBUG(Object obj)
	{
		if (debugMode)
			System.out.println( "*** DEBUG ***   " + obj.toString() );
	}

	public static void main(String args[])
	{
		Classifier classifier = null;
		long timeStart, timeEnd;
		
		/* Display Main usage */
		if (args.length == 0)
		{
			Main.usage();
			return ;
		}
			
		/*
		 * 0. Initialize arguments
		 */
		try {
			int argsLength = args.length; 
			
			/* DebugMode argument */
			if (args[ argsLength-1 ].equals("d") )
			{
				Main.debugMode = true;
				System.out.println("Debug Mode ON.\n");
				argsLength--;
			}
			
			/* classifier args */
			String classifierName = "knn." + args[0].toUpperCase();
			classifier = (Classifier)Class.forName(classifierName).newInstance();
			String classifierArgs[] = new String[ argsLength - 1 ];
			
			/* classifier usage */
			if (classifierArgs.length == 0)
			{
				classifier.usage();
				return;				
			}
			
			/*
			 * Initialize classifier
			 */
			System.out.println("Initializing dataset...");	
			timeStart = System.currentTimeMillis();			
			for (int i = 0; i < classifierArgs.length; i++)
				classifierArgs[i] = args[i+1];			
			classifier.initialize(classifierArgs);
			Main.DEBUG("Dimension: " + classifier.d);
			timeEnd = System.currentTimeMillis();
			Main.DEBUG("Time used(ms): " + (timeEnd - timeStart) );
			System.out.println();
			
		} catch (Exception e) {
			System.out.println("Initialize failed.");
			e.printStackTrace();
			return;
		}
		long trainingTime = 0, testingTime = 0;
		
		
		/*
		 * 1. train 
		 */
		System.out.println("Training data...");
		Main.DEBUG("Training Dataset size: " + classifier.getTrainDSSize());		
		timeStart = System.currentTimeMillis();
		classifier.train();
		timeEnd = System.currentTimeMillis();
		trainingTime = timeEnd - timeStart;
		Main.DEBUG("Time used(ms): " + trainingTime );
		Main.DEBUG("Training Time/record: " + 
				(double)trainingTime / classifier.getTrainDSSize());
		
		System.out.println();
		
		/*
		 * 2. test 
		 */
		// classifier.createDataset();
		System.out.println("Testing data...");
		timeStart = System.currentTimeMillis();
		double accuracy = -1;
		try {
			accuracy = classifier.test();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		timeEnd = System.currentTimeMillis();
		testingTime = timeEnd-timeStart;
		// report
		System.out.printf("  Accuracy: %.2f%%\n", accuracy );
		System.out.println("  Time used(ms): " + (testingTime+trainingTime) );
		Main.DEBUG("Testing Dataset size: " + classifier.testDS.size());
		Main.DEBUG("Time used(ms): " + testingTime);
		Main.DEBUG("Testing Time/record: " + 
				(double)testingTime / classifier.testDS.size());
		System.out.println();
		
	}

	/**
	 * Display help message
	 */
	private static void usage() {
		String help = "java knn.Main <classifier name> [ corresponding arguments ] [d]\n\n"
				+ "Corresponding arguments view be displayed after entering classifier name"+
				"\n\n" +
				"classifier name - knn, lsh, lslsh (case insensitive)\n" +
				"d - debug mode\n";
		System.out.println(help);
	}
	
}
