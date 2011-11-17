import java.awt.Point;
import java.util.ArrayList;
/**
 * // -------------------------------------------------------------------------
/**
 *  A basic Node in a Quadtree.  Can return information about the items and keys
 *  in the tree.
 *  @param <T> the class tyoe of the elements stored in the tree this node is a
 *  part of
 *
 *  @author Joshua Rush
 *  @author Benjamin Roble
 *  @version Oct 9, 2011
 */
public abstract class Node<T> {
    /**
     * Create a Node
     */

    Handle handle;

    // empty constructor, as the memory manager will assign a handle later
    public Node()
    {
        //empty constructor
    }

    public Node(int pPos)
    {
       handle = new Handle(pPos);
    }
    /**
     * Return the number of items contained within this node, or in all nodes
     * that are children of this node.
     * @return the number of items
     */
    public abstract int getItemCount();
    /**
     * Return a list of all the items contained within this node, or in all nodes
     * that are children of this node.
     * @return an ArrayList containing all the items
     */
    public abstract ArrayList<T> getItems();
    /**
     * Return a list of all the keys contained within this node, or in all nodes
     * that are children of this node.
     * @return an ArrayList containing all the keys
     */
    public abstract ArrayList<Point> getKeys();
}

