package ec2017.ga.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ttp.TTPInstance;
import ttp.TTPSolution;

/**
 * The Population class represents an evolving population of TTPSolutions.
 * @author pat
 *
 */
public class Population
{
	protected int _populationSize;
	private Algorithm _algorithm;
	private TTPInstance _ttp;

	List<TTPSolution> _population = new ArrayList<TTPSolution>();

	/**
	 *
	 * @param ttp
	 * @param populationSize
	 * @param algorithm
	 */
	public Population(TTPInstance ttp, int populationSize, Algorithm algorithm)
	{
		_populationSize = populationSize;
		_algorithm = algorithm;
		_ttp = ttp;

		generatePopulation();
	}

	/**
	 * Generates a new, randomised population.
	 */
	private void generatePopulation()
	{
		List<Integer> tour = new ArrayList<>(_ttp.numberOfNodes);
		for (int i = 0; i < _ttp.numberOfNodes; i++) {
			tour.add(i);
		}

		for (int i = 0; i < _populationSize; i++)
		{
			Collections.shuffle(tour);
			// We need to list the starting city at the end of the tour as well
			int[] tourArray = new int[_ttp.numberOfNodes+1];
			for (int j = 0; j < tourArray.length; j++) {
				tourArray[j] = tour.get(j % _ttp.numberOfNodes);
			}

			int[] packingPlan = new int[_ttp.numberOfItems];
			for (int j = 0; j < packingPlan.length; j++) {
				packingPlan[j] = Math.random() >= 0.5 ? 1 : 0;
			}

			TTPSolution solution = new TTPSolution(tourArray, packingPlan);
			_ttp.evaluate(solution);
			_population.add(solution);
		}
	}

	/**
	 * This method triggers a new generation to be formed from the population.
	 */
	public void evolve()
	{
		// First, we're going to use our parent selection method to create a mating pool
		List<TTPSolution> matingPool = _algorithm.getParentSelect().select(_population);
		List<TTPSolution> newGeneration;

		// We want to populate a new generation, if we have a cross over operation we'll
		// use that to breed the mating pool.
		newGeneration = new ArrayList<TTPSolution>();

		//Shuffle the mating pool to make sure our mating is random.
		ArrayList<TTPSolution> shuffledMatingPool = new ArrayList<TTPSolution>(matingPool);
		Collections.shuffle(shuffledMatingPool);
		Iterator<TTPSolution> it = shuffledMatingPool.iterator();
		for(TTPSolution parent : matingPool)
		{
			Boolean mutate = Math.random() <= _algorithm.getPMutate();
			Boolean xover = Math.random() <= _algorithm.getPXOver();

			if(_algorithm.getCrossOver() != null && xover)
			{
				// Sometimes parentA may be parentB, but this maintains ordering, making the InterOverOp easier.
				TTPSolution parentB = it.next();
				List<TTPSolution> children = _algorithm.getCrossOver().crossOver(parent, parentB);
				for (TTPSolution child : children) newGeneration.add(child);
			}

			// If we have a mutation operation, we're going to use it here.
			if (_algorithm.getMutate() != null && mutate)
			{
				newGeneration.add(_algorithm.getMutate().mutate(parent));
			}

			if (!xover)it.next();
		}

		// Calculate the fitness for all children before trying to select which ones to keep
		for (TTPSolution child : newGeneration) {
			_ttp.evaluate(child);
		}

		// And we work out who survives using our survivor selection method.
		List<TTPSolution> newPopulation =
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
	 * @return The fittest TTPSolution of the current generation.
	 */
	public TTPSolution getFittest()
	{
		// We can use this since TTPSolution compares for fitness.
		return Collections.max(_population);
	}

	public TTPSolution getRandom()
	{
		Random rand = new Random();
		return _population.get(rand.nextInt(_population.size()));
	}
}
