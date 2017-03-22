package ec2017.ga.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

/**
 * The Population class represents an evolving population of individuals.
 * @author pat
 *
 */
public class Population 
{
	protected ArrayList<Symbol> _symbols;
	protected int _populationSize;
	private Algorithm _algorithm;
	
	ArrayList<Individual> _population = new ArrayList<Individual>();

	/**
	 * 
	 * @param symbols
	 * @param populationSize
	 * @param populationSeed
	 * @param algorithm
	 */
	public Population(
			ArrayList<Symbol> symbols, int populationSize, Individual populationSeed, Algorithm algorithm)
	{
		_symbols = symbols;
		_populationSize = populationSize;
		_algorithm = algorithm;
		
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
		ArrayList<Individual> matingPool = _algorithm.getParentSelect().select(_population);
		ArrayList<Individual> newGeneration;
		
		// We want to populate a new generation, if we have a cross over operation we'll
		// use that to breed the mating pool.
		newGeneration = new ArrayList<Individual>();
		
		//Shuffle the mating pool to make sure our mating is random.
		ArrayList<Individual> shuffledMatingPool = new ArrayList<Individual>(matingPool);
		Collections.shuffle(shuffledMatingPool);
		Iterator<Individual> it = shuffledMatingPool.iterator();
		for(Individual parent : matingPool)
		{
			Boolean mutate = Math.random() <= _algorithm.getPMutate();
			Boolean xover = Math.random() <= _algorithm.getPXOver();
			
			if(_algorithm.getCrossOver() != null && xover)
			{
				// Sometimes parentA may be parentB, but this maintains ordering, making the InterOverOp easier.
				Individual parentB = it.next();
				ArrayList<Individual> children = parent.crossOver(parentB, _algorithm.getCrossOver());
				for (Individual child : children) newGeneration.add(child);
			}
			
			// If we have a mutation operation, we're going to use it here.
			if (_algorithm.getMutate() != null && mutate)
			{
				newGeneration.add(parent.mutate(_algorithm.getMutate()));
			}
			
			if (!xover)it.next();
		}
		
		// And we work out who survives using our survivor selection method.
		ArrayList<Individual> newPopulation = 
			_algorithm.getSurvivorSelect().select(matingPool, newGeneration, _populationSize);
		
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

	public Individual getRandom() 
	{
		Random rand = new Random();
		return _population.get(rand.nextInt(_population.size()));
	}
}
