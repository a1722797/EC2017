package ec2017.ga.general.selection;

import ec2017.ga.general.ParentSelectionMethod;
import ttp.TTPSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of n-armed roulette wheel fitness proportinal selection
 *
 * Created by yanshuo on 17/3/13.
 */
public class SUSParentSelectionMethod implements ParentSelectionMethod {
    @Override
    public List<TTPSolution> select(List<TTPSolution> population) {
    	List<TTPSolution> selectedParents = new ArrayList<TTPSolution>();
        double sumOfFitness = 0;

        for(int i = 0; i < population.size(); i++){
            sumOfFitness += population.get(i).getObjective();
        }

        double space = sumOfFitness / population.size();
        double starter = Math.random() * space;

        for(int i = 0; i < population.size(); i++){
            int index = 0;
            double curSumOfFitness = population.get(0).getObjective();
            while(curSumOfFitness < (starter + i * space)){
                index++;
                curSumOfFitness += population.get(index).getObjective();
            }
            selectedParents.add(population.get(index));
        }


        return selectedParents;
    }
}
