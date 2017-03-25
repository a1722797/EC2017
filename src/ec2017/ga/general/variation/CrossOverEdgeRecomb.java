package ec2017.ga.general.variation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import ec2017.ga.general.CrossOverOperator;
import ec2017.ga.general.Symbol;

/**
 * An implementation of the edge recombination crossover operator
 * @author fergus
 *
 */
public class CrossOverEdgeRecomb implements CrossOverOperator {
    public ArrayList<ArrayList<Symbol>>
    crossOver(ArrayList<Symbol> parentA,
              ArrayList<Symbol> parentB)
    {
    	// initialize the single child
        int size = parentA.size();

        ArrayList<ArrayList<Symbol>> result = new ArrayList<>(1);
        ArrayList<Symbol> child = new ArrayList<>(size);
        result.add(child);

        // Create the edge table
        HashMap<Symbol, ArrayList<Symbol>> edgeTable = new HashMap<>(size);
        // For efficiency, we also create a reverse table that goes from a Symbol
        // to all the Symbols in edgeTable that refer to it. This is so we don't
        // have to iterate over the whole table to remove instances of a Symbol
        HashMap<Symbol, HashSet<Symbol>> reverseTable = new HashMap<>(size);

        // Fill both tables with data
        for (int i = 0; i < size; i++) {
            Symbol symA = parentA.get(i);
            Symbol symB = parentB.get(i);

            ArrayList<Symbol> edgesA = edgeTable.computeIfAbsent(symA, (s) -> new ArrayList<>(4));
            ArrayList<Symbol> edgesB = edgeTable.computeIfAbsent(symB, (s) -> new ArrayList<>(4));

            Symbol symAnext = parentA.get((i+1) % size);
            Symbol symAprev = parentA.get((i+(size-1)) % size);
            edgesA.add(symAnext);
            edgesA.add(symAprev);

            Symbol symBnext = parentB.get((i+1) % size);
            Symbol symBprev = parentB.get((i+(size-1)) % size);
            edgesB.add(symBnext);
            edgesB.add(symBprev);

            reverseTable.computeIfAbsent(symAnext, (s) -> new HashSet<>(4)).add(symA);
            reverseTable.computeIfAbsent(symAprev, (s) -> new HashSet<>(4)).add(symA);
            reverseTable.computeIfAbsent(symBnext, (s) -> new HashSet<>(4)).add(symB);
            reverseTable.computeIfAbsent(symBprev, (s) -> new HashSet<>(4)).add(symB);
        }

        // Pick a random starting element
        Random rng = new Random();
        Symbol current = parentA.get(rng.nextInt(size));
        child.add(current);

        while (child.size() < size) {
        	// Get the list of choices and remove them from the edge table
        	ArrayList<Symbol> choices = edgeTable.remove(current);

            // Remove all references to the current symbol from the edge table
        	for (Symbol key : reverseTable.get(current)) {
        		// Use an iterator instead of remove because an edge can appear multiple times
        		Iterator<Symbol> it = edgeTable.get(key).iterator();
        		while (it.hasNext()) {
        			if (it.next().equals(current)) {
        				it.remove();
        			}
        		}
            }

        	// Remove all references to the current symbol from the reverse index
        	for (Symbol key : choices) {
        		reverseTable.get(key).remove(current);
        	}

        	// Pick the next Symbol and add it to the end of the child
            Symbol next = pickNext(edgeTable, choices, rng);
            current = next;

            child.add(current);
        }

        return result;
    }

    private Symbol pickNext(HashMap<Symbol, ArrayList<Symbol>> edgeTable,
    						ArrayList<Symbol> choices, Random rng)
    {
        // On an empty choice list, pick the next element at random
        if (choices.size() == 0) {
            choices = new ArrayList<>(edgeTable.keySet());
            return choices.get(rng.nextInt(choices.size()));
        }

        // Find all the edges in common between the two parents
        HashSet<Symbol> duplicates = new HashSet<>(4);
        HashSet<Symbol> choicesSet = new HashSet<>(4);
        for (Symbol s : choices) {
            if (!choicesSet.add(s)) {
                duplicates.add(s);
            }
        }

        // If there are common edges, restrict choices to them
        if (duplicates.size() > 0) {
            choices = new ArrayList<>(duplicates);
        }

        // Remove the choices that don't have the shortest list
        ArrayList<Symbol> best = new ArrayList<>(4);
        int bestLen = Integer.MAX_VALUE;
        for (Symbol choice : choices) {
            if (edgeTable.get(choice).size() < bestLen) {
                best.clear();
            }
            if (edgeTable.get(choice).size() <= bestLen) {
                best.add(choice);
            }
        }

        // Pick at random from what's left
        return choices.get(rng.nextInt(choices.size()));
    }
}
