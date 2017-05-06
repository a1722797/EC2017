package ec2017.ga.general.variation;

import java.util.ArrayList;
import java.util.List;

import ec2017.ga.general.CrossOverOperator;
import ttp.TTPInstance;
import ttp.TTPSolution;

public class CrossOverRandom implements CrossOverOperator {
	private TTPInstance _ttp;

	@Override
	public List<TTPSolution> crossOver(TTPSolution parentA, TTPSolution parentB) {
		List<TTPSolution> result = new ArrayList<>(1);

		int[] tspTour = parentA.tspTour;
		int[] packingPlan = new int[_ttp.numberOfItems];
		for (int i = 0; i < _ttp.numberOfItems; i++) {
			if (Math.random() < 0.5) {
				packingPlan[i] = parentA.packingPlan[i];
			} else {
				packingPlan[i] = parentB.packingPlan[i];
			}
		}

		TTPSolution solution = new TTPSolution(tspTour, packingPlan);
		result.add(solution);
		return result;
	}

	@Override
	public void setInstance(TTPInstance ttp) {
		_ttp = ttp;
	}

}
