package ec2017.ga.tsp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ec2017.ga.general.*;
import ec2017.ga.general.selection.*;
import ec2017.ga.general.variation.*;

/**
 * The main entry point for our program.
 * @author pat
 *
 */
public class TSPProblem
{
	// Setup our thread pool.
	static ExecutorService _executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		// Load the parameters from config.prop
		File configFile = new File("config.prop");
		Properties prop = new Properties();
		prop.load(new FileReader(configFile));

		// For each operator, dynamically load and instantiate the class we've been asked to use
		CrossOverOperator crossover = null;
		String crossoverClassName = "ec2017.ga.general.variation." + prop.getProperty("crossover");
		if (crossoverClassName != null) {
			Class<? extends CrossOverOperator> crossoverClass =
					(Class<? extends CrossOverOperator>) Class.forName(crossoverClassName);
			crossover = crossoverClass.getConstructor().newInstance();
		}

		String mutationClassName = "ec2017.ga.general.variation." + prop.getProperty("mutate", "MutateNop");
		Class<? extends MutateOperator> mutateClass =
				(Class<? extends MutateOperator>) Class.forName(mutationClassName);
		MutateOperator mutator = mutateClass.getConstructor().newInstance();

		String parentSelectionClassName = "ec2017.ga.general.selection." + prop.getProperty("parent-selection", "NoParentSelectionMethod");
		Class<? extends ParentSelectionMethod> parentSelectionClass =
				(Class<? extends ParentSelectionMethod>) Class.forName(parentSelectionClassName);
		ParentSelectionMethod parentSelector = parentSelectionClass.getConstructor().newInstance();

		String survivorSelectionClassName = "ec2017.ga.general.selection." + prop.getProperty("survivor-selection", "NoSurvivorSelectionMethod");
		Class<? extends SurvivorSelectionMethod> survivorSelectionClass =
				(Class<? extends SurvivorSelectionMethod>) Class.forName(survivorSelectionClassName);
		SurvivorSelectionMethod survivorSelector = survivorSelectionClass.getConstructor().newInstance();

		int population = new Integer(prop.getProperty("population", "50"));
		int genetations = new Integer(prop.getProperty("generations", "2000"));
		int runs = new Integer(prop.getProperty("runs", "1"));

		Algorithm algorithm = new Algorithm(
				crossover,
				mutator,
				parentSelector,
				survivorSelector);

		runTests(algorithm, population, genetations, runs);

