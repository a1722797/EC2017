package ea2017.ga.tsp;

/**
 * This class is identical to a city, but is has extra optimisation for the Symmetric case.
 * @author pat
 *
 */
public class SymCity extends City
{
	public SymCity(String id, int x, int y)
	{
		super(id, x, y);
	}
	
	public double distanceTo(SymCity city)
	{
		double distance = distanceTo(city);
		if (!city.cache().containsKey(city))
		{
			city.cache().put(this, distance);
		}
		return distance;
	}
	
}
