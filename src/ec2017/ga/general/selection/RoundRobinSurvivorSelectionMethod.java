package ec2017.ga.general.selection;

import ec2017.ga.general.Individual;
import ec2017.ga.general.SurvivorSelectionMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

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
    public ArrayList<Individual> select(ArrayList<Individual> oldGeneration, ArrayList<Individual> newGeneration, int size) {
    	// Combine the generations
        ArrayList<Individual> bothGeneration = newGeneration;
        bothGeneration.addAll(oldGeneration);

        // Calculate each individual's score
        HashMap<Individual, Integer> scores = new HashMap<>(bothGeneration.size());
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
    private int getScore(ArrayList<Individual> candidates, int index){
        int score = 0;
        for(int i = 0; i < _q; i++){
            if(candidates.get(index).getFitness() > candidates.get( (int)(Math.random()*candidates.size()) ).getFitness()){
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
    private ArrayList<Individual> getBestNSurvivor(ArrayList<Individual> candidates,
    		HashMap<Individual, Integer> scores, int size){
    	Comparator<Individual> comparator = new Comparator<Individual> () {
			@Override
			public int compare(Individual o1, Individual o2) {
				// We want higher scoring candidates to come first, so we negate the comparison
				return -Integer.compare(scores.get(o1), scores.get(o2));
			}
    	};

    	// Sort the list and return the best size
    	Collections.sort(candidates, comparator);
    	return new ArrayList<>(candidates.subList(0, size));
    }
}
