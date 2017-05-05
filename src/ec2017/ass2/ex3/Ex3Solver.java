package ec2017.ass2.ex3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import ttp.TTPInstance;
import ttp.TTPSolution;

public class Ex3Solver
{
	public static TTPSolution solve(int generations, int mode, int algor, int randomSeed, TTPInstance instance)
	{
		String tourName = instance.file.getName();
		tourName = tourName.substring(0, tourName.indexOf("_"));
		tourName += ".linkern.tour";
		int[] tour = getTour(tourName);
		
		// Set mutation rate based on mode.
		int mutationRate = mode == 1 ? 50 : mode == 2 ? 500 : generations;
		
		// Get PRNG. Since we have a fixed seed, all random choices are reproducible and consistent.
		Random rng = new Random(randomSeed);
		
		for (int i = 0; i < generations; i++)
		{
			if (i % mutationRate == 0 && i != 0)
			{
				int rand1 = rng.nextInt(tour.length - 1) + 1;
				int rand2 = rng.nextInt(tour.length - 1) + 1;
				int start = (Math.min(rand1, rand2));
				int end = (Math.max(rand1, rand2));
			
				System.out.println(Arrays.toString(tour));
				
				if (mode == 1) tour = exchange(tour, start, end);
				else if (mode == 2) tour = twoOpt(tour, start, end);
			};
		}
		
		return null;
	}
	
	public static int[] getTour(String tourName)
	{
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader("instances/" + tourName));
			String lenStr = br.readLine().split(" ")[0];
			int length = Integer.parseInt(lenStr);
			int[] tour = new int[length + 1];
			
			int i = 0;
			while(i < length && br.ready())
			{
				tour[i++] = Integer.parseInt(br.readLine().split(" ")[0]);
			}
			
			tour[length] = tour[0];
					
			br.close();

			return tour;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new RuntimeException("Something went wrong :((((((((");
	}
	
	
	public static int[] twoOpt(int[] tour, int start, int end)
	{
		while(start < end)
		{
			int temp = tour[start];
			tour[start] = tour[end];
			tour[end] = temp;
			start++;
			end--;
		}
		
		return tour;
	}
	
	public static int[] exchange(int[] tour, int start, int end)
	{
		int temp = tour[start];
		tour[start] = tour[end];
		tour[end] = temp;
		
		return tour;
	}
	
	
}
