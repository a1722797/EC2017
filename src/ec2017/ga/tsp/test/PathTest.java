package ec2017.ga.tsp.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import ec2017.ga.tsp.City;
import ec2017.ga.tsp.Path;
import ec2017.ga.tsp.SymCity;

public class PathTest {

	@Test
	public void test() 
	{
		ArrayList<City> geno = new ArrayList<City>();
		geno.add(new SymCity("a", 0, 0));
		geno.add(new SymCity("b", 3, 4));
		geno.add(new SymCity("c", 6, 8));
		geno.add(new SymCity("d", 9, 12));
		geno.add(new SymCity("e", 12, 16));
		
		Path path = new Path(geno);
		
		assertTrue(path.getDistance() == 40.0);
	}

}
