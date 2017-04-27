package ec2017.ga.general.selection;

import java.util.ArrayList;
import java.util.List;

import ec2017.ga.general.SurvivorSelectionMethod;
import ttp.TTPSolution;

/**
 * This is a stub implementation for testing.
 * @author pat
 *
 */
public class NoSurvivorSelectionMethod implements SurvivorSelectionMethod
{
	/**
	 * Stub implementation. Returns a shallow copy of the oldGeneration.
	 */
	@Override
	public List<TTPSolution> select(
			List<TTPSolution> oldGeneration, List<TTPSolution> newGeneration,
			int size)
	{
		return new ArrayList<TTPSolution>(oldGeneration);
	}

}
