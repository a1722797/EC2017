package ec2017.ga.general.variation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Collections;

import ec2017.ga.general.CrossOverOperator;
import ec2017.ga.general.Symbol;

public class CrossOverEdgeRecomb implements CrossOverOperator {
    public ArrayList<ArrayList<Symbol>>
    crossOver(ArrayList<Symbol> parentA,
              ArrayList<Symbol> parentB)
    {
        int size = parentA.size();

        ArrayList<ArrayList<Symbol>> result = new ArrayList();
        ArrayList<Symbol> child = new ArrayList();
        result.add(child);

        HashMap<Symbol, ArrayList<Symbol>> edgeTable = new HashMap();

        for (int i = 0; i < size; i++) {
            Symbol symA = parentA.get(i);
            Symbol symB = parentB.get(i);

            ArrayList<Symbol> edgesA = edgeTable.computeIfAbsent(symA, (s) -> new ArrayList());
            ArrayList<Symbol> edgesB = edgeTable.computeIfAbsent(symB, (s) -> new ArrayList());

            edgesA.add(parentA.get((i+1) % size));
            edgesA.add(parentA.get((i+(size-1)) % size));

            edgesB.add(parentB.get((i+1) % size));
            edgesB.add(parentB.get((i+(size-1)) % size));
        }

        ArrayList<Symbol> symbols = new ArrayList(edgeTable.keySet());
        Collections.shuffle(symbols);
        Symbol current = symbols.get(0);

        while (child.size() < parentA.size()) {
            child.add(current);

            // Remove current symbol from edge table
            for (Symbol key : edgeTable.keySet()) {
                while (edgeTable.get(key).remove(current)) {
                }
            }

            Symbol next = pickNext(edgeTable, current);
            edgeTable.remove(current);
            current = next;
        }

        ArrayList<ArrayList<Symbol>> ret = new ArrayList();
        ret.add(child);
        return ret;
    }

    private Symbol pickNext(HashMap<Symbol, ArrayList<Symbol>> edgeTable,
                            Symbol current)
    {
        ArrayList<Symbol> choices = edgeTable.get(current);

        // On an empty list, pick the next element at random
        if (choices.size() == 0) {
            choices = new ArrayList(edgeTable.keySet());
            Collections.shuffle(choices);
            return choices.get(0);
        }

        // Find all the common edges
        HashSet<Symbol> duplicates = new HashSet();
        HashSet<Symbol> choicesSet = new HashSet();
        for (Symbol s : choices) {
            if (!choicesSet.add(s)) {
                duplicates.add(s);
            }
        }

        // If there are common edges, restrict choices to them
        if (duplicates.size() > 0) {
            choices = new ArrayList(duplicates);
        }

        // Remove the choices that don't have the shortest list
        ArrayList<Symbol> best = new ArrayList();
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
        Collections.shuffle(choices);
        return choices.get(0);
    }
}
