package ec2017.ga.general.variation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;

import ec2017.ga.general.CrossOverOperator;
import ec2017.ga.general.Symbol;

public class CrossOverOrder implements CrossOverOperator {
    public ArrayList<ArrayList<Symbol>>
    crossOver(ArrayList<Symbol> parentA,
              ArrayList<Symbol> parentB)
    {
        ArrayList<ArrayList<Symbol>> result = new ArrayList();
        
        ArrayList<Symbol> childA = new ArrayList();
        ArrayList<Symbol> childB = new ArrayList();

        result.add(childA);
        result.add(childB);

        // Pick the sections to keep
        Random rng = new Random();
        int cutx = rng.nextInt(parentA.size());
        int cuty = cutx;
        while (cutx == cuty) {
            cuty = rng.nextInt(parentA.size());
        }
        int cutStart = Math.min(cutx, cuty);
        int cutEnd = Math.max(cutx, cuty);

        List<Symbol> subA = parentA.subList(cutStart,cutEnd+1);
        List<Symbol> subB = parentB.subList(cutStart,cutEnd+1);

        // Get the rest of the elements, ordered as they are in the
        // other parent
        List<Symbol> restA = new ArrayList(parentA);
        List<Symbol> restB = new ArrayList(parentB);
        restA.removeAll(subB);
        restB.removeAll(subA);

        // Copy everything into the children
        childA.addAll(restB.subList(0, cutStart));
        childB.addAll(restA.subList(0, cutStart));

        childA.addAll(subA);
        childB.addAll(subB);

        childA.addAll(restB.subList(cutStart, restB.size()));
        childB.addAll(restA.subList(cutStart, restA.size()));

        return result;
    }
}
