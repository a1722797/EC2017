package ec2017.ga.general.selection.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import ec2017.ga.general.Individual;
import ec2017.ga.general.ParentSelectionMethod;
import ec2017.ga.general.Symbol;
import ec2017.ga.general.selection.InverOverParentSelectionMethod;
import ec2017.ga.general.selection.OriginalFPSParentSelectionMethod;
import ec2017.ga.general.selection.RankBasdedParentSelectionMethod;
import ec2017.ga.general.selection.SUSParentSelectionMethod;
import ec2017.ga.general.selection.SigmaFPSParentSelectionMethod;
import ec2017.ga.general.selection.TournamentParentSelectionMethod;
import ec2017.ga.general.selection.WindowingFPSParentSelectionMethod;
import ec2017.ga.tsp.City;
import ec2017.ga.tsp.Path;

public class TestParentSelection {

	@Test
	public void testOriginalFPSParentSelectionMethod() 
	{
		checkForInfiniteLoop(new OriginalFPSParentSelectionMethod());
	}
	
	@Test
	public void testRankBasdedParentSelectionMethod() 
	{
		checkForInfiniteLoop(new RankBasdedParentSelectionMethod());
	}
	
	@Test
	public void testSigmaFPSParentSelectionMethod() 
	{
		checkForInfiniteLoop(new SigmaFPSParentSelectionMethod());
	}
	
	@Test
	public void testSUSParentSelectionMethod() 
	{
		checkForInfiniteLoop(new SUSParentSelectionMethod());
	}
	
	@Test
	public void testTournamentParentSelectionMethod() 
	{
		checkForInfiniteLoop(new TournamentParentSelectionMethod(10));
	}
	
	@Test
	public void testWindowingFPSParentSelectionMethod() 
	{
		checkForInfiniteLoop(new WindowingFPSParentSelectionMethod());
	}
	
	@Test
	public void testInterOverParentSelectionMethod() 
	{
		checkForInfiniteLoop(new InverOverParentSelectionMethod());
	}
	
	private void checkForInfiniteLoop(ParentSelectionMethod method)
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
				method.select(population);
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
