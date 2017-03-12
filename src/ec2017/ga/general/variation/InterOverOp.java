package ec2017.ga.general.variation;

import java.util.ArrayList;
import java.util.Random;

import ea2017.ga.tsp.City;
import ec2017.ga.general.CrossOverOperator;
import ec2017.ga.general.Individual;
import ec2017.ga.general.MutateOperator;
import ec2017.ga.general.Population;
import ec2017.ga.general.Symbol;

public class InterOverOp implements MutateOperator
{
	private double _p = 0.02;
	private Population _population;
	
	@Override
	public ArrayList<Symbol> mutate(ArrayList<Symbol> genotype)
	{
		ArrayList<Symbol> s = new ArrayList<Symbol>(genotype);
		
		Random rand = new Random();
		int index = rand.nextInt(s.size());
		int indexPrime = index;
		//select (randomly) a city c from S'
		Symbol c = s.get(index);
		
		boolean terminate = false;
		int repeats = 0;
		//repeat
		while(!terminate)
		{
			repeats++;
			if (Math.random() <= _p)
			{
				//select the city c' from the remaining cities in S'
				// Note: the paper doesn't say how to select our second city, so we'll 
				// just grab another at random.
				indexPrime = rand.nextInt(s.size());
				
			}
			else
			{
				// get a random individual from the population
				// we already have parentB, which is randomly shuffled in.
				// get the city next to ours in the parent individual.
				ArrayList<Symbol> sPrime = _population.getRandom().getGenotype();
				// assign to c' the 'next' city to the city c in selected individual
				int indexOnSPrime = sPrime.indexOf(c) + 1;
				if (indexOnSPrime >= s.size()) indexOnSPrime = 0;
				// We're getting the index
				Symbol cPrime = sPrime.get(indexOnSPrime);
				indexPrime = s.indexOf(cPrime);
			}
			
			int betweenTwoCities = Math.abs(indexPrime - index);
			terminate = betweenTwoCities <= 1 || betweenTwoCities == s.size() - 1;
			
			// We compare fitness later.
			if (terminate) return s;
			// The paper seems to assume our second city will always be after the first
			// but that's a silly assumption.
			int start = Math.min(index, indexPrime) + 1; //start after c'
			int end = Math.max(index, indexPrime);
					
			invert(s, start, end);
			
			index = indexPrime;
		}
		return s;
	}
	
	public void setPValue(double p)
	{
		_p = p;
	}
	
	private ArrayList<Symbol> invert(ArrayList<Symbol> genotype, int start, int end)
	{
		while(start < end)
		{
			Symbol temp = genotype.get(start);
			genotype.set(start, genotype.get(end));
			genotype.set(end, temp);
			start++;
			end--;
		}
		
		return genotype;
		
//		ArrayList<Symbol> child = new ArrayList<Symbol>();
//		//add symbols before start of inversion
//		for(int i = 0; i < start; i++) child.add(genotype.get(i));
//		
//		//invert section.
//		int i = end;
//		while(i >= start) child.add(genotype.get(i--));
//		
//		//add symbols after section.
//		for(i = end+1; i < genotype.size(); i++) child.add(genotype.get(i));
//		
//		System.out.println("\n");
//		for(Symbol sym : genotype)
//		{
//			City city = (City)sym;
//			System.out.print(city.getId() + ", ");
//		}
//		
//		return child;
	}

	public void setPopulation(Population population)
	{
		_population = population;
	}
}
