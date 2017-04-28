package ec2017.ga.general;

import ttp.TTPInstance;
import ttp.TTPSolution;

/**
 * The MutateOperator interface provides a method for mutating a genotype, thus creating new information
 * in the gene pool.
 * @author pat
 *
 */
public interface MutateOperator
{
	/**
	 * This method returns a mutated version of the given genotype.
	 * @param genotype
	 * @return
	 */
	public TTPSolution mutate(TTPSolution genotype);

	public void setInstance(TTPInstance ttp);
}
