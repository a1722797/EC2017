package ec2017.ga.general;

import java.util.ArrayList;

/**
 * This represents an individual phenotype within a population.
 * @author pat
 *
 */
public interface Individual extends Comparable<Individual>
{
	/**
	 * Creates a new individual from the provided genotype.
	 * @param genotype The type of symbol should be as expected by the implementation.
	 * @return
	 */
	public Individual create(ArrayList<Symbol> genotype);
	
	/**
	 * The fitness of the individual. A higher value means a more fit individual. 
	 * @return Fitness, this may be a negative value.
	 */
	public double getFitness();
	
	/**
	 * The genotype is the underlying representation of the genetic data, which is used
	 * for mutation and cross-over operations.
	 * @return
	 */
	public ArrayList<Symbol> getGenotype();
	
	/**
	 * Will create a mutated copy of the individual using the given mutate operation.
	 * @param mutateOp Mutate operation.
	 * @return Mutated copy
	 */
	public Individual mutate(MutateOperator mutateOp);
	
	/**
	 * Will use the given cross-over operation to create a child with another individual.
	 * @param otherParent The other individual to cross-over.
	 * @param crossOp The cross-over operation
	 * @return A new child Individual
	 */
	public Individual crossOver(Individual otherParent, CrossOverOperator crossOp);
}
