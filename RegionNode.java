import java.awt.Point;
import java.util.ArrayList;
/**
 * // -------------------------------------------------------------------------
/**
 *  A class representing an interior region node in a PRQuadtree.  The RegionNode
 *  has 4 children, one for each quadrant in the square area it represents.
 *  @param <T> the class type of the elements stored in the PRQuadtree
 *
 *  A RegionNode looks like this in memory:
 *  byte .... data
 *  0 ..... 0 if RegionNode, non zero if LeafNode
 *  1-4 ..... handle to NW quadrant (each handle is 4 bytes)
 *  5-8 ..... handle to NE quadrant
 *  9-12 ..... handle to SW quadrant
 *  13-16 ..... handle to SE quadrant
 *
 *
 *  @author Joshua Rush
 *  @author Benjamin Roble
 *  @version Oct 9, 2011
 */
public class RegionNode extends Node<T> {
    //The RegionNode has 4 node pointers, one for each quadrant in the region it
    //represents.
	private Handle northWest;
	private Handle northEast;
	private Handle southWest;
	private Handle southEast;
	private MemoryManager manager;
	/**
	 * Create a new RegionNode and assign its node pointers.
	 * @param NW the northWest node pointer
	 * @param NE the northeast node pointer
	 * @param SW the southWest node pointer
	 * @param SE the southEast node pointer
	 */
	public RegionNode(Handle NW, Handle NE, Handle SW, Handle SE, MemoryManager memman) {
	    super();
	    northWest = NW;
	    northEast = NE;
	    southWest = SW;
	    southEast = SE;
	    manager = memman;
	}

	/**
     * Create a new RegionNode and assign its node pointers.
     * @param NW the northWest node pointer
     * @param NE the northeast node pointer
     * @param SW the southWest node pointer
     * @param SE the southEast node pointer
     * @param pPos the handle position
     */
    public RegionNode(Handle NW, Handle NE, Handle SW, Handle SE, MemoryManager memman, int pPos) {
        super(pPos);
        northWest = NW;
        northEast = NE;
        southWest = SW;
        southEast = SE;
        manager = memman;
    }

    // ----------------------------------------------------------
    /**
     * Return the region's northwest child.
     * @return the northWest child
     */
    public Handle getNorthWest()
    {
        return northWest;
    }

    // ----------------------------------------------------------
    /**
     * Assign the region's northWest child.
     * @param northWest the northWest child to set
     */
    public void setNorthWest(Handle northWest)
    {
        this.northWest = northWest;
    }

    // ----------------------------------------------------------
    /**
     * Return the region's northEast child.
     * @return the northEast child
     */
    public Handle getNorthEast()
    {
        return northEast;
    }

    // ----------------------------------------------------------
    /**
     * Set the region's northEast child.
     * @param northEast the northEast to set
     */
    public void setNorthEast(Handle northEast)
    {
        this.northEast = northEast;
    }

    // ----------------------------------------------------------
    /**
     * Return the region's southWest child
     * @return the southWest child
     */
    public Handle getSouthWest()
    {
        return southWest;
    }

    // ----------------------------------------------------------
    /**
     * Assign the region's southWest child
     * @param southWest the southWest child to set
     */
    public void setSouthWest(Handle southWest)
    {
        this.southWest = southWest;
    }

    // ----------------------------------------------------------
    /**
     * Return the region's southEast child.
     * @return the southEast child
     */
    public Handle getSouthEast()
    {
        return southEast;
    }

    // ----------------------------------------------------------
    /**
     * Return the region's southEast child.
     * @param southEast the southEast child to set
     */
    public void setSouthEast(Handle southEast)
    {
        this.southEast = southEast;
    }

    /**
     * getItemCount returns the number of items in this RegionNode
     * @return int the number of items
     */
    public int getItemCount()
    {
        return northEast.getItemCount() + northWest.getItemCount() +
        southEast.getItemCount() + southWest.getItemCount();
    }

    /**
     * getItems returns the items in this RegionNode
     * @return ArrayList<T> the items
     */
    public ArrayList<T> getItems()
    {
        ArrayList<T> items = new ArrayList<T>();
        for (T item: northEast.getItems())
        {
            items.add(item);
        }
        for (T item: northWest.getItems())
        {
            items.add(item);
        }
        for (T item: southEast.getItems())
        {
            items.add(item);
        }
        for (T item: southWest.getItems())
        {
            items.add(item);
        }
        return items;
    }

    /**
     * getKeys returns the keys in this RegionNode
     * @return ArrayList<T> the keys
     */
    public ArrayList<Point> getKeys()
    {
        ArrayList<Point> keys = new ArrayList<Point>();
        for (Point key: northEast.getKeys())
        {
            keys.add(key);
        }
        for (Point key: northWest.getKeys())
        {
            keys.add(key);
        }
        for (Point key: southEast.getKeys())
        {
            keys.add(key);
        }
        for (Point key: southWest.getKeys())
        {
            keys.add(key);
        }
        return keys;
    }
}
