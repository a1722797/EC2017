package ec2017.ga.general;
import java.util.List;

import ttp.TTPSolution;

/**
 * This interface is for the survivor selection method.
 * @author pat
 *
 */
public interface SurvivorSelectionMethod
{
	/**
	 * This method takes the previous generation, as well as the new generation and returns a set of
	 * survivors.
	 * @param oldGeneration Parent generation.
	 * @param newGeneration New generation.
	 * @param size Size of the resulting population.
	 * @return
	 */
	public List<TTPSolution> select(List<TTPSolution> oldGeneration, List<TTPSolution> newGeneration, int size);
}
