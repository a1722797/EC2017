package ec2017.ga.general;

import java.util.ArrayList;

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
    public ArrayList<ArrayList<Symbol>> crossOver(ArrayList<Symbol> parentA, ArrayList<Symbol> parentB);
}
