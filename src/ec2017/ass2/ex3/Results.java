package ec2017.ass2.ex3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Results
{
	private HashMap<String, ArrayList<Long>> _results = new HashMap<>();
	private String _filename;
	private long _time = System.currentTimeMillis();
	
	public Results(String filename)
	{
		_filename = _time + "-" + filename;
	}
	
	public void addData(String row, long value)
	{
		if (!_results.containsKey(row))
		{
			_results.put(row, new ArrayList<>(10000));
		}
		
		_results.get(row).add(value);
	}
	
	public void writeToCSV()
	{
		StringBuilder sb = new StringBuilder();
		
		ArrayList<String> keys = new ArrayList<String>(_results.keySet());
		Collections.sort(keys);
		
		for(String key : keys)
		{
			sb.append(key);
			sb.append(',');
			
			for(long val : _results.get(key))
			{
				sb.append(val);
				sb.append(',');
			}
			
			sb.append(System.lineSeparator());
		}
		
		writeFile(_filename + "-charts.csv", sb.toString());

	}
	
	public void writeToTable()
	{
		StringBuilder sb = new StringBuilder();
		HashMap<String, ArrayList<Long>> benchmarks = new HashMap<>();
		for(String key : _results.keySet())
		{
			String benchkey = key.substring(0, key.length()-6);
			if(!benchmarks.containsKey(benchkey)) benchmarks.put(benchkey, new ArrayList<>());
			
			ArrayList<Long> val = _results.get(key);
			benchmarks.get(benchkey).add(val.get(val.size()-1));
		}
		
		sb.append("Benchmark,Best,Average,StdDev");
		sb.append(System.lineSeparator());
		
		ArrayList<String> keys = new ArrayList<>(benchmarks.keySet());
		Collections.sort(keys);
		for(String key : keys)
		{
			ArrayList<Long> results = benchmarks.get(key);
			long best = Integer.MIN_VALUE;
			long avg = 0;
			long stdDev = 0;
			
			for(long res : results)
			{
				avg += res;
				best = best < res ? res : best;
			}
			
			avg /= results.size();
			
			for(long res : results)
			{
				long s = res - avg;
				s = s*s;
				stdDev += s;
			}
			
			stdDev /= results.size();
			stdDev = (int) Math.sqrt(stdDev);
			
			sb.append(key);
			sb.append(",");
			sb.append(best);
			sb.append(",");
			sb.append(avg);
			sb.append(",");
			sb.append(stdDev);
			sb.append(System.lineSeparator());
		}
		
		writeFile(_filename + "-results.csv", sb.toString());
	}
	
	private void writeFile(String filename, String contents)
	{
		try 
		{
			File file = new File(filename);
			file.createNewFile();
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			bw.write(contents);
			
			bw.flush();
			bw.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
