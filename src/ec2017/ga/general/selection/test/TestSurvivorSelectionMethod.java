package ec2017.ga.general.selection.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import ec2017.ga.general.Individual;
import ec2017.ga.general.SurvivorSelectionMethod;
import ec2017.ga.general.Symbol;
import ec2017.ga.general.selection.E1337ismSurvivorSelect;
import ec2017.ga.general.selection.ElistmBothSurvivorSelectionMethod;
import ec2017.ga.general.selection.ElistmOnlyChildrenSurvivorSelectionMethod;
import ec2017.ga.general.selection.InverOverSurvivorSelectionMethod;
import ec2017.ga.general.selection.RoundRobinSurvivorSelectionMethod;
import ec2017.ga.tsp.City;
import ec2017.ga.tsp.Path;

public class TestSurvivorSelectionMethod {

	@Test
	public void testElistmBothSurvivorSelectionMethod() 
	{
		SurvivorSelectionMethod method = new ElistmBothSurvivorSelectionMethod();
		checkForInfiniteLoop(method);
		checkForWorstFit(method);
	}
	
	@Test
	public void testElistmOnlyChildrenSurvivorSelectionMethod() 
	{
		SurvivorSelectionMethod method = new ElistmOnlyChildrenSurvivorSelectionMethod();
		checkForInfiniteLoop(method);
		checkForWorstFit(method);
	}

	@Test
	public void testInterOverSurvivorSelectionMethod() 
	{
		SurvivorSelectionMethod method = new InverOverSurvivorSelectionMethod();
		checkForInfiniteLoop(method);
		checkForWorstFit(method);
	}

	@Test
	public void testRoundRobinSurvivorSelectionMethod() 
	{
		SurvivorSelectionMethod method = new RoundRobinSurvivorSelectionMethod(10);
		checkForInfiniteLoop(method);
		checkForWorstFit(method);
	}
	
	@Test
	public void testE1337ismSurvivorSelectionMethod() 
	{
		SurvivorSelectionMethod method = new E1337ismSurvivorSelect();
		checkForInfiniteLoop(method);
		checkForWorstFit(method);
	}
	
	private void checkForInfiniteLoop(SurvivorSelectionMethod method)
	{
		ArrayList<Symbol> cities = new ArrayList<Symbol>();
		City cityA = new City("a", 10, 10);
		City cityB = new City("b", 11, 10);
		City cityC = new City("c", 12, 10);
		cities.add(cityA);
		cities.add(cityB);
		cities.add(cityC);
		
		ArrayList<City> pathA = new ArrayList<City>();
		pathA.add(cityA);
		pathA.add(cityB);
		pathA.add(cityC);
		ArrayList<City> pathB = new ArrayList<City>();
		pathB.add(cityA);
		pathB.add(cityC);
		pathB.add(cityB);
		ArrayList<City> pathC = new ArrayList<City>();
		pathC.add(cityC);
		pathC.add(cityA);
		pathC.add(cityB);
		
		ArrayList<Individual> population = new ArrayList<Individual>();
		population.add(new Path(pathA));
		population.add(new Path(pathB));
		population.add(new Path(pathC));
		
		Thread t = new Thread(new Runnable() 
		{
			public void run() 
			{
				method.select(population, population, population.size());
			}
		});
		
		t.start();
		try
		{
			t.join(5000);
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertFalse(t.isAlive());
	}
	
	private void checkForWorstFit(SurvivorSelectionMethod method)
	{
		Individual best = new Path()
		{
			@Override
			public double getFitness()
			{
				return 200.0;
			}
		};
		
		Individual okay = new Path()
		{
			@Override
			public double getFitness()
			{
				return 100.0;
			}
		};
		
		Individual worst = new Path()
		{
			@Override
			public double getFitness()
			{
				return 50.0;
			}
		};
		
		ArrayList<Individual> oldGeneration = new ArrayList<Individual>();
		oldGeneration.add(okay);
		
		ArrayList<Individual> newGeneration = new ArrayList<Individual>();
		newGeneration.add(best);
		newGeneration.add(worst);
		
		ArrayList<Individual> result = method.select(oldGeneration, newGeneration, 1);
		double fitness = result.get(0).getFitness();
		
		assertTrue(fitness == best.getFitness());
	}
}
