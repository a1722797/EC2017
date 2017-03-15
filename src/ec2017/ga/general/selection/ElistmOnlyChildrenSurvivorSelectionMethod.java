package ec2017.ga.general.selection;

import ec2017.ga.general.Individual;
import ec2017.ga.general.SurvivorSelectionMethod;

import java.util.ArrayList;

/**
 * Created by yanshuo on 17/3/15.
 */
public class ElistmOnlyChildrenSurvivorSelectionMethod implements SurvivorSelectionMethod {
    @Override
    public ArrayList<Individual> select(ArrayList<Individual> oldGeneration, ArrayList<Individual> newGeneration, int size) {

        ArrayList<Individual> candidates = newGeneration;
        ArrayList<Individual> survivors = getBestNIndividual(candidates, size);
        return survivors;
    }


    public ArrayList<Individual> getBestNIndividual(ArrayList<Individual> candidates, int size){
        ArrayList<Individual> result = new ArrayList<Individual>();
        int[] indexSet = new int[candidates.size()];
        double[] fitness = new double[candidates.size()];
        for(int i = 0; i < candidates.size(); i++){
            indexSet[i] = i;
            fitness[i] = candidates.get(i).getFitness();
        }

        for(int i = 0; i < candidates.size(); i++){
            for(int j = 0; j < candidates.size(); j++){
                if(fitness[i] < fitness[j]){
                    double tmp = 0;
                    int tmpIndex = -1;

                    tmp = fitness[j];
                    fitness[j] = fitness[i];
                    fitness[i] = tmp;

                    tmpIndex = indexSet[j];
                    indexSet[j] = indexSet[i];
                    indexSet[i] = tmpIndex;

                }
            }
        }

        for(int i = 0; i < size; i++){
            result.add(candidates.get(indexSet[i]));
        }
        return result;
    }
}
