package ec2017.ga.general.selection;

import ec2017.ga.general.Individual;
import ec2017.ga.general.SurvivorSelectionMethod;

import java.util.ArrayList;

/**
 * Implementation of elitism selection on both old and new generations
 *
 * Created by yanshuo on 17/3/15.
 */
public class ElistmBothSurvivorSelectionMethod extends ElistmOnlyChildrenSurvivorSelectionMethod implements SurvivorSelectionMethod {
    @Override
    public ArrayList<Individual> select(ArrayList<Individual> oldGeneration, ArrayList<Individual> newGeneration, int size) {
        ArrayList<Individual> bothGeneration = newGeneration;
        bothGeneration.addAll(oldGeneration);
        return getBestNIndividual(bothGeneration, size);
    }
}
