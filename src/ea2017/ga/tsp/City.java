package ea2017.ga.tsp;

import java.util.HashMap;

import ec2017.ga.general.Symbol;

public class City implements Symbol
{
	private String _id;
	private int _x;
	private int _y;
	private HashMap<City, Double> _distanceCache = new HashMap<>();
	
	public City(String id, int x, int y)
	{
		_id = id;
		_x = x;
		_y = y;
	}
	
	public String getId()
	{
		return _id;
	}
	
	public double distanceTo(City city)
	{
		// This might save us a cycle or two on a heavily 
		// trafficed bit of code.
		if (_distanceCache.containsKey(city))
			return _distanceCache.get(city);
		
		// Some ol' school a^2 + b^2 == c^2. 
		int xDistance = Math.abs(_x - city._x);
		int yDistance = Math.abs(_y - city._y);
		double distance = Math.sqrt(xDistance*xDistance + yDistance*yDistance);
		
		_distanceCache.put(city, distance);
		
		return distance;
	}
	
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
