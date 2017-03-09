package ea2017.ga.tsp;

/**
 * This class is identical to a city, but is has extra optimisation for the Symmetric case.
 * @author pat
 *
 */
public class SymCity extends City
{
	/**
	 * Create a new symetrical city
	 * @param id The city's unique ID
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public SymCity(String id, int x, int y)
	{
		super(id, x, y);
	}
	
	/**
	 * Will return the distance to a given city
	 * @param city
	 * @return
	 */
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
