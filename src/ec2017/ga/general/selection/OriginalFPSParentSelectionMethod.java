package ec2017.ga.general.selection;

import ec2017.ga.general.Individual;
import ec2017.ga.general.ParentSelectionMethod;

import java.util.ArrayList;

/**
 * Implementation of one-armed roulette fitness proportional selection
 *
 * Created by yanshuo on 17/3/13.
 */
public class OriginalFPSParentSelectionMethod implements ParentSelectionMethod {
    @Override
    public ArrayList<Individual> select(ArrayList<Individual> population) {
        ArrayList<Individual> selectedParents = new ArrayList<Individual>(population.size());
        double sumOfFitness = 0;
        double [] cumulativeProbs = new double[population.size()];

        // Calculate the total fitness to provide a scaling factor
        for(int i = 0; i < population.size(); i++){
            sumOfFitness += population.get(i).getFitness();
        }

        // Calculate a cumulative probability distribution
        double cumulativeProb = 0;
        for(int i = 0; i < population.size(); i++){
        	cumulativeProbs[i] = cumulativeProb + (population.get(i).getFitness() / sumOfFitness);
            cumulativeProb = cumulativeProbs[i];
        }
        // Make sure we don't get any weird problems from cumulative floating point error
        cumulativeProbs[population.size()-1] = 1.0;

        // Spin the roulette wheel n times
        for(int i = 0; i < population.size(); i++){
        	double r = Math.random();
        	int index = 0;
        	for (; cumulativeProbs[index] >= r; index++) {
        	}
        	selectedParents.add(population.get(index));
        }

        return selectedParents;
    }
}
