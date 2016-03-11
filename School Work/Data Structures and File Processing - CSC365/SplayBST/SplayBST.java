import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
/**
 * This is a generic Splay Binary Search Tree class
 * @author Mike Mekker
 *
 * @param <E> any Type E that extends Comparable<E>
 */
public class SplayBST<E extends Comparable<E>> implements Serializable, Iterable<E>
{//Start SplayBST Class
	transient private Comparator<E> comp;
	Node<E> root;
	int count;
	/**
	 * SplayBST Constructor with generic comparator
	 */
    public SplayBST()
    {
    	comp = ((new Comparator<E>()
		{ // begin Comparator definition
		    public int compare(E a, E b) {return a.compareTo(b);}
		}));  // end Comparator definition
		root = null;
		count = 0;
    }
    /**
	 * SplayBST Constructor with  comparator parameter
	 * @param Comparator<E> Comparator for type E
	 */
    public SplayBST(Comparator<E> c)
    {
		root = null;
		count = 0;
		comp = c;
    }
    
	/**
	 * Adds elements to the tree and splays them to the root
	 * Assumes no duplicates
	 * @param x an element of type E
	 */
    public void add(E x) 
    {
    	root = splayInsert(root,x); 
    	count++;
    }

    /**
     * Searches tree for a given value and splays that node to the root
     * @param x element of type E being searched for
     * @return the found element or the node that would've been that searched for node's parent
     */
    public Node<E> search(E x) 
    {
    	if(root == null)
    	{
    		return root;
    	}
    	else if (comp.compare(root.value, x) == 0)
    	{
    		return root;
    	}
    	return root = splaySearch(root,x);
    }
    
    
    // There are two cases when both links in a double rotation are oriented
    // in the same direction.  Unlike the root-insertion method, splay 
    // insertion performs the higher rotation first.

    // 4 cases for two levels down in the search path from the root:
    // left-left:  rotate right at root twice
    // left-right: rotate left at left child; rotate right at root
    // right-right: rotate left at root twice
    // right-left: rotate right at right child, rotate left at root

     // There are two cases when both links in a double rotation are oriented
    // in the same direction.  Unlike the root-insertion method, splay 
    // insertion performs the higher rotation first.
    
    /**
     * Inserts a new node of type E with value x to the tree and splays it to the root
     * @param h root of tree
     * @param x Element of type E being inserted
     * @return the new root
     */
    public Node<E> splayInsert(Node<E> h, E x) 
    {
    	if(h == null)
    	{
    		return new Node<E>(x);
    	}
		if (comp.compare(x, h.value)<0) //should it go left??
		{
		    if (h.left == null) //left is empty
		    {
		    	h.left = new Node<E>(x);	//put new node there
		    	h = rotateRight(h);	//rotate right
		    }
		    else if (comp.compare(x,h.left.value)<0) //left left
		    {
		    	h.left.left = splayInsert(h.left.left,x);
		    	h = rotateRight(h);
		    	h = rotateRight(h);
		    }
		    else //(x.compareTo(h.left.value)>0) //left right
		    {
		    	h.left.right = splayInsert(h.left.right,x);
		    	h.left = rotateLeft(h.left);
		    	h = rotateRight(h);
		    }
		    return h;
		}

		else //(x.compareTo(h.value)>0) or go right??
		{
			if (h.right == null) //right is empty
		    {
				h.right = new Node<E>(x);	//put new node there
		    	h = rotateLeft(h);	//rotate left
		    }
			else if (comp.compare(x, h.right.value)>0) 
		    {
		    	h.right.right = splayInsert(h.right.right,x);
		    	h = rotateLeft(h);
		    	h = rotateLeft(h);
		    }
		    else 
		    {
		    	h.right.left = splayInsert(h.right.left,x);
		    	h.right = rotateRight(h.right);
		    	h = rotateLeft(h);
		    }
		    return h;
		}
    }

    /**
     * searches for an element of type E and splays it to the root
     * @param h root of the tree
     * @param x element of type E being searched for
     * @return the new root
     */
    public Node<E> splaySearch(Node<E> h, E x) 
    {
    	if(h == null)
    	{
    		return h;
    	}
    	if(comp.compare(h.value, x) == 0)
    	{
    		return h;
    	}
		if (comp.compare(x, h.value)<0) //should it go left??
		{
		    if (h.left == null) //left is empty
		    {
		    	return h;
		    }
		    else if (comp.compare(h.left.value, x) == 0)
		    {
		    	return rotateRight(h);
		    }
		    else if (comp.compare(x, h.left.value)<0) //left left
		    {
		    	if(h.left.left != null)
		    	{
		    		h.left.left = splaySearch(h.left.left,x);
			    	h = rotateRight(h);
		    	}
		    	h = rotateRight(h);
		    }
		    else //(x.compareTo(h.left.value)>0) //left right
		    {
		    	if(h.left.right != null)
		    	{
		    		h.left.right = splaySearch(h.left.right,x);
			    	h.left = rotateLeft(h.left);
		    	}
		    	h = rotateRight(h);
		    }
		    return h;
		}

		else //(x.compareTo(h.value)>0) or go right??
		{
			if (h.right == null) //right is empty
		    {
				return h;
		    }
		    else if (comp.compare(h.right.value, x) == 0)
		    {
		    	return rotateLeft(h);
		    }
			else if (comp.compare(x, h.right.value)>0) 
		    {
				if(h.right.right != null)
		    	{
					h.right.right = splaySearch(h.right.right,x);
			    	h = rotateLeft(h);
		    	}
		    	h = rotateLeft(h);
		    }
		    else 
		    {
		    	if(h.right.left != null)
		    	{
		    		h.right.left = splaySearch(h.right.left,x);
			    	h.right = rotateRight(h.right);
		    	}
		    	h = rotateLeft(h);
		    }
		    return h;
		}
    }
    
    /**
     * rotates the tree right
     * @param h root of tree
     * @return new root of the tree
     */
    private Node<E> rotateRight(Node<E> h) 
    {
    	Node<E> x = h.left;
    	h.left = x.right;
    	x.right = h;
    	return x;
    }
    /**
     * rotates the tree left
     * @param h root of tree
     * @return new root of the tree
     */
    private Node<E> rotateLeft(Node<E> h) 
    {
    	Node<E> x = h.right;
    	h.right = x.left;
    	x.left = h;
    	return x;
    }
    
    /**
     * Prints out the tree in a text based format
     */
	public void printTree() 
	{
		if(root == null)
		{
	    	System.out.println("NULL");
		}
		else
		{
			this.printBinaryTree(root, 0);
		}
	}
	
    /**
	 * prints elements of the tree in tree form
	 * found online at http://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
	 * @param root
	 * @param level
	 */
	private void printBinaryTree(Node<E> root, int level){
	    if(root==null)
	    {
	    	return;
	    }  
	    printBinaryTree(root.right, level+1);
	    if(level!=0){
	        for(int i=0;i<level-1;i++)
	            System.out.print("|\t");
	            System.out.println("|-------"+root.value);
	    }
	    else
	        System.out.println(root.value);
	    printBinaryTree(root.left, level+1);
	}
	
	/**
	 * Returns an iterator for the given SplayBST
	 */
	public Iterator<E> iterator() {
		return (Iterator<E>)new IOIterator<E>(root);
	}
    
//End SplayBST Class


}