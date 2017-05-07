package ec2017.ass2.ex3;

import ttp.TTPSolution;

public interface Algorithm 
{
	public TTPSolution iterate(int[] tour);
	public TTPSolution getBest();
}
