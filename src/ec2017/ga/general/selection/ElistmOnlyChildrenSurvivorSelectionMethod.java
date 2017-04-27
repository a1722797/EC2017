package ec2017.ga.general.selection;

import ec2017.ga.general.SurvivorSelectionMethod;
import ttp.TTPSolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of elitism selection on just the new generation
 *
 * Created by yanshuo on 17/3/15.
 */
public class ElistmOnlyChildrenSurvivorSelectionMethod implements SurvivorSelectionMethod {
    @Override
    public List<TTPSolution> select(List<TTPSolution> oldGeneration, List<TTPSolution> newGeneration, int size) {

        List<TTPSolution> candidates = newGeneration;
        List<TTPSolution> survivors = getBestNIndividual(candidates, size);
        return survivors;
    }

    /**
     * Return the size best candidates from the input list
     * @param candidates
     * @param size
     * @return
     */
    public List<TTPSolution> getBestNIndividual(List<TTPSolution> candidates, int size){
    	Collections.sort(candidates);
    	Collections.reverse(candidates);
    	return new ArrayList<>(candidates.subList(0, size));
    }
}
