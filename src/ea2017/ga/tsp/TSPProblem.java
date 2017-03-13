package ea2017.ga.tsp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import ec2017.ga.general.Individual;
import ec2017.ga.general.Population;
import ec2017.ga.general.PopulationFactory;
import ec2017.ga.general.Symbol;
import ec2017.ga.general.selection.NoParentSelectionMethod;
import ec2017.ga.general.selection.NoSurvivorSelectionMethod;
import ec2017.ga.general.variation.MutateNop;

/**
 * The main entry point for our program.
 * @author pat
 *
 */
public class TSPProblem
{
	public static void main(String[] args) 
	{
		String filename = "default";
		int generations = 50;
		int populationSize = 1;
		// TODO read values from args;
		
		ArrayList<Symbol> cities = readCitiesFromFile(filename);
		
		// Here we're using a factory to create a population
		// which evolves according to the operations and 
		// selection methods we want to use in this algorithm.
		PopulationFactory pf = new PopulationFactory();
		pf.setSymbols(cities);
		pf.setPopulationSize(populationSize);
		pf.setCrossOverOperator(null);
		pf.setMutateOperator(new MutateNop());
		pf.setParentSelectionMethod(new NoParentSelectionMethod());
		pf.setSurvivorSelectionMethod(new NoSurvivorSelectionMethod());
		
		Population population = pf.createPopulation(new Path());
		
		// Do our evolution
		for (int i = 0; i < generations; i++)
		{
			population.evolve();
		}
		
		Individual bestPath = population.getFittest();
		System.out.println(bestPath.toString());
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
	
		//File file = new File ("TSP_data/eil51.tsp");
		File file = new File ("TSP_data/pr2392.tsp");
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

}
