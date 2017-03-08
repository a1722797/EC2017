package ec2017.ga.general.variation;

import java.util.ArrayList;

import ec2017.ga.general.MutateOperator;
import ec2017.ga.general.Symbol;

/**
 * This is a stub implementation for testing.
 * @author pat
 *
 */
public class MutateNop implements MutateOperator
{

	@Override
	public ArrayList<Symbol> mutate(ArrayList<Symbol> genotype) 
	{
		return new ArrayList<Symbol>(genotype);
	}

}
