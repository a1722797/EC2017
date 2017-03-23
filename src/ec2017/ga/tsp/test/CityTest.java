package ec2017.ga.tsp.test;

import static org.junit.Assert.*;

import org.junit.Test;

import ec2017.ga.general.Symbol;
import ec2017.ga.tsp.City;
import ec2017.ga.tsp.SymCity;

public class CityTest
{

	@Test
	public void testDistance() 
	{
		City cityA = new City("A", 10, 10);
		City cityB = new City("B", 13, 14);
		assertTrue(cityA.distanceTo(cityB) == 5.0);
		assertTrue(cityB.distanceTo(cityA) == 5.0);
		
		City symA = new SymCity("C", -1, -1);
		City symB = new SymCity("D", 2, 3);
		assertTrue(symA.distanceTo(symB) == 5.0);
		assertTrue(symB.distanceTo(symA) == 5.0);
		assertTrue(symA.distanceTo(symB) == 5.0);
		assertTrue(symB.distanceTo(symA) == 5.0);
	}
	
	@Test
	public void testEquals()
	{
		Symbol cityA = new City("A", 10, 11);
		Symbol cityB = new City("B", 13, 14);
		Symbol cityA2 = new City("A", 10, 11);
		
		assertFalse(cityA.equals(cityB));
		assertTrue(cityA.equals(cityA2));
	}

}
