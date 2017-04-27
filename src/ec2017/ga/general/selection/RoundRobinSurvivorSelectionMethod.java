package ec2017.ga.general.selection;

import ec2017.ga.general.SurvivorSelectionMethod;
import ttp.TTPSolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * A round robin survivor selector
 *
 * Created by yanshuo on 17/3/15.
 */
public class RoundRobinSurvivorSelectionMethod implements SurvivorSelectionMethod {
    private int _q = 10;

    public RoundRobinSurvivorSelectionMethod(){
    }
    public RoundRobinSurvivorSelectionMethod(int q){
        _q = q;
    }
    @Override
    public List<TTPSolution> select(List<TTPSolution> oldGeneration, List<TTPSolution> newGeneration, int size) {
    	// Combine the generations
    	List<TTPSolution> bothGeneration = newGeneration;
        bothGeneration.addAll(oldGeneration);

        // Calculate each individual's score
        HashMap<TTPSolution, Integer> scores = new HashMap<>(bothGeneration.size());
        for(int i = 0; i < bothGeneration.size(); i++){
            scores.put(bothGeneration.get(i), getScore(bothGeneration, i));
        }

        // Return the best scorers
        return getBestNSurvivor(bothGeneration, scores, size);
    }

    /**
     * Compare the Individual at the given index against _q others, choosen at random
     * @param candidates
     * @param index
     * @return
     */
    private int getScore(List<TTPSolution> candidates, int index){
        int score = 0;
        for(int i = 0; i < _q; i++){
            if(candidates.get(index).getObjective() > candidates.get( (int)(Math.random()*candidates.size()) ).getObjective()){
                score++;
            }
        }
        return score;
    }

    /**
     * Return the size best candidates from the input list, based on the scores in scores
     * @param candidates
     * @param scores
     * @param size
     * @return
     */
    private List<TTPSolution> getBestNSurvivor(List<TTPSolution> candidates,
    		HashMap<TTPSolution, Integer> scores, int size){
    	Comparator<TTPSolution> comparator = new Comparator<TTPSolution> () {
			@Override
			public int compare(TTPSolution o1, TTPSolution o2) {
				// We want higher scoring candidates to come first, so we negate the comparison
				return -Integer.compare(scores.get(o1), scores.get(o2));
			}
    	};

    	// Sort the list and return the best size
    	Collections.sort(candidates, comparator);
    	return new ArrayList<>(candidates.subList(0, size));
    }
}
