package ec2017.ga.general.selection;

import ea2017.ga.tsp.Path;
import ec2017.ga.general.Individual;
import ec2017.ga.general.ParentSelectionMethod;

import java.util.ArrayList;

/**
 * Created by yanshuo on 17/3/14.
 */
public class TournamentParentSelectionMethod implements ParentSelectionMethod {
    public int _k = 5;     //choose k individuals randomly
    public double _pro = 1.0;   //the probablity of fittest individual choosen

    public TournamentParentSelectionMethod(int k){
        _k = k;
    }
    public TournamentParentSelectionMethod(int k, double pro){
        _k = k;
        _pro = pro;
    }

    @Override
    public ArrayList<Individual> select(ArrayList<Individual> population) {
        ArrayList<Individual> selectedParents = new ArrayList<Individual>();
        for(int i = 0; i < population.size(); i++) {
            int Index = getBestKIndividuals(population);
            selectedParents.add(population.get(Index));
        }
        return selectedParents;
    }

    private int getBestKIndividuals(ArrayList<Individual> population){
        //ArrayList<Individual> outcome = new ArrayList<Individual>();
        int[] indexSet = new int[_k];
        double[] fitness = new double[_k];
        for(int i = 0; i < _k; i++){
            int randomNum = (int)( Math.random()*population.size() );
            indexSet[i] = randomNum;
            fitness[i] = population.get(randomNum).getFitness();
        }


        for(int i = 0; i < _k; i++){
            for(int j = 0; j <_k; j++){
                if(fitness[i] < fitness[j]){
                    double tmp = 0;
                    tmp = fitness[j];
                    fitness[j] = fitness[i];
                    fitness[i] = tmp;

                    int tmpIndex = -1;
                    tmpIndex = indexSet[j];
                    indexSet[j] = indexSet[i];
                    indexSet[i] = tmpIndex;
                }
            }
        }

        double proNum = _pro;
        for(int i = 0; i < _k; i++){
            if(Math.random() < proNum){
                return indexSet[i];
            }
            proNum *= _pro;
        }
        return indexSet[0];
    }


}
