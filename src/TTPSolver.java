

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ec2017.ga.general.*;
import ttp.TTPInstance;
import ttp.TTPSolution;
import ttp.Optimisation.Optimisation;

/**
 * The main entry point for our program.
 * @author pat
 *
 */
public class TTPSolver
{
	// Setup our thread pool.
	static ExecutorService _executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		// Load the parameters from config.properties
		File configFile = new File("config.properties");
		Properties prop = new Properties();
		prop.load(new FileReader(configFile));

		// For each operator, dynamically load and instantiate the class we've been asked to use

		// There's no nop crossover operator, so if one hasn't been configured we default to null (which has the same effect)
		CrossOverOperator crossover = null;
		// We don't want to make people put fully-qualified classnames in the config file, so we add that bit here
		String crossoverClassName = prop.getProperty("crossover");
		if (crossoverClassName != null) {
			crossoverClassName = "ec2017.ga.general.variation." + crossoverClassName;
			// If a crossover operator has been configured, we load the class and call the no-argument constructor
			Class<? extends CrossOverOperator> crossoverClass =
					(Class<? extends CrossOverOperator>) Class.forName(crossoverClassName);
			crossover = crossoverClass.getConstructor().newInstance();
		}

		// Same here for the mutation operator, except we default to MutateNop instead of null
		String mutationClassName = "ec2017.ga.general.variation." + prop.getProperty("mutate", "MutateNop");
		Class<? extends MutateOperator> mutateClass =
				(Class<? extends MutateOperator>) Class.forName(mutationClassName);
		MutateOperator mutator = mutateClass.getConstructor().newInstance();

		// Same here for the parent selection operator, defaulting to NoParentSelectionMethod
		String parentSelectionClassName = "ec2017.ga.general.selection." + prop.getProperty("parent-selection", "NoParentSelectionMethod");
		Class<? extends ParentSelectionMethod> parentSelectionClass =
				(Class<? extends ParentSelectionMethod>) Class.forName(parentSelectionClassName);
		ParentSelectionMethod parentSelector = parentSelectionClass.getConstructor().newInstance();

		// Same here for the parent selection operator, defaulting to NoSurvivorSelectionMethod
		String survivorSelectionClassName = "ec2017.ga.general.selection." + prop.getProperty("survivor-selection", "NoSurvivorSelectionMethod");
		Class<? extends SurvivorSelectionMethod> survivorSelectionClass =
				(Class<? extends SurvivorSelectionMethod>) Class.forName(survivorSelectionClassName);
		SurvivorSelectionMethod survivorSelector = survivorSelectionClass.getConstructor().newInstance();

		// Load the population size, number of generations, and how many times to run the EA,
		// with reasonable defaults
		int population = new Integer(prop.getProperty("population", "50"));
		int runtime = new Integer(prop.getProperty("runtime", "600"));
		int runs = new Integer(prop.getProperty("runs", "1"));

		boolean dynamicItems = new Boolean(prop.getProperty("dynamicItems", "false"));
		boolean dynamicTours = new Boolean(prop.getProperty("dynamicTours", "false"));

		// Create the algorithm
		Algorithm algorithm = new Algorithm(
				crossover,
				mutator,
				parentSelector,
				survivorSelector);

		// Run it
		runTests(algorithm, population, runtime, runs);

		if (!dynamicItems && !dynamicTours) {
			runOptimisation(runtime);
		}

        System.out.println("************* Done *************");

	}

	private static void runTests(
			Algorithm algorithm,
			int populationSize,
			int runtime,
			int runs)
	{
		StringBuilder resultsLog = new StringBuilder();
		StringBuilder generationLog = new StringBuilder();

		int runtimemilli = runtime * 1000 - 200;

		File inputFolder = new File("TTPdata");
		for(File inFile : inputFolder.listFiles())
		{
			if (!inFile.getAbsolutePath().endsWith(".ttp")) {
				continue;
			}

			resultsLog.append("Input: ");
			resultsLog.append(inFile.getName());
			resultsLog.append(System.lineSeparator());
			resultsLog.append("Costs:");
			resultsLog.append(System.lineSeparator());

			System.out.println("Reading..." + inFile.getName());

			double total = 0;
			double best = Double.MIN_VALUE;
			TTPSolution optimal = null;

			TTPInstance ttp = new TTPInstance(inFile);

			int[] start_tour = Optimisation.linkernTour(ttp);

			double[] values = new double[runs];

			for (int i = 0; i < runs; i++)
			{
				long starttime = System.currentTimeMillis();

				Population population =
					new Population(ttp, populationSize, algorithm);

				population.setTour(start_tour);

				generationLog.append(inFile.getName());
				generationLog.append(',');

				// Do our evolution
				int j = 0;
				while (System.currentTimeMillis() - starttime < runtimemilli) {
					population.evolve();
					if (j == 1 || j % 500 == 0)
					{
						TTPSolution solution = population.getFittest();
						generationLog.append(solution.ob);
						generationLog.append(',');
					}
					j++;
				}

				generationLog.append(System.lineSeparator());

				TTPSolution bestSolution = population.getFittest();
				if(bestSolution.ob > best)
				{
					optimal = bestSolution;
					best = bestSolution.ob;
				}

				total += bestSolution.ob;
				values[i] = bestSolution.ob;
				resultsLog.append(bestSolution.ob);
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
			fileName.append(",time_");
			fileName.append(runtime);
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

	private static void runOptimisation(int runtime) {
		System.out.println("**************** Optimisation Start *************");

		StringBuilder resultsLog = new StringBuilder();

		int runtimemilli = runtime * 1000;
		File inputFolder = new File("TTPdata");
		for(File inFile : inputFolder.listFiles())
		{
			if (!inFile.getAbsolutePath().endsWith(".ttp")) {
				continue;
			}

			System.out.println(inFile.getName());
			resultsLog.append(inFile.getName());
			resultsLog.append(System.lineSeparator());

			TTPInstance instance = new TTPInstance(inFile);

			int[] tour = Optimisation.linkernTour(instance);

			TTPSolution solution = Optimisation.hillClimber(instance, tour, 1, 1000, runtimemilli);

			System.out.println(solution.getObjective());
			System.out.println("");
			resultsLog.append(solution.getObjective());
			resultsLog.append(System.lineSeparator());
			resultsLog.append(System.lineSeparator());
		}

		try {
			StringBuilder fileName = new StringBuilder();
			fileName.append("Optimisation");
			fileName.append(",time_");
			fileName.append(runtime);

			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("output/" + fileName.toString() + ".txt")));
			bw.write(resultsLog.toString());
			bw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
