package ec2017.ga.general.selection;

import ec2017.ga.general.Individual;
import ec2017.ga.general.ParentSelectionMethod;

import java.util.ArrayList;

/**
 * Created by yanshuo on 17/3/14.
 */
public class WindowingFPSParentSelectionMethod implements ParentSelectionMethod {
    @Override
    public ArrayList<Individual> select(ArrayList<Individual> population) {
        ArrayList<Individual> selectedParents = new ArrayList<Individual>(population.size());
        double sumOfFitness = 0;
        double minFitness = Integer.MAX_VALUE;
        double [] cumulativeProbs = new double[population.size()];

        // Find the minimum fitness
        for(int i = 0; i < population.size(); i++){
            if(population.get(i).getFitness() < minFitness){
                minFitness = population.get(i).getFitness();
            }
        }

        // Calculate the total fitness above the minimum
        for (int i = 0; i < population.size(); i++) {
            sumOfFitness += population.get(i).getFitness() - minFitness;
        }

        // Calculate cumulative probability distribution
        double cumulativeProb = 0;
        for(int i = 0; i < population.size(); i++){
            cumulativeProbs[i] = cumulativeProb + (population.get(i).getFitness() - minFitness) / sumOfFitness;
            cumulativeProb = cumulativeProbs[i];
        }

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
