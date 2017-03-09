package ec2017.ga.general;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The Population class represents an evolving population of individuals.
 * @author pat
 *
 */
public class Population 
{
	protected ArrayList<Symbol> _symbols;
	protected int _populationSize;
	protected CrossOverOperator _crossOverOp = null;
	protected MutateOperator _mutateOp = null;
	protected ParentSelectionMethod _parentSelect;
	protected SurvivorSelectionMethod _survivorSelect;
	
	ArrayList<Individual> _population = new ArrayList<Individual>();

	/**
	 * This is package protected since it should only be used by the PopulationFactory.
	 * @param symbols Our symbol set
	 * @param populationSize Expected size of the population
	 * @param crossOverOp Cross over operation
	 * @param mutateOperator Mutate operation
	 * @param parentSelect Parent selection method
	 * @param survivorSelect Survivor selection method
	 * @param populationSeed We require an instance of an individual to create new instances. This mother is used to bootstrap the initial population.
	 */
	Population(
			ArrayList<Symbol> symbols, int populationSize, CrossOverOperator crossOverOp,
			MutateOperator mutateOperator, ParentSelectionMethod parentSelect, SurvivorSelectionMethod survivorSelect,
			Individual populationSeed)
	{
		_symbols = symbols;
		_populationSize = populationSize;
		_crossOverOp = crossOverOp;
		_mutateOp = mutateOperator;
		_parentSelect = parentSelect;
		_survivorSelect = survivorSelect;
		
		generatePopulation(populationSeed);
	}
	
	/**
	 * Generates a new, randomised population.
	 * @param mother The seed individual, which will provide the sub-type. 
	 * 				 The mother will not be present in the population. 
	 */
	private void generatePopulation(Individual mother)
	{
		for (int i = 0; i < _populationSize; i++)
		{
			ArrayList<Symbol> genotype = new ArrayList<Symbol>(_symbols); 
			Collections.shuffle(genotype);
			_population.add(mother.create(genotype));
		}
	}

	/**
	 * This method triggers a new generation to be formed from the population.
	 */
	public void evolve() 
	{
		// First, we're going to use our parent selection method to creat a mating pool
		ArrayList<Individual> matingPool = _parentSelect.select(_population);
		ArrayList<Individual> newGeneration;
		
		// We want to populate a new generation, if we have a cross over operation we'll
		// use that to breed the mating pool.
		if(_crossOverOp != null)
		{
			newGeneration = new ArrayList<Individual>();
			
			//Shuffle the mating pool to make sure our mating is random.
			Collections.shuffle(matingPool);
			
			// Get the last parent.
			Individual previousParent = matingPool.get(matingPool.size()-1);
			// Put on some Barry White and leave them to it...
			for(int i = 0; i < matingPool.size(); i++)
			{
				Individual partent = matingPool.get(i);
				Individual child = previousParent.crossOver(partent, _crossOverOp);
				newGeneration.add(child);
			}
		}
		// Otherwise we'll just use the mating pool as the new generation.
		else
		{
			newGeneration = matingPool;
		}
		
		// If we have a mutation operation, we're going to use it here.
		if (_mutateOp != null)
		{
			// Create a mutated version of our new generation.
			ArrayList<Individual> mutantGeneration = new ArrayList<Individual>();
			for(Individual child : newGeneration)
			{
				mutantGeneration.add(child.mutate(_mutateOp));
			}
			
			newGeneration = mutantGeneration;
		}
		
		// And we work out who survives using our survivor selection method.
		ArrayList<Individual> newPopulation = _survivorSelect.select(_population, newGeneration, _populationSize);
		
		// Certain combinations might give us less survivors than we need, so we'll complain.
		if(newPopulation.size() != _populationSize)
		{
			throw new RuntimeException("Current SurvivorSelectionMethod is not producing the correct population size");
		}
		
		// And our little babies are all grown up.
		_population = newPopulation;
	}

	/**
	 * 
	 * @return The fittest individual of the current generation.
	 */
	public Individual getFittest()
	{
		// We can use this since Individual compares for fitness.
		return Collections.max(_population);
	}
}
