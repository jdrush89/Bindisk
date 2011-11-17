
/**
 * // -------------------------------------------------------------------------
/**
 *  A basic record containing coordinates of the record's location.
 *
 *  @author Joshua Rush
 *  @author Benjamin Roble
 *  @version Oct 9, 2011
 */
public class Record {
    // the x coordinate of this record
    private int x;

    // the y coordinate of this record
    private int y;
    /**
     * Create a new Record with the specified coordinates.
     * @param x the x coordinate of this record
     * @param y the y coordinate of this record
     */
    public Record(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Get the x coordinate value for this record.
     * @return the x coordinate value
     */
    public int getX() {
    	return x;
    }
    /**
     * Get the y coordinate value for this record.
     * @return the y coordinate value
     */
    public int getY() {
    	return y;
    }
    /**
     * Return a String representation of this record, containing the x and y
     * coordinates of the record.
     * @return a string representing this record
     */
    public String toString() {
	String ret = x+ ","+y;

	return ret;
    }
    /**
     * Return whether or not the Object is equal to this Record.  Two Records
     * are equal if they have equal coordinates.
     */
    public boolean equals(Object obj)
    {
        if(!(obj instanceof Record)) return false;

    	Record rec = (Record)obj;

    	if (rec.getX() == x && rec.getY() == y)
            return true;
        else
            return false;
    }

}