        _executor.shutdown();
        try
        {
			_executor.awaitTermination(72, TimeUnit.HOURS);
		}
        catch (InterruptedException e)
        {
			e.printStackTrace();
		}
        System.out.println("************* Done *************");

	}

	private static void runBenchMarks(Algorithm ... algorithms)
	{
		for (Algorithm algorithm : algorithms)
		{
			Runnable runnable = new Runnable()
			{
				@Override
				public void run()
				{
					benchMark(algorithm);
				}
			};

			_executor.execute(runnable);
		}
	}

	private static void testIndividual(Algorithm algorithm)
	{
		Runnable runnable = new Runnable()
		{
			@Override
			public void run()
			{
				runTests(algorithm, 50, 10000, 30);
			}
		};

		_executor.execute(runnable);
	}

	/**
	 * Reads our list of cities from a given file.
	 * @param filename The location of the file
	 * @return Cities contained.
	 */
	private static ArrayList<Symbol> readCitiesFromFile(String filename)
	{
		// Note : use SymCity to create instance for better speed.
		//        only works on symetric datasets.
		ArrayList<Symbol> cities = new ArrayList<Symbol>();

		File file = new File (filename);
		//File file = new File ("TSP_data/pr2392.tsp");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			//once a line until to the end
			while ((tempString = reader.readLine())!= null) {
				if(line>=7 && !tempString.equals("EOF")) {
					String[] tempCity = tempString.split(" ");
					//since there are some decimal numbers, changes are needed
					BigDecimal city_bdx = new BigDecimal(tempCity[1]);
					BigDecimal city_bdy = new BigDecimal(tempCity[2]);
					cities.add(new SymCity(tempCity[0], city_bdx.intValue(), city_bdy.intValue()));
				}
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {

				}
			}
		}

		return cities;
	}

	private static void benchMark(Algorithm algorithm)
	{
		runTests(algorithm, 20, 20000, 5);
		runTests(algorithm, 50, 20000, 5);
		runTests(algorithm, 100, 20000, 5);
		runTests(algorithm, 200, 20000, 5);
	}

	private static void runTests(
			Algorithm algorithm,
			int populationSize,
			int generations,
			int runs)
	{
		StringBuilder resultsLog = new StringBuilder();
		StringBuilder generationLog = new StringBuilder();

		File inputFolder = new File("TSP_data");
		for(File inFile : inputFolder.listFiles())
		{
			resultsLog.append("Input: ");
			resultsLog.append(inFile.getName());
			resultsLog.append(System.lineSeparator());
			resultsLog.append("Costs:");
			resultsLog.append(System.lineSeparator());

			System.out.println("Reading..." + inFile.getName());

			double total = 0;
			double shortest = Double.MAX_VALUE;
			Path optimal = null;

			ArrayList<Symbol> cities = readCitiesFromFile(inFile.getPath());

			double[] values = new double[runs];

			for (int i = 0; i < runs; i++)
			{
				long starttime = System.currentTimeMillis();

				Population population =
					new Population(cities, populationSize, new Path(), algorithm);

				// The InverOverOp needs a reference to the population,
				// obviously we need to wait till we have a population.
				if (InverOverOp.class.isAssignableFrom(algorithm.getMutate().getClass()))
				{
					InverOverOp iop = (InverOverOp)algorithm.getMutate();
					iop.setPopulation(population);
				}

				generationLog.append(inFile.getName());
				generationLog.append(',');

				// Do our evolution
				for (int j = 0; j < generations; j++)
				{
					population.evolve();
					if (j == 1 || j % 500 == 0)
					{
						Path path = (Path)population.getFittest();
						generationLog.append(path.getDistance());
						generationLog.append(',');
					}
				}

				generationLog.append(System.lineSeparator());

				Path bestPath = (Path)population.getFittest();
				if(bestPath.getDistance() < shortest)
				{
					optimal = bestPath;
					shortest = bestPath.getDistance();
				}

				total += bestPath.getDistance();
				values[i] = bestPath.getDistance();
				resultsLog.append(bestPath.getDistance());
				resultsLog.append(',');

				long endtime = System.currentTimeMillis();
				System.out.println(inFile.getName() + "-- run [" + i + "] -- " + ((endtime-starttime)/1000.0) + " seconds -- " + algorithm.toString());
			}

			double mean = total / runs;

			// Work out standard deviation.
			double stdDev = 0;
			for(int k = 0; k < values.length; k++)
			{
				values[k] = values[k] - mean;
				values[k] *= values[k];
				stdDev += values[k];
			}
			stdDev /= runs;
			stdDev = Math.sqrt(stdDev);

			resultsLog.append(System.lineSeparator());
			resultsLog.append("Mean cost: ");
			resultsLog.append(mean);
			resultsLog.append(System.lineSeparator());
			resultsLog.append("Std Deviation: ");
			resultsLog.append(stdDev);
			resultsLog.append(System.lineSeparator());
			resultsLog.append("Best path:");
			resultsLog.append(System.lineSeparator());
			resultsLog.append(optimal.toString());
			resultsLog.append(System.lineSeparator());
			resultsLog.append(System.lineSeparator());

			System.out.println(inFile.getPath());
			System.out.println(inFile.getPath() + " :: " + algorithm.toString() + " mean: " + mean + '\n');
		}

		System.out.println(resultsLog.toString());

		try
		{
			StringBuilder fileName = new StringBuilder();
			fileName.append(algorithm.toString());
			fileName.append(",pop_");
			fileName.append(populationSize);
			fileName.append(",gen_");
			fileName.append(generations);
			fileName.append(",runs_");
			fileName.append(runs);

			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("output/" + fileName.toString() + ".txt")));
			bw.write(resultsLog.toString());
			bw.close();

			bw = new BufferedWriter(new FileWriter(new File("output/" + fileName.toString()+ "-generations.csv")));
			bw.write(generationLog.toString());
			bw.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
