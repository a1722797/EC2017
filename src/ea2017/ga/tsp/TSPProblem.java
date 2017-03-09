package ea2017.ga.tsp;

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
		cities.add(new SymCity("a", 34, 90));
		cities.add(new SymCity("b", 2, 34));
		cities.add(new SymCity("c", 77, 64));
		cities.add(new SymCity("d", 84, 26));
		cities.add(new SymCity("e", 45, 3));
		cities.add(new SymCity("f", 10, 45));
		cities.add(new SymCity("g", 1, 10));
		
		return cities;
	}

}
