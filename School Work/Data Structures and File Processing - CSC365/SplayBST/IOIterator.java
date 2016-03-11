import java.util.Stack;
import java.util.Iterator;

/**
 * IOIterator iterates (inorder) through a SplayBST
 * @author Mike Mekker
 * @param <E> Type that extends comparable<E>
 */
public class IOIterator<E extends Comparable<E>> implements Iterator{
	private Stack<Node<E>> s;
	/**
	 * IOIterator constructor
	 * adds nodes to the stack
	 * @param root start of tree
	 */
	public IOIterator(Node<E> root)
	{
		s = new Stack<Node<E>>();
		Node<E> n = root;
		if(n == null)
		{
			return;
		}
		while(n.left != null)
		{
			s.push(n);
			n = n.left;
		}
		s.push(n);
	}
	/**
	 * Checks if stack is empty
	 */
	public boolean hasNext()
	{
		return !s.isEmpty();
	}
	/**
	 * Gets next value in the stack
	 */
	public E next()
	{
		if(!hasNext())
		{
			return null;
		}
		else
		{
			Node<E> ret = s.pop();
			E i = ret.value;
			if(ret.right != null)
			{
				Node<E> t = ret.right;
				while(t.left != null)
				{
					s.push(t);
					t = t.left;
				}
				s.push(t);
			}
			return (E)i;
		}
	}
}
