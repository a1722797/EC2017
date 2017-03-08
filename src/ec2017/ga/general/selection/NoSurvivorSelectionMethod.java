package ec2017.ga.general.selection;

import java.util.ArrayList;

import ec2017.ga.general.Individual;
import ec2017.ga.general.SurvivorSelectionMethod;

/**
 * This is a stub implementation for testing.
 * @author pat
 *
 */
public class NoSurvivorSelectionMethod implements SurvivorSelectionMethod
{

	@Override
	public ArrayList<Individual> select(
			ArrayList<Individual> oldGeneration, ArrayList<Individual> newGeneration,
			int size) 
	{
		return new ArrayList<Individual>(oldGeneration);
	}

}
