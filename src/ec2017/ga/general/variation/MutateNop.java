package ec2017.ga.general.variation;

import java.util.List;
import java.util.ArrayList;

import ec2017.ga.general.MutateOperator;
import ec2017.ga.general.Symbol;

/**
 * This is a stub implementation for testing. Returns a shallow copy of the given genotype.
 * @author pat
 *
 */
public class MutateNop implements MutateOperator
{

	@Override
	public List<Symbol> mutate(List<Symbol> genotype) 
	{
		return new ArrayList<Symbol>(genotype);
	}

}
