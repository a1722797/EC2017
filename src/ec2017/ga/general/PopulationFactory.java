package ec2017.ga.general;

import java.util.ArrayList;

public class PopulationFactory 
{
	private ArrayList<Symbol> _symbols = null;
	private int _populationSize = 100; //default
	private CrossOverOperator _crossOverOp = null;
	private MutateOperator _mutateOp = null;
	private ParentSelectionMethod _parentSelect = null;
	private SurvivorSelectionMethod _survivorSelect = null;
	
	public void setSymbols(ArrayList<Symbol> symbols) 
	{
		_symbols = symbols;
	}

	public void setPopulationSize(int populationSize) 
	{
		_populationSize = populationSize;
	}

	public void setCrossOverOperator(CrossOverOperator crossOverOp)
	{
		_crossOverOp = crossOverOp;
	}
	
	public void setMutateOperator(MutateOperator mutateOp)
	{
		_mutateOp = mutateOp;
	}

	public void setParentSelectionMethod(ParentSelectionMethod selectionMethod) 
	{
		_parentSelect = selectionMethod;
	}

	public void setSurvivorSelectionMethod(SurvivorSelectionMethod selectionMethod) 
	{
		_survivorSelect = selectionMethod;
	}

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
		
		return new Population(_symbols, _populationSize, _crossOverOp, _mutateOp, _parentSelect, _survivorSelect, mother);
	}

}
