/**
 * // -------------------------------------------------------------------------
/**
 *  A class representing a simple CityRecord with a city name and coordinates.
 *
 *  @author Joshua Rush
 *  @author Benjamin Roble
 *  @version Oct 9, 2011
 */
public class CityRecord extends Record {

    // the name of this city
    private String name;
    /**
     * Create a new City Record with the specified information.
     * @param x the x coordinate of the city
     * @param y the y coordinate of the city
     * @param name the city's name
     */
    public CityRecord(int x, int y, String name) {
        super(x,y);
      	this.name = name;
    }
    /**
     * Return the city's name.
     * @return the name of the city
     */
    public String getName() {
      	return name;
    }
    /**
     * Return a string representation of this city.  The String contains the
     * x and y coordinates, followed by the name.
     */
    public String toString() {
	      String ret = super.toString() + "," + name;
	      return ret;
    }
}
