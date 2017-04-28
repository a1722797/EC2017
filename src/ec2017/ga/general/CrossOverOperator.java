package ec2017.ga.general;
import java.util.List;

import ttp.TTPInstance;
import ttp.TTPSolution;

/**
 * Interface for Cross-over operations.
 * @author pat
 *
 */
public interface CrossOverOperator
{
	/**
	 * This method will construct two new child genotype from two parent genotypes.
	 * @param parentA First parent genotype
	 * @param parentB Second parent genotype
         * @param childA Output for first child
         * @param childB Output for second child
	 */
    public List<TTPSolution> crossOver(TTPSolution parentA, TTPSolution parentB);

    public void setInstance(TTPInstance ttp);
}
