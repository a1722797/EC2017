package ec2017.ga.general.selection;

import ec2017.ga.general.ParentSelectionMethod;
import ttp.TTPSolution;

import java.util.ArrayList;
import java.util.List;


/**
 * As FPS, but scaled according to the standard deviation of the fitness distribution
 *
 * Created by yanshuo on 17/3/14.
 */
public class SigmaFPSParentSelectionMethod implements ParentSelectionMethod {
    @Override
    public List<TTPSolution> select(List<TTPSolution> population) {
    	List<TTPSolution> selectedParents = new ArrayList<TTPSolution>(population.size());
        double[] fitness = new double[population.size()];
        double [] weight = new double[population.size()];
        double aveFitness = 0;
        double SDFitness = 0;   //sigma(standard devition)

        // Calculate the average and SD of the fitness distribution
        for(int i = 0; i < population.size(); i++){
            fitness[i] = population.get(i).getObjective();
        }
        aveFitness = getAve(fitness);
        SDFitness = getSD(fitness);

        double sumOfWeight = 0;
        // Calculate the scaled weights
        for(int i = 0; i < population.size(); i++){
            weight[i] = Math.max(fitness[i] - (aveFitness - 2* SDFitness),0);
            sumOfWeight += weight[i];
        }

        // Calculate the cumulative probability distribution
        double cumulativeProb = 0;
        for (int i = 0; i < population.size(); i++) {
        	weight[i] = cumulativeProb + (weight[i]/sumOfWeight);
        	cumulativeProb = weight[i];
        }
        weight[population.size()-1] = 1.0;

        // Spin the roulette wheel n times
        for(int i = 0; i < population.size(); i++){
        	double r = Math.random();
        	int index = 0;
        	for (; weight[index] < r; index++) {
        	}
        	selectedParents.add(population.get(index));
        }

        return selectedParents;
    }

    private double getAve(double[] set){
        double sum = 0;
        for (int i = 0; i < set.length; i++){
            sum += set[i];
        }
        return (sum / set.length);
    }

    private double getSD(double[] set){
        double ave = getAve(set);
        double tmpSum = 0;
        for (int i = 0; i < set.length; i++){
            tmpSum += (set[i] - ave) * (set[i] - ave);
        }
        return Math.sqrt(tmpSum / set.length);
    }
}
