package ec2017.ga.general.selection;

import java.util.ArrayList;

import ec2017.ga.general.Individual;
import ec2017.ga.general.ParentSelectionMethod;

/**
 * This is a stub implementation for testing. Returns a copy of the given population.
 * @author pat
 *
 */
public class NoParentSelectionMethod implements ParentSelectionMethod
{

	@Override
	public ArrayList<Individual> select(ArrayList<Individual> population) 
	{
		return new ArrayList<Individual>(population);
	}

}
