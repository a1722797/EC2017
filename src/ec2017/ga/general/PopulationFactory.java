package ec2017.ga.general;

import java.util.ArrayList;

/**
 * The PopulationFactory produces a new population, with some additional checks.
 * @author pat
 *
 */
public class PopulationFactory 
{
	private ArrayList<Symbol> _symbols = null;
	private int _populationSize = 100; //default
	private CrossOverOperator _crossOverOp = null;
	private MutateOperator _mutateOp = null;
	private ParentSelectionMethod _parentSelect = null;
	private SurvivorSelectionMethod _survivorSelect = null;
	private double _pMutate = 1;
	private double _pXOver = 1;
	
	/**
	 * The initial set of symbols for our genotype.
	 * @param symbols
	 */
	public void setSymbols(ArrayList<Symbol> symbols) 
	{
		_symbols = symbols;
	}

	/**
	 * Sets the population size.
	 * @param populationSize
	 */
	public void setPopulationSize(int populationSize) 
	{
		_populationSize = populationSize;
	}

	/**
	 * Sets the cross-over operation. This is optional if a mutate operator is set.
	 * @param crossOverOp
	 */
	public void setCrossOverOperator(CrossOverOperator crossOverOp)
	{
		_crossOverOp = crossOverOp;
	}
	
	/**
	 * Sets the mutate operator. This is optional if a cross-over operation has been set.
	 * @param mutateOp
	 */
	public void setMutateOperator(MutateOperator mutateOp)
	{
		_mutateOp = mutateOp;
	}

	/**
	 * Sets a parent selection method.
	 * @param selectionMethod
	 */
	public void setParentSelectionMethod(ParentSelectionMethod selectionMethod) 
	{
		_parentSelect = selectionMethod;
	}

	/**
	 * Sets a survivor selection method.
	 * @param selectionMethod
	 */
	public void setSurvivorSelectionMethod(SurvivorSelectionMethod selectionMethod) 
	{
		_survivorSelect = selectionMethod;
	}
	
	/**
	 * Sets the probability (between 0 and 1) that an individual will mutate each generation.
	 * @param pVal
	 */
	public void setMutateProbability(double pVal)
	{
		_pMutate = pVal;
	}
	
	/**
	 * Sets the probability (between 0 and 1) that the an individual will cross over each generation.
	 * @param pVal
	 */
	public void setCrossOverProbability(double pVal)
	{
		_pXOver = pVal;
	}

	/**
	 * Checks that everything has been set and creates a new population.
	 * @param mother Seed for the base-population, the mother will not be present in the initial generation.
	 * @return
	 */
	public Population createPopulation(Individual mother)
	{
		if (_symbols == null)
			throw new IllegalArgumentException("Population cannot be created without symbols");
		if (_crossOverOp == null && _mutateOp == null)
			throw new IllegalArgumentException("Population cannot be created without a cross-over or mutation operation");
		if (_parentSelect == null)
			throw new IllegalArgumentException("You must determine a parent selection method.");
		if (_survivorSelect == null)
			throw new IllegalArgumentException("Population cannot be created without a survivor selection method");
		
		Population pop = new Population(_symbols, _populationSize, _crossOverOp, _mutateOp, _parentSelect, _survivorSelect, mother);
		pop.setCrossOverProbability(_pXOver);
		pop.setMutationProbability(_pMutate);
		
		return pop;
	}

}
