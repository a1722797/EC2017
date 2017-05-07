package ec2017.ass2.ex3;

import java.util.Arrays;

import ttp.TTPInstance;
import ttp.TTPSolution;

public class TrackingPlan implements Algorithm
{
	TTPSolution _best;
	TTPInstance _instance;
	
	public TrackingPlan(TTPInstance instance)
	{
		_instance = instance;
	}

	@Override
	public TTPSolution iterate(int[] tour)
	{
		if (_best == null)
		{
			int[] plan = new int[_instance.numberOfItems];
			double pVal = 1.0/_instance.rentingRatio;
			for (int i = 0; i < plan.length; i++)
			{
				if(Math.random() < pVal) plan[i] = 1;
			}
	
			_best = new TTPSolution(tour, plan);
		}
		
		int[] plan = Arrays.copyOf(_best.packingPlan, _best.packingPlan.length);
		double pVal = 5.0/plan.length;
		
		int count = 0;
		TTPSolution sol = null;
		
		while (count++ < 10)
		{
			for(int i = 0; i < plan.length; i++)
			{
				if(Math.random() < pVal)
				{
					if(plan[i] == 1 && Math.random() < 10.0 / (i+1)) plan[i] = 0;
					else if (Math.random() > 10.0 / (i+1)) plan[i] = 1;
				}
			}
			
			sol = new TTPSolution(tour, plan);
			_instance.evaluate(sol);
			_instance.evaluate(_best);
			if (sol.ob > _best.ob && sol.wend >= 0)
			{
				_best = sol;
				return sol;
			}
		}
		
		return sol;
	}

	@Override
	public TTPSolution getBest()
	{
		
		return _best;
	}

	public void exchange(int start, int end) 
	{
		int[] packingPlan = _best.packingPlan;
		
		int itemsPer = _instance.numberOfItems / _instance.numberOfNodes;
		for(int i = 0; i < itemsPer; i++)
		{
			int temp = packingPlan[start+i];
			packingPlan[start+i] = packingPlan[end+i];
			packingPlan[end+i] = temp;
		}
		_best.packingPlan = packingPlan;
	}

	public void twoOpt(int start, int end) 
	{
		int[] packingPlan = _best.packingPlan;
		
		int itemsPer = _instance.numberOfItems / _instance.numberOfNodes;
		while(start < end)
		{
			for(int i = 0; i < itemsPer; i++)
			{
				int temp = packingPlan[start+i];
				packingPlan[start+i] = packingPlan[end+i];
				packingPlan[end+i] = temp;
			}
			
			start++; 
			end--;
		}
		
		_best.packingPlan = packingPlan;
	}

}
