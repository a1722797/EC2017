package ec2017.ga.general.selection;

import ec2017.ga.general.ParentSelectionMethod;
import ttp.TTPSolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yanshuo on 17/3/14.
 */
public class TournamentParentSelectionMethod implements ParentSelectionMethod {
    public int _k = 5;     //choose k individuals randomly
    public double _pro = 1.0;   //the probability of fittest individual chosen

    public TournamentParentSelectionMethod(){
    }
    public TournamentParentSelectionMethod(int k){
        _k = k;
    }
    public TournamentParentSelectionMethod(int k, double pro){
        _k = k;
        _pro = pro;
    }

    @Override
    public List<TTPSolution> select(List<TTPSolution> population) {
    	List<TTPSolution> selectedParents = new ArrayList<TTPSolution>(population.size());
        for(int i = 0; i < population.size(); i++) {
            selectedParents.add(getBestKIndividuals(population));
        }
        return selectedParents;
    }

    private TTPSolution getBestKIndividuals(List<TTPSolution> population){
    	// Pick _k individuals at random
    	Collections.shuffle(population);
    	List<TTPSolution> kSubList;
    	if (population.size() > _k) {
    		kSubList = population.subList(0, _k);
    	} else {
    		kSubList = population;
    	}

    	// Sort by fitness
    	Collections.sort(kSubList);
    	Collections.reverse(kSubList);

    	// Pick best individual
    	return kSubList.get(0);
    }


}
