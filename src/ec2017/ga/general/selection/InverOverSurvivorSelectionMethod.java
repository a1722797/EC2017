package ec2017.ga.general.selection;

import java.util.ArrayList;
import java.util.List;

import ec2017.ga.general.SurvivorSelectionMethod;
import ttp.TTPSolution;

public class InverOverSurvivorSelectionMethod implements SurvivorSelectionMethod
{

	@Override
	public List<TTPSolution> select(
			List<TTPSolution> oldGeneration, List<TTPSolution> newGeneration, int size)
	{
		List<TTPSolution> output = new ArrayList<TTPSolution>();

		for (int i = 0; i < size; i++)
		{
			TTPSolution parent = oldGeneration.get(i);
			TTPSolution child = newGeneration.get(i);
			if(parent.getObjective() > child.getObjective())
			{
				output.add(parent);
			}
			else
			{
				output.add(child);
			}
		}

		return output;
	}

}
