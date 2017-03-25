package ec2017.ga.general.variation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

import ec2017.ga.general.CrossOverOperator;
import ec2017.ga.general.Symbol;

/**
 * An implementation of the cyclic recombination crossover operator
 * @author fergus
 *
 */
public class CrossOverCycle implements CrossOverOperator {
    public ArrayList<ArrayList<Symbol>>
    crossOver(ArrayList<Symbol> parentA,
              ArrayList<Symbol> parentB)
    {
    	// Initialize the two children
        ArrayList<ArrayList<Symbol>> result = new ArrayList<>(2);

        ArrayList<Symbol> childA = new ArrayList<>(parentA.size());
        ArrayList<Symbol> childB = new ArrayList<>(parentA.size());

        result.add(childA);
        result.add(childB);

        for (int i = 0; i < parentA.size(); i++) {
            childA.add(null);
            childB.add(null);
        }

        // For efficiency, create a map from Symbols to their index in
        // parentA
        HashMap<Symbol, Integer> indexMap = new HashMap<>(parentA.size());
        for (int i = 0; i < parentA.size(); i++) {
        	indexMap.put(parentA.get(i), i);
        }

        // Find all the cycles
        ArrayList<ArrayList<Integer>> cycleList = new ArrayList<>(parentA.size());
        HashSet<Integer> usedInCycle = new HashSet<>(parentA.size());

        for (int i = 0; i < parentA.size(); i++) {
        	// Skip anything that has already been placed in a cycle,
        	// so we don't find the same cycle several times
            if (usedInCycle.contains(i)) {
                continue;
            }

            // Otherwise, follow the cycle until it loops around and add it to the list of cycles
            ArrayList<Integer> newCycle = new ArrayList<>(parentA.size() - usedInCycle.size());
            int head = i;
            while (!usedInCycle.contains(head)) {
                usedInCycle.add(head);
                newCycle.add(head);
                head = indexMap.get(parentB.get(head));
            }

            cycleList.add(newCycle);
        }

        // Each child takes half the cycles from one parent and half from the other
        for (int i = 0; i < cycleList.size(); i++) {
            ArrayList<Integer> cycle = cycleList.get(i);

            for (int j : cycle) {
                if ((i % 2) == 0) {
                    childA.set(j, parentA.get(j));
                    childB.set(j, parentB.get(j));
                } else {
                    childA.set(j, parentB.get(j));
                    childB.set(j, parentA.get(j));
                }
            }
        }

        return result;
    }
}
