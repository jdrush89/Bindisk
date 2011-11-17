import java.nio.ByteBuffer;
import java.awt.Point;
import java.util.ArrayList;

/**
 * // -------------------------------------------------------------------------
/**
 *  A Point Region Quadtree for storing objects of generic type T according to
 *  x and y coordinates.  Objects can be inserted, removed, and searched for by
 *  region.
 *  @param <T> the generic type of the objects being stored
 *
 *  @author Joshua Rush
 *  @author Benjamin Roble
 *  @version Sep 30, 2011
 */
public class PRQuadTree<T>
{
    /**
     * MAX_COORD is the maximum coordinate value this quadtree can contain (2^14 - 1)
     */
    public final int MAX_COORD = (int)Math.pow(2, 14) - 1;

    private final Handle EMPTY = new Handle(-1);

    private MemoryManager manager;
    //The root node of the quadtree
    private Handle root;
    //a flyweight for an empty leaf node
    private EmptyLeafNode<T> empty;
    //a boolean flag that is set when an insertion fails
    private boolean failedInsert;
    //a counter for the number of nodes looked at during a search
    private int searchCount;
    /**
     * Create a new quadtree with an empty root.
     */
    public PRQuadTree(MemoryManager memman)
    {
        empty = new EmptyLeafNode<T>();
        root = EMPTY;
        failedInsert = false;
        manager = memman;
    }
    /**
     * Insert the specified record into the appropriate place in the quadtree.
     * @param element the element to be inserted
     * @param x the x coordinate where the element will be stored
     * @param y the y coordinate where the element will be stored
     * @return if the insert was successful
     */
    public boolean insert(T element, int x, int y)
    {
        failedInsert = false;
        root =  insertHelp(0, MAX_COORD, 0, MAX_COORD, root, element, new Point(x, y));
        if (failedInsert)
            return false;
        else
            return true;
    }
    /**
     * Print out a String representation of the quadtree.  Traverse in a
     * preorder fashion from the root, printing I for internal node, E for empty
     * leaf, and a string representation of the records in a non-empty leaf.
     */
    public void debug()
    {
        preOrderTraverse(root);
    }
    /**
     * Visit each Node in the tree in a preorder fashion.  The NW, NE, SW, and
     * SE children are visited in that order after visiting the root.
     * @param rootNode the root of the tree/subtree being traversed.
     */
    @SuppressWarnings("unchecked")
    public void preOrderTraverse(Node<T> rootNode)
    {
        if (rootNode == null)
            return;
        visit(rootNode);
        if (rootNode instanceof RegionNode)
        {
            RegionNode<T> region = (RegionNode<T>)rootNode;
            preOrderTraverse(region.getNorthWest());
            preOrderTraverse(region.getNorthEast());
            preOrderTraverse(region.getSouthWest());
            preOrderTraverse(region.getSouthEast());
        }
    }
    /**
     * Print out a representation of a node in the quadtree.  RegionNodes are
     * represented with an 'I', emmpty leaf nodes with an 'E' and leaf nodes
     * with a printout of the items they store.
     * @param node the node to print a representation of
     */
    public void visit(Node<T> node)
    {
        if(node instanceof RegionNode<?>)
            System.out.print("I");
        else if(node.equals(empty))
            System.out.print("E");
        else
        {
            //node is leaf, print its records
            LeafNode<T> leaf = (LeafNode<T>)node;
            ArrayList<T> records = leaf.getItems();
            for(T record: records)
            {
                System.out.print(record.toString());
            }
            System.out.print("|");
        }
    }

