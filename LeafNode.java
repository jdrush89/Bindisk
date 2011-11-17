import java.awt.Point;
import java.util.ArrayList;

/**
 * // -------------------------------------------------------------------------
/**
 *  A Leaf Node in a PRQuadtree.  Can store up to 3 elements, using Points as
 *  keys.
 *  @param <T> the class type of the elements stored
 *
 *  @author Joshua Rush
 *  @author Benjamin Roble
 *  @version Oct 9, 2011
 */
public class LeafNode<T> extends Node<T>
{
    private ArrayList<T> items;
    private ArrayList<Point> keys;
    private MemoryManager manager;
    private Handle[] handles;
    // ----------------------------------------------------------
    /**
     * Create a new LeafNode object.
     */
    public LeafNode()
    {
        items = new ArrayList<T>();
        keys = new ArrayList<Point>();
        handles = new Handle[0];
    }

    public LeafNode(Handle[] handles, MemoryManager memman)
    {
        manager = memman;
        this.handles = handles;
    }
    /**
     * Add an item into this LeafNode.
     * @param item the item to add
     * @param key the key to associate with item
     */
    public void addItem(T item, Point key) {
        if (!isFull())
        {
            items.add(item);
            keys.add(key);
        }

    }

    /**
     * Get all the items in this LeafNode.
     * @return ArrayList<T> a list of all items
     */
    public ArrayList<T> getItems() {
        return items;
    }

    /**
     * Returns all the keys in this LeafNode
     * @return ArrayList<Point> a list of all the keys
     */
    public ArrayList<Point> getKeys() {
        return keys;
    }

    /**
     * Remove an item from this LeafNode
     * (if the item does not exist, nothing is removed)
     * @param item the item to remove if it exists
     */
    public void remove(T item)
    {
        int pos = items.indexOf(item);
        if (pos > -1)
        {
            items.remove(item);
            keys.remove(pos);
        }
    }

    /**
     * Remove an item from this LeafNode based on a key
     *  (if the item does not exist, nothing is removed)
     * @param removeKey the key to look for
     * @return T the element removed
     */
   public T remove(Point removeKey)
    {
        int pos = keys.indexOf(removeKey);
        if (pos > -1)
        {
            keys.remove(pos);
            return items.remove(pos);
        }
        return null;
    }

    /**
     * Tests if the LeafNode is empty
     * @return boolean true if empty
     */
    public boolean isEmpty()
    {
        return items.isEmpty();
    }

    /**
     * Tests if the LeafNode is full
     * @return boolean true if full
     */
    public boolean isFull()
    {
        if (items.size() > 2)
            return true;
        else
            return false;
    }

    /**
     * Returns the number of items in this LeafNode
     * @return the number of items in this LeafNode
     */
    public int getItemCount()
    {
        return handles.length;
    }
}
