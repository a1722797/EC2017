package ec2017.ga.general.selection;

import java.util.ArrayList;

import ec2017.ga.general.Individual;
import ec2017.ga.general.SurvivorSelectionMethod;

public class InverOverSurvivorSelectionMethod implements SurvivorSelectionMethod
{

	@Override
	public ArrayList<Individual> select(
			ArrayList<Individual> oldGeneration, ArrayList<Individual> newGeneration, int size) 
	{
		ArrayList<Individual> output = new ArrayList<Individual>();
		
		for (int i = 0; i < size; i++)
		{
			Individual parent = oldGeneration.get(i);
			Individual child = newGeneration.get(i);
			if(parent.getFitness() > child.getFitness())
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