    /**
     * Insert a record into the tree.  Traverse down to the correct leaf, and
     * split the leaf into 4 regions if necessary.
     * @param xmin the min x value for the region being inserted into
     * @param xmax the max x value for the region being inserted into
     * @param ymin the min y value for the region being inserted into
     * @param ymax the max y value for the region being inserted into
     * @param rootNode the root of the tree being inserted into
     * @param element the element being inserted
     * @param coords the coordinates for the element being inserted, stored in a Point
     * @return the root of a new Quadtree with the record inserted
     */
    public Node<T> insertHelp(int xmin, int xmax, int ymin, int ymax,
        Node<T> rootNode, T element, Point coords)
    {
        //check what type of node rootNode is.
        if (rootNode instanceof RegionNode<?>)
        {
            RegionNode<T> regionRoot = (RegionNode<T>)rootNode;
            //We're not at a leaf, so find which node to try to insert to next.
            String regionName = determineRegion(coords, xmin, ymin, xmax, ymax);
            if (regionName.equals("SE"))
                regionRoot.setSouthEast(insertHelp((xmax + xmin)/2 + 1, xmax,
                    (ymax + ymin)/2 + 1, ymax, regionRoot.getSouthEast(), element, coords));
            else if (regionName.equals("NE"))
                regionRoot.setNorthEast(insertHelp((xmax + xmin)/2 + 1, xmax, ymin,
                    (ymax + ymin)/2, regionRoot.getNorthEast(), element, coords));
            else if (regionName.equals("SW"))
                regionRoot.setSouthWest(insertHelp(xmin, (xmax + xmin)/2, (ymax + ymin)/2 + 1, ymin,
                    regionRoot.getSouthWest(), element, coords));
            else
                regionRoot.setNorthWest(insertHelp(xmin, (xmax + xmin)/2, ymin, (ymax + ymin)/2,
                    regionRoot.getNorthWest(), element, coords));
            //return the RegionNode that set it's region through recursion
            return regionRoot;
        }
        else if (rootNode.equals(empty))
        {
            //we're at an empty leaf, return a new Leafnode with the record.
            LeafNode<T> newLeaf = new LeafNode<T>();
            newLeaf.addItem(element, coords);
            return newLeaf;
        }
        else
        {
            //We're at a leaf node, check to see if the record is already
            //present.
            LeafNode<T> leaf = (LeafNode<T>)rootNode;
            ArrayList<T> leafItems = leaf.getItems();
            for(T item: leafItems)
            {
                if (item.equals(element))
                {
                    //if the record is already present, set the failedinsert
                    //flag and return the leaf without inserting.
                    failedInsert = true;
                    return leaf;
                }
            }
            if (leaf.isFull())
            {
                //the leaf is full, split it into 4 leaves under a new RegionNode and try the insertion again
                RegionNode<T> newRegion = split(xmin, xmax, ymin, ymax,
                    leaf);
                return insertHelp(xmin, xmax, ymin, ymax, newRegion, element, coords);
            }
            else
            {
                //the leaf isn't full, add it the record to it and return it.
                leaf.addItem(element, coords);
                return leaf;
            }
        }

    }
    /**
     * Convert a leaf node into a region node and 4 leaf nodes.  Insert the leaf
     * node's records into the correct leaves of the new region.
     * @param xmin the minimum x bounds for the new regionNode
     * @param xmax the max x bound for the new regionNode
     * @param ymin the minimum y bound for the new regionNode
     * @param ymax the max y bound for the new regionNode
     * @param leaf the leaf being split
     * @return the new RegionNode with leaves containing the previous leaf's
     * records
     */
    public RegionNode<T> split(int xmin, int xmax, int ymin,
        int ymax, LeafNode<T> leaf)
    {
        RegionNode<T> regionNode = new RegionNode<T>(empty, empty,
            empty, empty);
        //get the items and keys stored in the leaf
        ArrayList<T> items = leaf.getItems();
        ArrayList<Point> coords = leaf.getKeys();
        for(T item: items)
        {
            //add all the items in to the new region with their respective coordinates
            regionNode = (RegionNode<T>)insertHelp(xmin, xmax, ymin, ymax,
                regionNode, item, coords.get(items.indexOf(item)));

        }
        return regionNode;
    }
    /**
     * Remove the item with the given x and y coordinates.  Return the item removed,
     * or null if there wasn't an item at the specified coordinates.
     * @param x the x coordinate of the item to remove
     * @param y the y coordinate of the item to remove
     * @return the item removed
     */
    public T remove(int x, int y)
    {
        ArrayList<T> toRemove = new ArrayList<T>();
        //search for the coordinates to make sure a record to remove exists.
        searchHelp(new Point(x, y), 0, root,
            toRemove, 0, 0, MAX_COORD, MAX_COORD);
        if (!toRemove.isEmpty())
        {
            //remove the node
            root = removeHelp(root, new Point(x, y), 0, 0, MAX_COORD, MAX_COORD);
            return toRemove.get(0);
        }
        else
            return null;
    }
    /**
     * Progress through the tree until the item is found, then remove it.
     * Merge any regionNodes that have fewer than 4 items under them into a single
     * leaf node.
     * @param node the node currently being looked at (root of current subtree)
     * @param coords the coordinates of the point where the item to remove is stored
     * @param xmin the minimum x value for the region being examined
     * @param ymin the minimum y value for the region being examined
     * @param xmax the maximum x value for the region being examined
     * @param ymax the maximum y value for the region being examined
     * @return the root of the subtree with the item removed
     */
    public Node<T> removeHelp(Node<T> node, Point coords, int xmin, int ymin,
        int xmax, int ymax)
    {
        //Determine what type of node is currently being looked at.
        if (node instanceof RegionNode<?>)
        {
            RegionNode<T> region = (RegionNode<T>)node;
            //determine which region the node to be deleted is in.
            String regionName = determineRegion(coords, xmin, ymin, xmax, ymax);
            if (regionName.equals("SE"))
                region.setSouthEast(removeHelp(region.getSouthEast(), coords,
                    (xmax + xmin)/2 + 1, (ymax + ymin)/2 + 1, xmax, ymax));
            else if (regionName.equals("NE"))
                region.setNorthEast(removeHelp(region.getNorthEast(), coords,
                    (xmax + xmin)/2 + 1, ymin, xmax, (ymax + ymin)/2));
            else if (regionName.equals("SW"))
                region.setSouthWest(removeHelp(region.getSouthWest(), coords,
                    xmin, (ymax + ymin)/2 + 1, (xmax + xmin)/2, ymax));
            else
                region.setNorthWest(removeHelp(region.getNorthWest(), coords,
                    xmin, ymin, (xmax + xmin)/2, (ymax + ymin)/2));

            //check how many records we have in our children.
            if (region.getItemCount() < 4)
            {
                //There's less than 4 items in this region, merge it into a
                //single leaf.
                ArrayList<T> items = region.getItems();
                ArrayList<Point> keys = region.getKeys();
                //make a new leaf with this region's items and return it
                LeafNode<T> newLeaf = new LeafNode<T>();
                for (int i = 0; i < items.size(); i++)
                {
                    newLeaf.addItem(items.get(i), keys.get(i));
                }
                return newLeaf;
            }
            else
                return region;
        }
        else
        {
            //otherwise the node is the leaf we're looking for, delete the record.
            LeafNode<T> leaf = (LeafNode<T>)node;
            leaf.remove(coords);
            if (leaf.isEmpty())
                return empty;
            else
                return leaf;
        }
    }

