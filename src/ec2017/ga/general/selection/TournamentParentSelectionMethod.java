package ec2017.ga.general.selection;

import ec2017.ga.general.Individual;
import ec2017.ga.general.ParentSelectionMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yanshuo on 17/3/14.
 */
public class TournamentParentSelectionMethod implements ParentSelectionMethod {
    public int _k = 5;     //choose k individuals randomly
    public double _pro = 1.0;   //the probability of fittest individual chosen

    public TournamentParentSelectionMethod(int k){
        _k = k;
    }
    public TournamentParentSelectionMethod(int k, double pro){
        _k = k;
        _pro = pro;
    }

    @Override
    public ArrayList<Individual> select(ArrayList<Individual> population) {
        ArrayList<Individual> selectedParents = new ArrayList<Individual>(population.size());
        for(int i = 0; i < population.size(); i++) {
            selectedParents.add(getBestKIndividuals(population));
        }
        return selectedParents;
    }

    private Individual getBestKIndividuals(ArrayList<Individual> population){
    	// Pick _k individuals at random
    	Collections.shuffle(population);
    	List<Individual> kSubList = population.subList(0, _k);

    	// Sort by fitness
    	Collections.sort(kSubList);
    	Collections.reverse(kSubList);

    	// Pick best individual
    	return kSubList.get(0);
    }


}
