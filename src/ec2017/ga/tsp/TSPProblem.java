package ec2017.ga.tsp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import ec2017.ga.general.Population;
import ec2017.ga.general.PopulationFactory;
import ec2017.ga.general.Symbol;
import ec2017.ga.general.selection.InterOverParentSelectionMethod;
import ec2017.ga.general.selection.InterOverSurvivorSelectionMethod;
import ec2017.ga.general.selection.RankBasdedParentSelectionMethod;
import ec2017.ga.general.selection.RoundRobinSurvivorSelectionMethod;
import ec2017.ga.general.selection.SUSParentSelectionMethod;
import ec2017.ga.general.selection.WindowingFPSParentSelectionMethod;
import ec2017.ga.general.variation.CrossOverCycle;
import ec2017.ga.general.variation.InterOverOp;
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
		runInterOver();
		
		// Example code
//		String filename = "default";
//		int generations = 10000;
//		int populationSize = 50;
		// TODO read values from args;
		
//		
//		ArrayList<Symbol> cities = readCitiesFromFile(filename);
//		
//		// Here we're using a factory to create a population
//		// which evolves according to the operations and 
//		// selection methods we want to use in this algorithm.
//		PopulationFactory pf = new PopulationFactory();
//		pf.setSymbols(cities);
//		pf.setPopulationSize(populationSize);
//		pf.setCrossOverOperator(new InterOverOp());
//		pf.setMutateOperator(null);
//		pf.setParentSelectionMethod(new InterOverParentSelectionMethod());
//		pf.setSurvivorSelectionMethod(new InterOverSurvivorSelectionMethod());
//		
//		Population population = pf.createPopulation(new Path());
//		
//		// Do our evolution
//		for (int i = 0; i < generations; i++)
//		{
//			population.evolve();
//		}
//		
//		Individual bestPath = population.getFittest();
//		System.out.println(bestPath.toString());
	}
	
	/**
	 * Reads our list of cities from a given file.
	 * @param filename The location of the file
	 * @return Cities contained.
	 */
	private static ArrayList<Symbol> readCitiesFromFile(String filename)
	{
		//TODO
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
	
	private static void runInterOver()
	{
		int populationSize = 20;
		int generations = 2000;
		
		StringBuilder sb = new StringBuilder();
		File inputFolder = new File("TSP_data");
		for(File inFile : inputFolder.listFiles())
		{
			sb.append("Input: ");
			sb.append(inFile.getName());
			sb.append("\n");
			sb.append("Costs: \n");
			
			System.out.println("Reading..." + inFile.getName());
			
			double total = 0;
			double shortest = Double.MAX_VALUE;
			Path optimal = null;
			
			ArrayList<Symbol> cities = readCitiesFromFile(inFile.getPath());
			
			// Run 30x per file
			final int RUNS = 3;
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
				pf.setCrossOverOperator(new CrossOverCycle());
				pf.setMutateOperator(new MutateInversion());
				pf.setParentSelectionMethod(new RankBasdedParentSelectionMethod());
				pf.setSurvivorSelectionMethod(new RoundRobinSurvivorSelectionMethod(10));
				
				Population population = pf.createPopulation(new Path());
				
				
				// Do our evolution
				for (int j = 0; j < generations; j++)
				{
					population.evolve();
				}
				
				Path bestPath = (Path)population.getFittest();
				if(bestPath.getDistance() < shortest)
				{
					optimal = bestPath;
					shortest = bestPath.getDistance();
				}
				
				total += bestPath.getDistance();
				values[i] = bestPath.getDistance();
				sb.append(bestPath.getDistance());
				sb.append(',');
				
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
			
			sb.append("\nMean cost: ");
			sb.append(mean);
			sb.append("\nStd Deviation: ");
			sb.append(stdDev);
			sb.append("\nBest path:\n");
			sb.append(optimal.toString());
			sb.append("\n\n");
			
			System.out.println(inFile.getPath());
			System.out.println("Mean: " + mean + '\n');
		}
		
		System.out.println(sb.toString());
		
		try
		{
			/////////////////////////////////////////////////////////////// Change this filename
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("output/CrossOverCycle-MutateInversion-RankBasdedParentSelectionMethod-RoundRobinSurvivorSelectionMethod.txt")));
			bw.write(sb.toString());
			bw.close();
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
