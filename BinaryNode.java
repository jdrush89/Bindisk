
/**
 * // -------------------------------------------------------------------------
/**
 *  A class representing a Binary Search Tree node.  The node stores one element
 *  along with a key value.  each node has a left and right pointer to child nodes.
 *  @param <K> the class of the key value
 *  @param <E> the class of the element
 *
 *  @author Joshua Rush
 *  @author Benjamin Roble
 *  @version Oct 9, 2011
 */
public class BinaryNode<K,E>{
    // the key for this node
	private K key;

	// the element for this node
	private E element;

	// the left child node
	private BinaryNode<K,E> left;

	// the right child node
	private BinaryNode<K,E> right;

	/**
	 * The default constructor simply instantiates the children to null
	 */
	public BinaryNode() {
		left = right = null;
	}

	/**
	 * Create a new BSTNode object.
	 * @param k the key
	 * @param val the value
	 */
	public BinaryNode(K k, E val) {
		left = right = null;
		key = k;
		element = val;
	}

	/**
     * Create a new BSTNode object.
     * @param k the key
     * @param val the value
     * @param l the left child
     * @param r the right child
     */
	public BinaryNode (K k, E val, BinaryNode<K,E> l, BinaryNode<K,E> r) {
		key = k;
		element = val;
		left = l;
		right = r;
	}

	/**
	 * gets this Node's key
	 * @return K the key
	 */
	public K key() {
		return key;
	}

	/**
     * sets this Node's key
     * @param keyValue the key to set
     * @return K the key that was set
     */
	public K setKey(K keyValue) {
		return key = keyValue;
	}

	/**
     * gets this Node's element
     * @return E the element
     */
	public E element() {
		return element;
	}

	/**
     * sets this Node's element
     * @param v the element to set
     * @return E the element
     */
	public E setElement(E v) {
		return element = v;
	}

	/**
     * gets this Node's left child
     * @return BSTNode<K,E> the left child
     */
	public BinaryNode<K,E> left() {
		return left;
	}

	/**
     * sets this Node's left child
     * @param p the child to set
     * @return BSTNode<K,E> the child set
     */
	public BinaryNode<K,E> setLeft(BinaryNode<K,E> p) {
		return left = p;
	}

	/**
     * gets this Node's right child
     * @return BSTNode<K,E> the right child
     */
	public BinaryNode<K,E> right() {
		return right;
	}

	/**
     * sets this Node's right child
     * @param p the child to set
     * @return BSTNode<K,E> the child set
     */
	public BinaryNode<K,E> setRight(BinaryNode<K,E> p) {
		return right = p;
	}

	/**
	 * returns whether the node is a leaf or not
	 * @return boolean true if the Node is a leaf
	 */
	public boolean isLeaf() {
		return (left == null && right == null);
	}
}
