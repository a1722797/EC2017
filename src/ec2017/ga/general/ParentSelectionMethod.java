package ec2017.ga.general;

import java.util.ArrayList;

/**
 * An interface for the parent selection method.
 * @author pat
 *
 */
public interface ParentSelectionMethod 
{
	/**
	 * This method creates a mating pool from an existing generation of individuals.
	 * @param population The current generation
	 * @return The new mating pool
	 */
	public ArrayList<Individual> select(ArrayList<Individual> population);
}
