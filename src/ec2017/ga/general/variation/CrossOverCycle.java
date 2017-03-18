package ec2017.ga.general.variation;

import java.util.ArrayList;
import java.util.HashSet;

import ec2017.ga.general.CrossOverOperator;
import ec2017.ga.general.Symbol;

public class CrossOverCycle implements CrossOverOperator {
    public ArrayList<ArrayList<Symbol>>
    crossOver(ArrayList<Symbol> parentA,
              ArrayList<Symbol> parentB)
    {
        ArrayList<ArrayList<Symbol>> result = new ArrayList();
        
        ArrayList<Symbol> childA = new ArrayList();
        ArrayList<Symbol> childB = new ArrayList();

        result.add(childA);
        result.add(childB);

        for (int i = 0; i < parentA.size(); i++) {
            childA.add(null);
            childB.add(null);
        }

        ArrayList<ArrayList<Integer>> cycleList = new ArrayList();
        HashSet<Integer> usedInCycle = new HashSet();

        for (int i = 0; i < parentA.size(); i++) {
            if (usedInCycle.contains(i)) {
                continue;
            }

            ArrayList<Integer> newCycle = new ArrayList();
            int head = i;
            while (!usedInCycle.contains(head)) {
                usedInCycle.add(head);
                newCycle.add(head);
                head = parentA.indexOf(parentB.get(head));
            }

            cycleList.add(newCycle);
        }

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
