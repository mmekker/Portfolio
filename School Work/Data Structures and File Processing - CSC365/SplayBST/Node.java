import java.io.Serializable;

/**
 * Node class has right and left subtrees and a value
 * @author Mike Mekker
 * @param <E> Type that extends comparable<E>
 */
public class Node<E extends Comparable<E>> implements Serializable
{
	Node<E> left;
	Node<E> right;
	E value;
	/**
	 * default constructor
	 * @param x element of type E to be entered into value
	 */
	public Node(E x)
	{ 
	    left = null;
	    right = null;
	    value=x;
	}
}