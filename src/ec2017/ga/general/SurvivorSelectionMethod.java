package ec2017.ga.general;

import java.util.ArrayList;

public interface SurvivorSelectionMethod 
{
	public ArrayList<Individual> select(ArrayList<Individual> oldGeneration, ArrayList<Individual> newGeneration, int size);
}
