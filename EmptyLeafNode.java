/**
 * // -------------------------------------------------------------------------
/**
 *  A Quadtree Leaf node that doesn't hold any values.
 *  @param <T> the type of values stored in the quadtree the node is a part of.
 *
 *  @author Joshua Rush
 *  @author Benjamin Roble
 *  @version Oct 9, 2011
 */
public class EmptyLeafNode<T> extends LeafNode<T>
{

    /**
     * Creates a Leaf Node with no elements.
     */
    public EmptyLeafNode()
    {
        super();
    }

    // ----------------------------------------------------------
    /**
     * Prints out a letter representing this node
     */
    public void printLetter()
    {
        System.out.print("E");
    }

}