    /**
     * Given a point and region boundries, determine which quadrant of the
     * region the point lies in.  Return a String representing the quadrant.
     * @param coords the Point being checked
     * @param xmin the minimum x value for the region
     * @param ymin the minimum y value for the region
     * @param xmax the maximum x value for the region
     * @param ymax the maximum y value for the region
     * @return a two character string representing the quadrant the point is in.
     * @precondition the point is in the region bounds
     */
    private String determineRegion(Point coords, int xmin, int ymin, int xmax, int ymax)
    {
        if (coords.x > (xmax + xmin)/2)
        {
            //the point is in the the east side, check the y value
            if (coords.y > (ymax + ymin)/2)
                return "SE";
            else
                return "NE";
        }
        //the point is in the west side, check the y value
        else if (coords.y > (ymax+ ymin)/2)
            return "SW";
        else
            return "NW";
    }

    /**
     * Search the quadtree with a search area centered at (x, y) and indicated search
     * radius.
     * @param x the x coordinate of the search area center
     * @param y the y coordinate of the search area center
     * @param radius the radius of the search area
     * @return an ArrayList containing all the items within the search area
     */
    public ArrayList<T> search(int x, int y, int radius)
    {
        ArrayList<T> results = new ArrayList<T>();
        searchCount = 0;
        searchHelp(new Point(x, y), radius, root, results, 0, 0, MAX_COORD, MAX_COORD);
        System.out.println("Searched " + searchCount + " nodes");

        return results;
    }
    /**
     * Recursively search each node within the given search region.
     * @param center the coordinates of the search point
     * @param radius the radius of the search area
     * @param rootNode the root node of the subtree being searched
     * @param currList the current List of items within the search region so far
     * @param xmin the minimum x value for the region being examined
     * @param ymin the minimum y value for the region being examined
     * @param xmax the maximum x value for the region being examined
     * @param ymax the maximum y value for the region being examined
     */
    private void searchHelp(Point center, int radius, Node<T> rootNode, ArrayList<T> currList,
        int xmin, int ymin, int xmax, int ymax)
    {
        searchCount ++;
        //make sure the node isn't null
        if (rootNode == null)
        {
          return;
        }
        //check for a leaf node, if so, check the leaf's elements.
        else if (rootNode instanceof LeafNode<?>)
        {
            leafSearch(center, radius, (LeafNode<T>)rootNode, currList);
        }
        //if regionNode, check to see if the search area intersects each region
        //call searchHelp on all regions that intersect.
        else if (rootNode instanceof RegionNode<?>)
        {
            RegionNode<T> region = (RegionNode<T>)rootNode;
            if (regionContains(center, radius, xmin, ymin, xmax/2, ymax/2))
                searchHelp(center, radius, region.getNorthWest(), currList, xmin, ymin, xmax/2, ymax/2);
            if (regionContains(center, radius, xmax/2 + 1, ymin, xmax, ymax/2))
                searchHelp(center, radius, region.getNorthEast(), currList, xmax/2 + 1, ymin, xmax, ymax/2);
            if (regionContains(center, radius, xmax/2 + 1, ymax/2 + 1, xmax, ymax))
                searchHelp(center, radius, region.getSouthEast(), currList, xmax/2 + 1, ymax/2 + 1, xmax, ymax);
            if (regionContains(center, radius, xmin, ymax/2 + 1, xmax/2, ymax))
                searchHelp(center, radius, region.getSouthWest(), currList, xmin, ymax/2 + 1, xmax/2, ymax);
        }
    }
    /**
     * Return whether or not any part of the circular search area is contained
     * within the rectangular region given by the x and y bounds.  The search
     * area is in the region if the center is in the region, or if the
     * circumference intersects the edge of the rectangle.
     */
    private boolean regionContains(Point center, int radius, int xmin, int ymin, int xmax, int ymax)
    {
        //if the center is in the rectangle, return true
        if (center.x <= xmax && center.x >= xmin && center.y <= ymax && center.y >= ymin)
            return true;
        //find which edge is closest
        if (center.y > ymax)
        {
            //use bottom, find closest point
            if (center.x >= xmax)  //use BR corner
                return (getDistance(center, new Point(xmax, ymax)) <= radius);
            else if (center.x <= xmin)   //use BL corner
                return (getDistance(center, new Point(xmin, ymax)) <= radius);
            else  //use point above center on bottom edge
                return (getDistance(center, new Point(center.x, ymax)) <= radius);
        }

        else if (center.y < ymin)
        {
            //use top, find closest point
            if (center.x >= xmax) //use TR corner
                return (getDistance(center, new Point(xmax, ymin)) <= radius);
            if (center.x <= xmax) //use TL corner
                return (getDistance(center, new Point(xmin, ymin)) <= radius);
            else
                return (getDistance(center, new Point(center.x, ymin)) <= radius);
        }
        else if (center.x > xmax)
        {
            //use right edge, the closest point has the center's y value
            return (getDistance(center, new Point(xmax, center.y)) <= radius);
        }
        else
        {
            //use left edge, the closest point has the center's y value
            return (getDistance(center, new Point(xmin, center.y)) <= radius);
        }
    }
    /**
     * Use the distance formula to get the distance between 2 points.
     * @param p1 the first point
     * @param p2 the second point
     * @return the distance between the points
     */
    private double getDistance(Point p1, Point p2)
    {
      return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    /**
     * Search a leaf node to find if any of its items are stored within the
     * radius of the given point.
     * @param center the coordinates of the search point
     * @param radius the radius of the search area
     * @param leaf the leaf node being searched
     * @param currList the list of items in the search so far.  Items found are
     * added here.
     */
    private void leafSearch(Point center, int radius, LeafNode<T> leaf, ArrayList<T> currList)
    {
        ArrayList<Point> points = leaf.getKeys();
        ArrayList<T> items = leaf.getItems();
        //search each leaf key for a point match
        for (Point point: points)
        {
            if (getDistance(point, center) <= radius)
                currList.add(items.get(points.indexOf(point)));
        }
    }

    /**
     * Clear all the nodes in the tree by setting the root to an empty node.
     */
    public void clear()
    {
        root = empty;
    }

    private Node getRoot()
    {
        return new Node()
    }

    private static Node makeNode(byte[] nodeMessage)
    {
        ByteBuffer bbuff = ByteBuffer.allocate(4);
        if (nodeMessage[0] == 0)
        {
            //the node is internal, get the children Handles
            Handle[] handles = new Handle[4];
            for (int i = 0; i <= 12; i += 4)
            {
                for(int j = 0; j < 4; j++)
                {
                    bbuff.put(nodeMessage[j+i+1]);
                }
                handles[i/4] = new Handle(bbuff.getInt(0));
                bbuff.clear();
            }
            return new RegionNode(handles[0], handles[1], handles[2], manager);
        }
    }
}
