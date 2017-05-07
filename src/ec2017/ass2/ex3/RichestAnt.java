package ec2017.ass2.ex3;

import java.util.Arrays;

import ttp.TTPInstance;
import ttp.TTPSolution;

public class RichestAnt implements Algorithm
{
	public TTPSolution _bestSolution;
	private double[] _pVals;
	TTPInstance _instance;
	
	public RichestAnt(TTPInstance instance)
	{
		_pVals = new double[instance.numberOfItems];
		Arrays.fill(_pVals, 0.5);
		_instance = instance;
	}
	
	@Override
	public TTPSolution iterate(int[] tour)
	{
		int[] packingPlan = new int[_pVals.length];
		for(int i = 0; i < packingPlan.length; i++)
		{
			if(Math.random() < _pVals[i]) packingPlan[i] = 1;
			else packingPlan[i] = 0;
		}
		
		TTPSolution solution = new TTPSolution(tour, packingPlan);
		_instance.evaluate(solution);
		
		if(_bestSolution != null)
		{
			_instance.evaluate(_bestSolution);
			if(solution.ob > _bestSolution.ob && solution.wend >= 0) setBest(solution);
		}
		else
		{
			setBest(solution);	
		}
		
		return solution;
	}
	
	private void setBest(TTPSolution solution)
	{
		_bestSolution = solution;
		
		for(int i = 0; i < solution.packingPlan.length; i++)
		{
			if(solution.packingPlan[i] == 1)
			{
				_pVals[i] = _pVals[i] + 1.0/_pVals.length;
			}
			else
			{
				_pVals[i] = _pVals[i] - 1.0/_pVals.length;
			}
		}
	}
	
	@Override
	public TTPSolution getBest()
	{
		_instance.evaluate(_bestSolution);
		return _bestSolution;
	}
}
