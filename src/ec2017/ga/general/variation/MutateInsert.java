package ec2017.ga.general.variation;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

import ec2017.ga.general.MutateOperator;
import ec2017.ga.general.Symbol;

/**
 * 
 * @author Fergus
 */
public class MutateInversion implements MutateOperator
{
    @Override
    public List<Symbol> mutate(List<Symbol> genotype) 
    {
        Random rng = new Random();
        List<Symbol> result = new ArrayList<Symbol>(genotype);

        int x = rng.nextInt(result.size());
        int y = x;
        while (y == x) {
            y = rng.nextInt(result.size());
        }

        int start = Math.min(x,y);
        int end = Math.max(x,y);

        Symbol removed = result.remove(end);
        result.add(start+1, removed);

        return result;
    }
}
