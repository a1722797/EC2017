package ec2017.ga.general.selection;

import ec2017.ga.general.SurvivorSelectionMethod;
import ttp.TTPSolution;

import java.util.List;

/**
 * Implementation of elitism selection on both old and new generations
 *
 * Created by yanshuo on 17/3/15.
 */
public class ElistmBothSurvivorSelectionMethod extends ElistmOnlyChildrenSurvivorSelectionMethod implements SurvivorSelectionMethod {
    @Override
    public List<TTPSolution> select(List<TTPSolution> oldGeneration, List<TTPSolution> newGeneration, int size) {
        List<TTPSolution> bothGeneration = newGeneration;
        bothGeneration.addAll(oldGeneration);
        return getBestNIndividual(bothGeneration, size);
    }
}
