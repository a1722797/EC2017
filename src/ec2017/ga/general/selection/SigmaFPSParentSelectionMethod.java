package ec2017.ga.general.selection;

import ec2017.ga.general.Individual;
import ec2017.ga.general.ParentSelectionMethod;

import java.util.ArrayList;


/**
 * Created by yanshuo on 17/3/14.
 */
public class SigmaFPSParentSelectionMethod implements ParentSelectionMethod {
    @Override
    public ArrayList<Individual> select(ArrayList<Individual> population) {
        ArrayList<Individual> selectedParents = new ArrayList<Individual>();
        double sumOfFitness = 0;
        double[] fitness = new double[population.size()];
        double [] weight = new double[population.size()];
        boolean notaccepted;
        int index = 0;
        double aveFitness = 0;
        double SDFitness = 0;   //sigma(standard devition)

        for(int i = 0; i < population.size(); i++){
            fitness[i] = population.get(i).getFitness();
            sumOfFitness += fitness[i];
        }
        aveFitness = getAve(fitness);
        SDFitness = getSD(fitness);

        for(int i = 0; i < population.size(); i++){
            weight[i] = Math.max(fitness[i] - (aveFitness - 2* SDFitness),0) / sumOfFitness;

        }

        for(int i = 0; i < population.size(); i++){
            notaccepted = true;
            while(notaccepted){     //stochastic acceptance
                index = (int) (population.size() * Math.random());
                if(Math.random() < weight[index] ){
                    notaccepted = false;    //the index of population is choosen
                }
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
