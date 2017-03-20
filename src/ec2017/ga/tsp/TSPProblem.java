package ec2017.ga.tsp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import ec2017.ga.general.CrossOverOperator;
import ec2017.ga.general.MutateOperator;
import ec2017.ga.general.ParentSelectionMethod;
import ec2017.ga.general.Population;
import ec2017.ga.general.PopulationFactory;
import ec2017.ga.general.SurvivorSelectionMethod;
import ec2017.ga.general.Symbol;
import ec2017.ga.general.selection.WindowingFPSParentSelectionMethod;
import ec2017.ga.general.selection.E1337ismSurvivorSelect;
import ec2017.ga.general.variation.CrossOverCycle;
import ec2017.ga.general.variation.InverOverOp;
import ec2017.ga.general.variation.MutateInversion;

/**
 * The main entry point for our program.
 * @author pat
 *
 */
public class TSPProblem
{
	public static void main(String[] args) 
	{
		runTests(
			new CrossOverCycle(),
			new MutateInversion(),
			new WindowingFPSParentSelectionMethod(),
			new E1337ismSurvivorSelect());
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
	
	private static void runTests(
			CrossOverOperator xop,
			MutateOperator mop,
			ParentSelectionMethod pmeth,
			SurvivorSelectionMethod smeth)
	{
		int populationSize = 50;
		int generations = 10000;
		
		StringBuilder resultsLog = new StringBuilder();
		StringBuilder generationLog = new StringBuilder();
		generationLog.append("File,2000gen,5000gen,10000gen,20000gen");
		
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
			
			// Run 30x per file
			final int RUNS = 5;
			double[] values = new double[RUNS];

			for (int i = 0; i < RUNS; i++)
			{
				long starttime = System.currentTimeMillis();
				// Here we're using a factory to create a population
				// which evolves according to the operations and 
				// selection methods we want to use in this algorithm.
				PopulationFactory pf = new PopulationFactory();
				pf.setSymbols(cities);
				pf.setPopulationSize(populationSize);
				pf.setCrossOverOperator(xop);
				pf.setMutateOperator(mop);
				pf.setParentSelectionMethod(pmeth);
				pf.setSurvivorSelectionMethod(smeth);
				
				Population population = pf.createPopulation(new Path());
				
				// The InverOverOp needs a reference to the population,
				// obviously we need to wait till we have a population.
				if (InverOverOp.class.isAssignableFrom(mop.getClass()))
				{
					InverOverOp iop = (InverOverOp)mop;
					iop.setPopulation(population);
				}
//				population.setCrossOverProbability(0.25);
//				population.setMutationProbability(0.75);
				
				generationLog.append(inFile.getName());
				generationLog.append(',');
				
				// Do our evolution
				for (int j = 0; j < generations; j++)
				{
					population.evolve();
					if (j == 2000 || j == 5000 || j%10000 == 0)
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
				System.out.println("Run [" + i + "] -- " + ((endtime-starttime)/1000.0) + " seconds");
			}
			
			double mean = total / RUNS;
			
			// Work out standard deviation.
			double stdDev = 0;
			for(int k = 0; k < values.length; k++)
			{
				values[k] = values[k] - mean;
				values[k] *= values[k];
				stdDev += values[k];
			}
			stdDev /= RUNS;
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
			System.out.println("Mean: " + mean + '\n');
		}
		
		System.out.println(resultsLog.toString());
		
		try
		{
			StringBuilder fileName = new StringBuilder();
			if (xop != null)fileName.append(xop.getClass().getSimpleName());
			else fileName.append("none");
			fileName.append('-');
			if (mop != null)fileName.append(mop.getClass().getSimpleName());
			else fileName.append("none");
			fileName.append('-');
			fileName.append(pmeth.getClass().getSimpleName());
			fileName.append('-');
			fileName.append(smeth.getClass().getSimpleName());
			
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
