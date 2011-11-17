import java.util.ArrayList;
import java.util.List;

/** BST implementation with a key-value pair
 * @param <K> the class type of the key
 * @param <E> the class type of the item being stored
 */

public class BinarySearchTree<K extends Comparable<? super K>, E> {

	//root of our BST
	private BinaryNode<K, E> root;

	//size of our BST
	private int nodecount;

	/**
	 * The constructor instantiates a blank tree
	 */
	BinarySearchTree() {
		root = null;
		nodecount = 0;
	}

	/**
	 * Clear deletes the tree
	 */
	public void clear() {
		root = null;
		nodecount = 0;
	}

	/**
	 * Insert places a record into the tree
	 * @param k the key to associate
	 * @param e the value to insert
	 */
	public void insert(K k, E e) {
		root = inserthelp(root, k, e);
		nodecount++;
	}

	/**
	 * Removes a record given a key
	 * @param k the key
	 * @return E the record removed
	 */
	public E remove(K k) {
	    List<E> result = new ArrayList<E>();
		findhelp(root, k, result);
		if(!result.isEmpty()) {
			root = removehelp(root, k);
			nodecount--;
			return result.get(0);
		}
		return null;
	}

	/**
	 * Finds a record given a key
	 * @param k the key
	 * @return List<E> a list of the records found
	 */
	public List<E> find(K k) {
		List<E> l = new ArrayList<E>();
		findhelp(root, k, l);
		return l;
	}

	/**
	 * Size returns the size of this BST
	 * @return int size of this BST
	 */
	public int size() {
		return nodecount;
	}


	/**
	 * Findhelp finds all nodes equal to key K
	 * @param rt the root node to search from
	 * @param k the key to look for
	 * @param l the List<E> to put results in
	 */
	private void findhelp(BinaryNode<K, E> rt, K k, List<E> l) {
		if(rt == null)
		    return;
		if(rt.key().compareTo(k) > 0)
		    findhelp(rt.left(), k, l);
		else if(rt.key().compareTo(k) < 0)
		    findhelp(rt.right(), k, l);
		else {
			l.add(rt.element());
			if(rt.right() != null) findhelp(rt.right(), k, l);

		}
	}

	/**
	 * insertHelp inserts the key value pair in the correct position
	 * @param rt the root node to start our recursive insertion from
	 * @param k the key matched with element E
	 * @param e the element to insert
	 * @return BSTNode<K,E> the current node after adding in the element
	 */

	private BinaryNode<K, E> inserthelp(BinaryNode<K,E> rt, K k, E e) {
		if(rt == null)
		    return new BinaryNode<K, E>(k, e);
		else if(rt.key().compareTo(k) > 0)
			rt.setLeft(inserthelp(rt.left(), k, e));
		else
			rt.setRight(inserthelp(rt.right(), k, e));

		return rt;

	}
	/**
	 * gets the minimum node in the tree
	 * @param rt where the algorithm begins it recursive search
	 * @return BSTNode<K,E> the minimum node in the tree
	 */

	private BinaryNode<K,E> getmin(BinaryNode<K,E> rt) {

		if(rt.left() == null) return rt;
		return getmin(rt.left());
	}
	/**
	 * removes the minimum node in the tree
	 * @param rt where the algorithm begins it recursive search
	 * @return BSTNode<K,E> the minimum node in the tree removed
	 */


	private BinaryNode<K,E> deletemin(BinaryNode<K, E> rt) {

		if(rt.left() == null) return rt.right();
		rt.setLeft(deletemin(rt.left()));
		return rt;
	}

	/**
	 * remove a node with key value k
	 * @return the tree with the node removed
	 */
	private BinaryNode<K, E> removehelp(BinaryNode<K,E> rt, K k) {
		if(rt == null) return null;
		if(rt.key().compareTo(k) > 0)
			rt.setLeft(removehelp(rt.left(), k));
		else if(rt.key().compareTo(k) < 0)
			rt.setRight(removehelp(rt.right(), k));

		else {
			if(rt.left() == null) return rt.right();
			if(rt.right() == null) return rt.left();

			BinaryNode<K, E> temp = getmin(rt.right());
			rt.setElement(temp.element());
			rt.setKey(temp.key());
			rt.setRight(deletemin(rt.right()));
		}
		return rt;
	}


}
