package ec2017.ga.general.selection;

import ec2017.ga.general.Individual;
import ec2017.ga.general.SurvivorSelectionMethod;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Implementation of elitism selection on just the new generation
 *
 * Created by yanshuo on 17/3/15.
 */
public class ElistmOnlyChildrenSurvivorSelectionMethod implements SurvivorSelectionMethod {
    @Override
    public ArrayList<Individual> select(ArrayList<Individual> oldGeneration, ArrayList<Individual> newGeneration, int size) {

        ArrayList<Individual> candidates = newGeneration;
        ArrayList<Individual> survivors = getBestNIndividual(candidates, size);
        return survivors;
    }

    /**
     * Return the size best candidates from the input list
     * @param candidates
     * @param size
     * @return
     */
    public ArrayList<Individual> getBestNIndividual(ArrayList<Individual> candidates, int size){
    	Collections.sort(candidates);
    	Collections.reverse(candidates);
    	return new ArrayList<>(candidates.subList(0, size));
    }
}
