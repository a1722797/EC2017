package ec2017.ga.general.variation;

import java.util.Arrays;

import ec2017.ga.general.MutateOperator;
import ttp.TTPInstance;
import ttp.TTPSolution;

public class MutateRandomWithOptimisation implements MutateOperator {
	private TTPInstance _ttp;
	private PackingPlanOptimiser optimiser;

	@Override
	public TTPSolution mutate(TTPSolution genotype) {
		int[] tspTour = genotype.tspTour;
		int[] packingPlan = Arrays.copyOf(genotype.packingPlan, _ttp.numberOfItems);

		int index = (int)(Math.random() * _ttp.numberOfItems);
		packingPlan[index] ^= 1;

		TTPSolution solution = new TTPSolution(tspTour, packingPlan);
		optimiser.optimisePackingPlan(solution);

		return solution;
	}

	@Override
	public void setInstance(TTPInstance ttp) {
		_ttp = ttp;
		optimiser = new PackingPlanOptimiser(ttp);
	}

}
