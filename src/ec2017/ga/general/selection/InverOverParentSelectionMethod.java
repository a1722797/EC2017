package ec2017.ga.general.selection;

import java.util.ArrayList;
import java.util.List;

import ec2017.ga.general.ParentSelectionMethod;
import ttp.TTPSolution;


public class InverOverParentSelectionMethod implements ParentSelectionMethod
{
	/**
	 * We just use the base population as our mating pool.
	 */
	@Override
	public List<TTPSolution> select(List<TTPSolution> population)
	{
		return new ArrayList<TTPSolution>(population);
	}

}