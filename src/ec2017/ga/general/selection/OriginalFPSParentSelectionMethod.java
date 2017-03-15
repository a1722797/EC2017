package ec2017.ga.general.selection;

import ec2017.ga.general.Individual;
import ec2017.ga.general.ParentSelectionMethod;

import java.util.ArrayList;

/**
 * Created by yanshuo on 17/3/13.
 */
public class OriginalFPSParentSelectionMethod implements ParentSelectionMethod {
    @Override
    public ArrayList<Individual> select(ArrayList<Individual> population) {
        ArrayList<Individual> selectedParents = new ArrayList<Individual>();
        double sumOfFitness = 0;
        double [] weight = new double[population.size()];
        boolean notaccepted;
        int index = 0;

        for(int i = 0; i < population.size(); i++){
            sumOfFitness += population.get(i).getFitness();
        }
        for(int i = 0; i < population.size(); i++){
            weight[i] = population.get(i).getFitness() / sumOfFitness;
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
}
