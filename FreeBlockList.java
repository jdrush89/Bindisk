
// -------------------------------------------------------------------------
/**
 *  A class that represents free blocks in a memory pool.
 *  The blocks are arranged in decreasing order according to size
 *  in a doubly linked list.  Each block knows its size and index in memory;
 *  blocks can be used or freed.
 *
 *  @author Joshua Rush
 *  @author Benjamin Roble
 *  @version Aug 29, 2011
 */
public class FreeBlockList
{
    //NOT_ENOUGH_SPACE serves as a value to indicate not enough space was in the
    //freelist to process a request.
    private final int NOT_ENOUGH_SPACE = -1;
    //head and tail serve as sentinel nodes in the doubly linked list
    private Node head;
    private Node tail;
    //memorySize holds the value for the Freelist's total capacity.
    private int memorySize;

    /**
     * Create a new FreeblockList with one node representing a free
     * block the size of the memory pool.
     * @param memorySize the initial size of the freeblockList in bytes.
     */
    public FreeBlockList(int memorySize)
    {
        head = new Node(0, 0);
        tail = new Node(0, 0);
        this.memorySize = memorySize;
        Node node = new Node(memorySize, 0, tail, head);
        head.next = node;
        tail.prev = node;
    }

    /**
     * Uses up a block of the indicated size.  If not enough space exists,
     * an error is printed and NOT_ENOUGH_SPACE is returned.  The freelist
     * will be sorted in decreasing size order after using a block.
     * @param size the size of the block of memory being used.
     * @return the position in memory where the used block will start or -1 if
     * not enough space exists.
     */
    public int use(int size)
    {
        Node currentNode = tail;
        //search from the tail to find the smallest free block that can be used.
        while (currentNode.size < size)
        {
            currentNode = currentNode.prev;
            //if we get to head, there wasn't a block big enough for the
            //requested size to be used.
            if (currentNode == head)
            {
                return NOT_ENOUGH_SPACE;
            }
        }
        remove(currentNode);
        //if the node used has left over space, sort a new node with that size.
        if (currentNode.size > size)
        {
            sort(new Node(currentNode.size - size, currentNode.index + size));
        }
        return currentNode.index;
    }
    /**
     * A node of the indicated size and position is added to the
     * freelist.  Merge adjacent nodes if necessary.  The freelist will be
     * sorted after merging.
     * @param size the size in bytes of the new node being added
     * @param position the position in memory where the new node starts
     */
    public void free(int size, int position)
    {
        if (size > memorySize || position < 0 || position > memorySize)
        {
            System.out.println("invalid size or position specified");
            return;
        }
        Node newNode = new Node(size, position);
        newNode = merge(newNode);
        sort(newNode);
    }
    /**
     * Returns a String representation of the FreeBlockList.
     * The size and index of each free block is printed in order
     * of decreasing size.
     * @return a string representation of the FreeBlockList
     */
    public String toString()
    {
        Node currentNode = head.next;
        String st = "Freelist:\t";
        //for each node, print it's position and size information.
        while(currentNode != tail)
        {
            st += currentNode.toString();
            if(currentNode.next != tail)
            {
                st += ";\n\t\t";
            }
            currentNode = currentNode.next;
        }
        return st;
    }

    /**
     * Sort the new node into the correct position in the linked list.
     * @postcondition the free list is in decreasing size order.
     * @param node the node to be sorted into the list
     */
    private void sort(Node node)
    {
        Node currentNode = head.next;
        Node newNode = node;
        //progress through the list to find where the node should go.
        while (newNode.next == null)
        {
            //if you get to the end or find a smaller node, or a node with equal
            //size and smaller index, insert the node.
            if (currentNode == tail || currentNode.size < newNode.size ||
               (currentNode.size == newNode.size &&
                    newNode.index < currentNode.index))
            {
                insertBefore(newNode, currentNode);
                return;
            }
            currentNode = currentNode.next;
        }
    }
    /**
     * Insert the new node before the target node in the linked list.
     * @param newNode the node to be inserted into the list
     * @param beforeNode the node the new node will be added before
     */
    private void insertBefore(Node newNode, Node beforeNode)
    {
        beforeNode.prev.next = newNode;
        newNode.next = beforeNode;
        newNode.prev = beforeNode.prev;
        beforeNode.prev = newNode;
    }
    /**
     * Merge any adjacent nodes to the node being added to the list
     * and return the merged node.
     * @param newNode the node being added to the list
     * @return the new Node after merging with any adjacent nodes
     */
    private Node merge(Node newNode)
    {
        Node currentNode = head.next;
        Node mergeNode = newNode;
        //loop throught the list, checking for adjacent nodes.
        while (currentNode != tail)
        {
            //if the index+size of a node = the index of another,
            //they are adjacent.  Remove the adjacent node and form a larger
            //merged node.
            if (currentNode.index + currentNode.size == mergeNode.index)
            {
                remove(currentNode);
                mergeNode.size += currentNode.size;
                mergeNode.index = currentNode.index;
            }
            else if (mergeNode.index + mergeNode.size == currentNode.index)
            {
                remove(currentNode);
                mergeNode.size += currentNode.size;
            }
            currentNode = currentNode.next;
        }
        return mergeNode;
    }
    /**
     * Remove the specified node from the list by removing the references to it.
     * @precondition the freelist contains the specified node.
     * @param nodeToRemove the node being removed from the list
     */
    private void remove(Node nodeToRemove)
    {
        nodeToRemove.prev.next = nodeToRemove.next;
        nodeToRemove.next.prev = nodeToRemove.prev;
    }

    /**
     * A basic node in a doubly linked list, storing a single data value and
     * references to both the next node and the previous node in the linked
     * chain.
     */
    private static class Node
    {
      //~ Instance/static variables .........................................

        private int size;
        private int index;
        private Node next;
        private Node prev;


        // ----------------------------------------------------------

        /**
         * Creates a node with null next and previous pointers.
         * @param size the size of memory this node represent
         * @param index the starting position of this block of memory
         */
        public Node(int size, int index)
        {
            this(size, index, null, null);
        }

        /**
         * Creates a node with a link to the specified node.
         * @param size the size of free space the node represents
         * @param index the position the block starts at
         * @param next the node to follow this one in the list
         * @param prev the node to precede this one in the list
         */
        public Node(int size, int index, Node next, Node prev)
        {
            setSize(size);
            setIndex(index);
            setNext(next);
            setPrev(prev);
        }

        //~ Public methods ....................................................

        // ----------------------------------------------------------
        /**
         * Set the size value stored in this node.
         * @param value the new size value to set
         */
        public void setSize(int value)
        {
            size = value;
        }

        /**
         * Set the index value stored in this node.
         * @param value the new index value to set
         */
        public void setIndex(int value)
        {
            index = value;
        }

        /**
         * Set the value of this node's next pointer.
         * @param value the node is to point to as the next one in the circle
         */
        public void setNext(Node value)
        {
            next = value;
        }

        /**
         * Set the value of this node's previous pointer.
         * @param value the node is to point to as the previous one in
         * the circle
         */
        public void setPrev(Node value)
        {
            prev = value;
        }
        /**
         * Returns a string representation of this node.
         * The format will look like:
         * [startIndex, endIndex] (size bytes)
         */
        public String toString()
        {
            String st = "[" + index + ", " + ((index+size)-1) + "]";
            st += " ("+ size + " bytes)";
            return st;
        }

    }
}
