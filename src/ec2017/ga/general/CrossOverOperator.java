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
	 * This method will construct a new child genotype from two parent genotypes. 
	 * @param parentA First parent genotype
	 * @param parentB Second parent genotype
	 * @return Child genotype
	 */
	public ArrayList<Symbol> crossOver(ArrayList<Symbol> parentA, ArrayList<Symbol> parentB);
}
