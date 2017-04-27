package ec2017.ga.general.selection;

import ec2017.ga.general.ParentSelectionMethod;
import ttp.TTPSolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of rank-based parent selection
 *
 * Created by yanshuo on 17/3/14.
 */
public class RankBasedParentSelectionMethod implements ParentSelectionMethod {
    @Override
    public List<TTPSolution> select(List<TTPSolution> population) {
    	// Sort the population by increasing fitness
    	Collections.sort(population);
    	Collections.reverse(population);

    	// Calculate weights
        int size = population.size();
        double[] weight = new double[size];
        double totalWeight = 0;
        double cumulativeWeight = 0;

        for(int i = 0; i < size; i++){
        	//Linear Ranking    (at least 1/ (size*(size - 1) )
            weight[i] = (2*i + size - 1) / size * (size - 1)* (size - 1);
            totalWeight += weight[i];
        }

        // Calculate cumulative probabilities
        for(int i = 0; i < size; i++) {
        	weight[i] = cumulativeWeight + (weight[i]/totalWeight);
        	cumulativeWeight = weight[i];
        }
        weight[size-1] = 1.0; // Avoid cumulative errors

        // Spin the roulette wheel n times
        return getParentByPro(weight, population);
    }
    private List<TTPSolution> getParentByPro(double[] cumulativeProbs, List<TTPSolution> population){
    	List<TTPSolution> selectedParents = new ArrayList<TTPSolution>(population.size());
        for(int i = 0; i < population.size(); i++){
        	double r = Math.random();
        	int index = 0;
        	for (; cumulativeProbs[index] < r; index++) {
        	}
        	selectedParents.add(population.get(index));
        }
        return selectedParents;
    }
}
