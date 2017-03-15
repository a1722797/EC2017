package ec2017.ga.general.selection;

import ec2017.ga.general.Individual;
import ec2017.ga.general.ParentSelectionMethod;

import java.util.ArrayList;

/**
 * Created by yanshuo on 17/3/14.
 */
public class RankBasdedParentSelectionMethod implements ParentSelectionMethod {
    @Override
    public ArrayList<Individual> select(ArrayList<Individual> population) {
        int size = population.size();
        double[] fitness = new double[size];
        int[] rankingIndex = new int[size];
        double[] weight = new double[size];

        for(int i = 0; i < size; i++){
            fitness[i] = population.get(i).getFitness();
        }
        rankingIndex = getRankingIndex(fitness);
        for(int i = 0; i < size; i++){
            weight[rankingIndex[i]] = (2*i + size - 1) / size * (size - 1)* (size - 1);     //Linear Ranking    (at least 1/ (size*(size - 1) )
        }

        return getParentByPro(weight, population);
    }
    private ArrayList<Individual> getParentByPro(double[] weight, ArrayList<Individual> population){
        ArrayList<Individual> selectedParents = new ArrayList<Individual>();
        boolean notaccepted;
        int index = 0;
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


    private int[] getRankingIndex(double[] numSet){
        int[] outcome = new int[numSet.length];
        for(int i = 0; i < numSet.length; i++){
            outcome[i] = i;
        }


        for(int i = 0; i < numSet.length; i++){
            for(int j = 0; j < numSet.length; j++){
                if(numSet[i] < numSet[j]){
                    double tmp = 0;
                    tmp = numSet[j];
                    numSet[j] = numSet[i];
                    numSet[i] = tmp;

                    int tmpIndex = -1;
                    tmpIndex = outcome[j];
                    outcome[j] = outcome[i];
                    outcome[i] = tmpIndex;
                }
            }
        }
        return outcome;
    }
}
