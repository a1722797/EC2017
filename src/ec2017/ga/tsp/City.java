package ec2017.ga.tsp;

import java.util.HashMap;

import ec2017.ga.general.Symbol;

/**
 * This class represents a single city in the traveling salesman problem.
 * @author pat
 *
 */
public class City implements Symbol
{
	private String _id;
	private long _x;
	private long _y;
	private HashMap<City, Double> _distanceCache = new HashMap<>();
	
	/**
	 * Create a new city
	 * @param id The city's unique ID
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public City(String id, long x, long y)
	{
		_id = id;
		_x = x;
		_y = y;
	}
	
	/**
	 * 
	 * @return The city's identifier.
	 */
	public String getId()
	{
		return _id;
	}
	
	/**
	 * This method calculates the distance traveling from this city to the given city. 
	 * @param city The given city.
	 * @return The distance to the given city.
	 */
	public double distanceTo(City city)
	{
		// This might save us a cycle or two on a heavily 
		// trafficed bit of code.
		if (_distanceCache.containsKey(city))
			return _distanceCache.get(city);
		
		// Some ol' school a^2 + b^2 == c^2. 
		long xDistance = Math.abs(_x - city._x);
		long yDistance = Math.abs(_y - city._y);
		double distance = Math.sqrt(xDistance*xDistance + yDistance*yDistance);
		
		_distanceCache.put(city, distance);
		
		return distance;
	}
	
	/**
	 * The cache holds a set of previously calculated distance values.
	 * @return
	 */
	protected HashMap<City, Double> cache()
	{
		return cache();
	}
	
	// Note, since we don't want to add an inefficient hashCode
	// which will just slow things down, HashSet may not give a 
	// unique set.
	@Override
	public boolean equals(Object o)
	{
		if(o == null || !City.class.isInstance(o)) return false;
		City that = (City)o;
		
		return 
				that._id.equals(this._id) 
				&& that._x == this._x 
				&& that._y == this._y;
	}
}
