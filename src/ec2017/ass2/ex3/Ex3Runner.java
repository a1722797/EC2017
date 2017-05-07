package ec2017.ass2.ex3;

import java.io.File;
import java.io.FilenameFilter;

import ttp.TTPInstance;
import ttp.TTPSolution;

public class Ex3Runner 
{
	public static void main(String[] args)
	{
		int generations = args.length > 0 ? Integer.parseInt(args[0]) : 100000;
		int mode = args.length > 1 ? Integer.parseInt(args[1]) : 2;
		int algor = args.length > 2 ? Integer.parseInt(args[2]) : 3;
		int randomSeed = args.length > 3 ? Integer.parseInt(args[3]) : 69;
		String folder = args.length > 4 ? args[4] : "instances";
		
		runBenchmarks(generations, mode, algor, randomSeed, folder);
		
	}
	
	public static void runBenchmarks(int generations, int mode, int algor, int randomSeed, String folder)
	{
		Results results = new Results("ex3");
		
		for (File file : getFiles(folder, null))
		{
			TTPInstance instance = new TTPInstance(file);
			TTPSolution solution = Ex3Solver.solve(generations, mode, algor, randomSeed, instance, results);
			
			instance.evaluate(solution);
			System.out.println(file.getName() + " : " + solution.ob);
			results.writeToCSV();
			results.writeToTable();
		}
		
	}
	
	public static File[] getFiles(String folder, String match)
	{
		File fileDir = new File(folder);
		return fileDir.listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return name.contains(".ttp") && (match == null || name.contains(match));
			}	
		});
	}
}
