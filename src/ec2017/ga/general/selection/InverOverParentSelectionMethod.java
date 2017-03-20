package ec2017.ga.general.selection;

import java.util.ArrayList;

import ec2017.ga.general.Individual;
import ec2017.ga.general.ParentSelectionMethod;


public class InverOverParentSelectionMethod implements ParentSelectionMethod
{
	/**
	 * We just use the base population as our mating pool.
	 */
	@Override
	public ArrayList<Individual> select(ArrayList<Individual> population) 
	{
		return new ArrayList<Individual>(population);
	}

}