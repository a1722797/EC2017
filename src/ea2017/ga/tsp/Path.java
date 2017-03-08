package ea2017.ga.tsp;

import java.util.ArrayList;

import ec2017.ga.general.CrossOverOperator;
import ec2017.ga.general.Individual;
import ec2017.ga.general.MutateOperator;
import ec2017.ga.general.Symbol;

/**
 * A Path is an complete trip for the salesman.
 * @author pat
 *
 */
public class Path implements Individual 
{
	private ArrayList<City> _cities = new ArrayList<City>();
	private int _startIndex = 0;
	private double _distance = 0; // If we see a winning distance of zero we'll know we messed up.
	
	/**
	 * This is package protected since we only want to use this for a seed individual. 
	 */
	Path()
	{
	}
	
	public Path(ArrayList<City> cities)
	{
		_cities = cities;
		initialize();
	}
	
	private void initialize()
	{
		// This is to make sure that the array has more than 1 city. Otherwise we're being silly.
		if (_cities.size() < 2)
		{
			throw new RuntimeException("You've tried to calculate the distance with not enough cities.");
		}
		
		// We're going to set our starting city to eliminate the largest single distance between two cities.
		// This should make our search space smaller, and give us the best version of this permutation 
		// super cheap.
		double maxSingleDistance = 0;
		City previousCity = _cities.get(_cities.size() - 1);
		
		for (int i = 0; i < _cities.size(); i++)
		{
			City city = _cities.get(i);
			double singleDistance = previousCity.distanceTo(city);
			if (singleDistance > maxSingleDistance)
			{
				maxSingleDistance = singleDistance;
				_startIndex = i;
			}
			
			_distance += singleDistance;
		}
		_distance -= maxSingleDistance;
	}
	
	/**
	 * Gives us the total distance of this path.
	 * @return
	 */
	public double getDistance()
	{
		return _distance;
	}
	
	/**
	 * Since we want a smaller distance, fitness is a negative version of the distance.
	 * (More fit with less distance).
	 * @return
	 */
	@Override
	public double getFitness()
	{
		return -_distance;
	}
	
	@SuppressWarnings("unchecked")
	@Override 
	public ArrayList<Symbol> getGenotype()
	{
		// We'll return a clone so they don't mess up our ordering.
		return (ArrayList<Symbol>) _cities.clone();
	}
	
	/**
	 * This method allows us to compare paths for fitness.
	 * The compareTo method will return -1 if this individual is less fit,
	 * 0 if the two are equal, and 1 if this individual is more fit.
	 */
	@Override
	public int compareTo(Individual other) 
	{
		// We can only compare classes, so we're going to throw an expception.
		if (!Path.class.isInstance(other))
		{
			throw new IllegalArgumentException("Path can only be compared to other Path objects.");
		}
		
		// Cast our individual to a path.
		Path that = (Path)other;
		
		// Get our total distances.
		double thisDistance = this.getDistance();
		double thatDistance = that.getDistance();
		
		// Compare for this being less fit, or more fit. If neither then we are equal.
		if (thisDistance > thatDistance) return -1;
		if (thisDistance < thatDistance) return 1;
		return 0;
	}

	@Override
	public Individual mutate(MutateOperator mutateOp)
	{
		ArrayList<Symbol> genotype = mutateOp.mutate(getGenotype());
		return create(genotype);
	}

	@Override
	public Individual crossOver(Individual otherParent, CrossOverOperator crossOp) 
	{
		ArrayList<Symbol> genotype = crossOp.crossOver(getGenotype(), otherParent.getGenotype());
		return create(genotype);
	}
	
	@Override
	public Individual create(ArrayList<Symbol> genotype)
	{
		ArrayList<City> cities = new ArrayList<City>();
		
		for(Symbol symbol : genotype)
		{
			if (City.class.isAssignableFrom(symbol.getClass()))
			{
				cities.add((City) symbol);
			}
			else
			{
				throw new IllegalArgumentException("You need to provide a list of cities.");
			}
		}
		
		return new Path(cities);
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Distance:");
		sb.append(_distance);
		sb.append("\nPath: [");
		int i = _startIndex;
		do
		{
			City city = _cities.get(i);
			sb.append(city.getId());
			sb.append(", ");
			
			i++;
			if (i == _cities.size()) i = 0;
			
		} while (i != _startIndex);
		
		String s = sb.toString();
		s = s.substring(0, s.length()-2) + "]";
		
		return s;
	}
}
