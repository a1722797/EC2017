package ec2017.ga.general.variation;

import ec2017.ga.general.MutateOperator;
import ttp.TTPInstance;
import ttp.TTPSolution;

/**
 * This is a stub implementation for testing. Returns a shallow copy of the given genotype.
 * @author pat
 *
 */
public class MutateNop implements MutateOperator
{

	@Override
	public TTPSolution mutate(TTPSolution genotype) {
		return new TTPSolution(genotype.tspTour, genotype.packingPlan);
	}

	@Override
	public void setInstance(TTPInstance ttp) {
	}

}
