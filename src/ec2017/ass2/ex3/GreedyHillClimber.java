package ec2017.ass2.ex3;

import ttp.TTPInstance;
import ttp.TTPSolution;
import ttp.Utils.DeepCopy;

/**
 * This class was adapted from the ex2 implementation.
 *
 */
public class GreedyHillClimber implements Algorithm 
{
	private TTPInstance _instance;
	private TTPSolution _best;
	
	public GreedyHillClimber(TTPInstance instance)
	{
		_instance = instance;
	}
	
	@Override
	public TTPSolution iterate(int[] tour)
	{
		ttp.Utils.Utils.startTiming();
		
		//boolean debugPrint = !true;
		int[] packingPlan = new int[_instance.numberOfItems];
		
		int[][] newPackingPlans = null;
		int num = _instance.numberOfItems / 4000;
		if(num  > 5)
		{
			newPackingPlans = new int[5][_instance.numberOfItems];   //just for speed, 5 could be larger
		}
		else
		{
			newPackingPlans = new int[1][_instance.numberOfItems];
		}
		
		for(int j=0; j < newPackingPlans.length; j++)
		{
			newPackingPlans[j] = (int[]) DeepCopy.copy(packingPlan);
			newPackingPlans[j] = flipN(newPackingPlans[j]);  //flip one bit if it is available
		}
		
		int[] bestPlan = new int[_instance.numberOfItems];
		double bValuse = Double.NEGATIVE_INFINITY;
		for(int j = 0; j < newPackingPlans.length; j++)
		{
			TTPSolution newSolution = new TTPSolution(tour, newPackingPlans[j]);
			_instance.evaluate(newSolution);
			if(newSolution.ob > bValuse)
			{
				bValuse = newSolution.ob;
				bestPlan = (int[]) DeepCopy.copy(newPackingPlans[j]);
			}
		
		}
		
		TTPSolution newSolution = new TTPSolution(tour, bestPlan);
		_instance.evaluate(newSolution);
		if (_best == null) _best = newSolution;
		_instance.evaluate(_best);
		
		/* replacement condition:
		*   objective value has to be at least as good AND
		*   the knapsack cannot be overloaded
		*/
		
		if (newSolution.ob >= _best.ob && newSolution.wend >=0 )
		{		
			_best = newSolution;
		}

		return newSolution;
	}
	
    private static int[] flipN(int[] packingplan)
    {
        int numberofFlip = 1;
        if(packingplan.length > 20000)
        {
            numberofFlip = packingplan.length /20000;
        }
        for(int i = 0; i < numberofFlip; i++)
        {
            boolean signal = true;      //only flip if that bit is aviable
            while(signal)
            {
	            int position = (int) (Math.random() * packingplan.length);
	            
	            if (packingplan[position] == 1) 
	            {
	            	packingplan[position] = 0;
	            }
	            else if (packingplan[position] == 0) 
	            {
	                packingplan[position] = 1;
	            }
	            signal = false;
	                
            }
        }
        return packingplan;
    }

	@Override
	public TTPSolution getBest() 
	{
		return _best;
	}

}
