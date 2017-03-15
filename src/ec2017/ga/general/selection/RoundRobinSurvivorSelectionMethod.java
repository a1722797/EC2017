package ec2017.ga.general.selection;

import ec2017.ga.general.Individual;
import ec2017.ga.general.SurvivorSelectionMethod;

import java.util.ArrayList;

/**
 * Created by yanshuo on 17/3/15.
 */
public class RoundRobinSurvivorSelectionMethod implements SurvivorSelectionMethod {
    private int _q = 10;
    public RoundRobinSurvivorSelectionMethod(int q){
        _q = q;
    }
    @Override
    public ArrayList<Individual> select(ArrayList<Individual> oldGeneration, ArrayList<Individual> newGeneration, int size) {
        ArrayList<Individual> bothGeneration = newGeneration;
        int[] score = new int[newGeneration.size() + oldGeneration.size()];
        for(int i = 0; i < oldGeneration.size(); i++){
            bothGeneration.add(oldGeneration.get(i));
        }
        for(int i = 0; i < bothGeneration.size(); i++){
            score[i] = getScore(bothGeneration, i);
        }
        return getBestNSurvivor(bothGeneration, score, size);
    }

    private int getScore(ArrayList<Individual> candidates, int index){
        int score = 0;
        for(int i = 0; i < _q; i++){
            if(candidates.get(index).getFitness() > candidates.get( (int)(Math.random()*candidates.size()) ).getFitness()){
                score++;
            }
        }
        return score;
    }

    private ArrayList<Individual> getBestNSurvivor(ArrayList<Individual> candidates, int[] score, int size){
        int[] indexSet = new int[candidates.size()];
        ArrayList<Individual> result = new ArrayList<Individual>();
        for(int i = 0; i < candidates.size(); i++){
            indexSet[i] = i;
        }
        for(int i = 0; i < candidates.size(); i++){
            for(int j = 0; j < candidates.size(); j++){
                if(score[i] < score[j]){
                    int tmp = -1;
                    int tmpIndex = -1;

                    tmp = score[j];
                    score[j] = score[i];
                    score[i] = tmp;

                    tmpIndex = indexSet[j];
                    indexSet[j] = indexSet[i];
                    indexSet[i] = tmpIndex;

                }
            }
        }
        for(int i = 0; i < size; i++){
            result.add(candidates.get(indexSet[i]));
        }
        return result;
    }
}
