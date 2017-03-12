package ec2017.ga.general.variation;

import java.util.ArrayList;
import java.util.Random;

import ec2017.ga.general.CrossOverOperator;
import ec2017.ga.general.Symbol;

public class InterOverOp implements CrossOverOperator
{
	private double _p = 0.5;
	
	@Override
	public ArrayList<Symbol> crossOver(ArrayList<Symbol> parentA, ArrayList<Symbol> parentB)
	{
		Random prng = new Random();
		int index = prng.nextInt(parentA.size());
		int index2 = index;
		//select (randomly) a city c from S'
		Symbol city = parentA.get(index);
		
		if (Math.random() <= _p)
		{
			//select the city c' from the remaining cities in S'
			// Note: the paper doesn't say how to select our second city, so we'll 
			// just grab another at random.
			while (index2 == index) index2 = prng.nextInt(parentA.size());
		}
		else
		{
			// get a random individual from the population
			// we already have parentB, which is randomly shuffled in.
			// get the city next to ours in the parent individual.
			int indexOfNext = parentB.indexOf(city) + 1;
			if (indexOfNext >= parentA.size()) indexOfNext = 0;
			Symbol nextSymbol = parentB.get(indexOfNext);
			index2 = parentA.indexOf(nextSymbol);
		}
		
		// The paper seems to assume our second city will always be after the first
		// but that's a silly assumption.
		int start = Math.min(index, index2) + 1; //start after c'
		int end = Math.max(index, index2);
				
		return invert(parentA, start, end);
	}
	
	public void setPValue(double p)
	{
		_p = p;
	}
	
	private ArrayList<Symbol> invert(ArrayList<Symbol> genotype, int start, int end)
	{
		ArrayList<Symbol> child = new ArrayList<Symbol>();
		//add symbols before start of inversion
		for(int i = 0; i < start; i++) child.add(genotype.get(i));
		
		//invert section.
		int i = end;
		while(i >= start) child.add(genotype.get(i--));
		
		//add symbols after section.
		for(i = end+1; i < genotype.size(); i++) child.add(genotype.get(i));
		
		return child;
	}
}
