package ec2017.ga.general.variation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import ttp.TTPInstance;
import ttp.TTPSolution;

public class PackingPlanOptimiser {
	private TTPInstance _ttp;
	private int[] currentTour;

	// List of item indices, sorted by ascending profit to weight ratio
	private List<Integer> itemsByProfitToWeightInc;
	// List of item indices, sorted by descending profit to weight ratio
	private List<Integer> itemsByProfitToWeightDec;

	public PackingPlanOptimiser(TTPInstance ttp) {
		_ttp = ttp;
		itemsByProfitToWeightInc = new ArrayList<>(_ttp.numberOfItems);
		itemsByProfitToWeightDec = new ArrayList<>(_ttp.numberOfItems);

		for (int i = 0; i < _ttp.numberOfItems; i++) {
			itemsByProfitToWeightInc.add(i);
			itemsByProfitToWeightDec.add(i);
		}
		Collections.sort(itemsByProfitToWeightInc, new Comparator<Integer> () {
			@Override
			public int compare(Integer arg0, Integer arg1) {
				Double ratio0 = new Double(_ttp.items[arg0][1]) / _ttp.items[arg0][2];
				Double ratio1 = new Double(_ttp.items[arg1][1]) / _ttp.items[arg1][2];
				return ratio0.compareTo(ratio1);
			}
		});

		Collections.copy(itemsByProfitToWeightDec, itemsByProfitToWeightInc);
		Collections.reverse(itemsByProfitToWeightDec);
	}

	private void setTour(int[] tour) {
		currentTour = tour;


	}

	public void optimisePackingPlan(TTPSolution solution) {
		if (solution.tspTour != currentTour) {
			setTour(solution.tspTour);
		}
/*
		long currentWeight = 0;

		for (int i = 0; i < _ttp.numberOfItems; i++) {
			if (solution.packingPlan[i] != 0) {
				currentWeight += _ttp.items[i][2];
			}
		}

		// Remove the worst items until we're under the limit
		Iterator<Integer> it = itemsByProfitToWeightInc.iterator();
		while (currentWeight > _ttp.capacityOfKnapsack && it.hasNext()) {
			int idx = it.next();
			if (solution.packingPlan[idx] != 0) {
				solution.packingPlan[idx] = 0;
				currentWeight -= _ttp.items[idx][2];
			}
		}


		// Add the best items until we match the limit or run out of items
		it = itemsByProfitToWeightDec.iterator();
		while (currentWeight < _ttp.capacityOfKnapsack && it.hasNext()) {
			int idx = it.next();
			if (solution.packingPlan[idx] == 0 &&
				_ttp.items[idx][2] <= (_ttp.capacityOfKnapsack - currentWeight))
			{
				solution.packingPlan[idx] = 1;
				currentWeight += _ttp.items[idx][2];
			}
		}*/
	}
}
