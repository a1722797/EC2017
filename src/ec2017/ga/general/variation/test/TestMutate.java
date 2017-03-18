package ec2017.ga.general.variation.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import ec2017.ga.general.MutateOperator;
import ec2017.ga.general.Symbol;
import ec2017.ga.general.variation.MutateInsert;
import ec2017.ga.general.variation.MutateInversion;
import ec2017.ga.general.variation.MutateNop;
import ec2017.ga.general.variation.MutateSwap;
import ec2017.ga.tsp.City;

public class TestMutate {

	@Test
	public void testMutateInsert() 
	{
		checkForInfiniteLoop(new MutateInsert());
	}
	
	@Test
	public void testMutateInversion() 
	{
		checkForInfiniteLoop(new MutateInversion());
	}
	
	@Test
	public void testMutateNop() 
	{
		checkForInfiniteLoop(new MutateNop());
	}
	
	@Test
	public void testMutateSwap() 
	{
		checkForInfiniteLoop(new MutateSwap());
	}
	
	

	private void checkForInfiniteLoop(MutateOperator op)
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
		
		Thread t = new Thread(new Runnable() 
		{
			public void run() 
			{
				op.mutate(pathA);
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
