package ec2017.ga.general.selection;

import java.util.ArrayList;
import java.util.List;

import ec2017.ga.general.ParentSelectionMethod;
import ttp.TTPSolution;

/**
 * This is a stub implementation for testing. Returns a copy of the given population.
 * @author pat
 *
 */
public class NoParentSelectionMethod implements ParentSelectionMethod
{

	@Override
	public List<TTPSolution> select(List<TTPSolution> population)
	{
		return new ArrayList<TTPSolution>(population);
	}

}
