package ec2017.ga.general.selection;

import java.util.ArrayList;
import java.util.Collections;

import ec2017.ga.general.Individual;
import ec2017.ga.general.SurvivorSelectionMethod;

public class E1337ismSurvivorSelect implements SurvivorSelectionMethod
{

	@Override
	public ArrayList<Individual> select(
			ArrayList<Individual> oldGeneration,
			ArrayList<Individual> newGeneration,
			int size) 
	{
		ArrayList<Individual> generations = new ArrayList<>();
		generations.addAll(oldGeneration);
		generations.addAll(newGeneration);
		
		Collections.sort(generations);
		
		return new ArrayList<>(generations.subList(generations.size()-size-1, generations.size()-1));
	}

}
