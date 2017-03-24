package ec2017.ga.general.variation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import ec2017.ga.general.CrossOverOperator;
import ec2017.ga.general.Symbol;

public class CrossOverPMX implements CrossOverOperator {
    public ArrayList<ArrayList<Symbol>>
    crossOver(ArrayList<Symbol> parentA,
              ArrayList<Symbol> parentB)
    {
        ArrayList<ArrayList<Symbol>> result = new ArrayList<>(2);

        ArrayList<Symbol> childA = new ArrayList<>(parentA.size());
        ArrayList<Symbol> childB = new ArrayList<>(parentA.size());

        result.add(childA);
        result.add(childB);

        for (int i = 0; i < parentA.size(); i++) {
            childA.add(null);
            childB.add(null);
        }

        // Pick the sections to keep
        Random rng = new Random();
        int cutx = rng.nextInt(parentA.size());
        int cuty = rng.nextInt(parentA.size());
        int cutStart = Math.min(cutx, cuty);
        int cutEnd = Math.max(cutx, cuty);

        List<Symbol> subA = parentA.subList(cutStart,cutEnd+1);
        List<Symbol> subB = parentB.subList(cutStart,cutEnd+1);

        Collections.copy(childA.subList(cutStart,cutEnd+1), subA);
        Collections.copy(childB.subList(cutStart,cutEnd+1), subB);

        mapRemainder(parentB, childA, cutStart);
        mapRemainder(parentA, childB, cutStart);

        return result;
    }

    private void mapRemainder(List<Symbol> parent, List<Symbol> child, int start) {
    	HashSet<Symbol> childContains = new HashSet<>(child);
    	childContains.remove(null); // Don't count the absence of a Symbol
    	HashMap<Symbol, Integer> parentIndex = new HashMap<>(parent.size());

    	for (int i = 0; i < parent.size(); i++) {
    		parentIndex.put(parent.get(i), i);
    	}

        for (int i = start; childContains.size() != child.size(); i = (i+1)%child.size())
        {
            Symbol toPlace = parent.get(i);
            if (childContains.contains(toPlace)) {
                continue;
            }

            int toPlaceIndex = i;
            while (child.get(toPlaceIndex) != null) {
                Symbol tracking = child.get(toPlaceIndex);
                toPlaceIndex = parentIndex.get(tracking);
            }
            child.set(toPlaceIndex, toPlace);
            childContains.add(toPlace);
        }
    }
}
