package ec2017.ga.general.variation.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import ec2017.ga.general.CrossOverOperator;
import ec2017.ga.general.Symbol;
import ec2017.ga.general.variation.CrossOverCycle;
import ec2017.ga.general.variation.CrossOverEdgeRecomb;
import ec2017.ga.general.variation.CrossOverOrder;
import ec2017.ga.general.variation.CrossOverPMX;
import ec2017.ga.tsp.City;

public class TestCrossOver {

	@Test
	public void testCrossOverCycle() 
	{
		checkForInfiniteLoop(new CrossOverCycle());
	}
	
	@Test
	public void testCrossOverEdgeRecomb() 
	{
		checkForInfiniteLoop(new CrossOverEdgeRecomb());
	}
	
	@Test
	public void testCrossOverOrder() 
	{
		checkForInfiniteLoop(new CrossOverOrder());
	}
	
	@Test
	public void testCrossOverPMX() 
	{
		checkForInfiniteLoop(new CrossOverPMX());
	}

	private void checkForInfiniteLoop(CrossOverOperator op)
	{
		ArrayList<Symbol> cities = new ArrayList<Symbol>();
		City cityA = new City("a", 10, 10);
		City cityB = new City("b", 11, 10);
		City cityC = new City("c", 12, 10);
		cities.add(cityA);
		cities.add(cityB);
		cities.add(cityC);
		
		ArrayList<Symbol> pathA = new ArrayList<Symbol>();
		pathA.add(cityA);
		pathA.add(cityB);
		pathA.add(cityC);
		ArrayList<Symbol> pathB = new ArrayList<Symbol>();
		pathB.add(cityA);
		pathB.add(cityC);
		pathB.add(cityB);
		
		
		Thread t = new Thread(new Runnable() 
		{
			public void run() 
			{
				op.crossOver(pathA, pathB);
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
}
