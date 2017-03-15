package ec2017.ga.general.selection;

import ec2017.ga.general.Individual;
import ec2017.ga.general.ParentSelectionMethod;

import java.util.ArrayList;

/**
 * Created by yanshuo on 17/3/13.
 */
public class SUSParentSelectionMethod implements ParentSelectionMethod {
    @Override
    public ArrayList<Individual> select(ArrayList<Individual> population) {
        ArrayList<Individual> selectedParents = new ArrayList<Individual>();
        double sumOfFitness = 0;
        double space = sumOfFitness / population.size();
        double starter = Math.random() * space;

        for(int i = 0; i < population.size(); i++){
            sumOfFitness += population.get(i).getFitness();
        }
        for(int i = 0; i < population.size(); i++){
            int index = 0;
            double curSumOfFitness = population.get(0).getFitness();
            while(curSumOfFitness < (starter + i * space)){
                index++;
                curSumOfFitness += population.get(index).getFitness();
            }
            selectedParents.add(population.get(index));
        }


        return selectedParents;
    }
}
