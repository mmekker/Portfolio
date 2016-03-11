package bstree;
import java.io.Serializable;
import java.lang.Comparable;
import java.util.Comparator;
import java.util.Iterator;

/** This class is a Binary Search Tree that stores generic data
 * 
 * @author Mike Mekker
 *
 */
public class bst<E extends Comparable<E>> implements Serializable, Iterable<E>
{
	transient private Comparator<E> comp;
	bst<E> leftTree;
	bst<E> rightTree;
	int x;
	int y;
	E data;
	
	
	public static int index;
	
	/**
	 * BST Constructor without comparator param
	 */
	public bst()
	{
		comp = ((new Comparator<E>()
			      { // begin Comparator definition
			         public int compare(E a, E b) {return a.compareTo(b);}
			         public boolean equals(Object obj, Object obj2) {return obj == obj2;}
			      } // end Comparator definition
			      )); 
		data = null;
		leftTree = null;
		rightTree = null;
	}
	/**
	 * Constructor with comparator param
	 * @param c - custom comparator
	 */
	public bst(Comparator<E> c)
	{
		comp = c;
		data = null;
		leftTree = null;
		rightTree = null;
	}
	
	/**
	 * @return if tree is empty
	 */
	public boolean isEmpty()
	{
		return(data==null);
	}
	/**
	 * @return size of tree
	 */
	public int size()
	{
		if(data != null)
		{
			int num = 1;
			if(leftTree!=null)
			{
				num+=leftTree.size();
			}
			if(rightTree!=null)
			{
				num+=rightTree.size();
			}
			return num;
		}
		return 0;
	}
	/**
	 * adds new data to tree
	 * @param newD - new data
	 * @param c - comparator for  new node
	 */
	public void add(E newD, Comparator c)
	{
		if(this.data == null)
		{
			this.data = newD;
		}
		else if(comp.compare(data,newD)>0)
		{
			if(leftTree == null)
			{
				leftTree = new bst<E>(c);
				leftTree.data = newD;
			}
			else
			{
				leftTree.add(newD,c);
			}
		}
		else if(comp.compare(data,newD)<0)
		{
			if(rightTree == null)
			{
				rightTree = new bst<E>(c);
				rightTree.data = newD;
			}
			else
			{
				rightTree.add(newD,c);
			}
		}
	}
	/**
	 * Finds the location of a specific element
	 * @param d - data target
	 * @return refence to the node
	 */
	public bst<E> locate(E d)
	{
		if(this.data == d)
		{
			return this;
		}
		else if(comp.compare(d,data) < 0)
		{
			if(leftTree != null)
			{
				return leftTree.locate(d);
			}
			else
			{
				return this;
			}
		}
		else if(comp.compare(d,data) > 0)
		{
			if(rightTree != null)
			{
				return rightTree.locate(d);
			}
			else
			{
				return this;
			}
		}
		return null;
	}
	
	/**
	 * Finds the next element to take place of removed node
	 * called in remove method
	 * @return next node
	 */
	public bst<E> findNext()
	{
		if(leftTree.leftTree != null)
		{
			return leftTree.findNext();
		}
		else
		{
			bst<E> temp = leftTree;
			this.leftTree = leftTree.rightTree;
			return temp;
		}
	}
	/**
	 * Removes target node
	 * @param target - node being removed
	 */
	public void remove(E target)
	{
		if(comp.compare(target,data) == 0)
		{
			//case 1: only one child
			if(leftTree == null && rightTree != null)
			{
				bst<E> temp = rightTree.rightTree;
				this.data = temp.data;
				this.leftTree = temp.leftTree;
				this.rightTree = temp.rightTree;
			}
			else if(leftTree != null && rightTree == null)
			{
				bst<E> temp = leftTree;
				this.data = temp.data;
				this.leftTree = temp.leftTree;
				this.rightTree = temp.rightTree;
			}
			//case 2: 2 children
			//replace with smallest node in the right subtree
			else if(rightTree != null &&  leftTree != null)
			{
				if(rightTree.leftTree != null)
				{
					this.data = rightTree.findNext().data;
				}
				else
				{
					this.data = rightTree.data;
					this.rightTree = rightTree.rightTree;
				}
			}
			
		}
		else if(comp.compare(target,data) < 0)
		{
			if(leftTree != null)
			{
				leftTree.remove(target);
			}
			return;
		}
		else if(comp.compare(target,data) > 0)
		{
			if(rightTree != null)
			{
				rightTree.remove(target);
			}
			return;
		}
	}
	
	/**
	 * Redo's the coordiantes for all nodes in the tree
	 * (inorder Transversal index, level)
	 */
	public void redoCoords() 
	{
		index = 1;
		changeX();
		index = 1;
		changeY(20);
	}
	/**
	 * Recursively changes all x coordinates
	 */
	public void changeX()
	{
		if(leftTree != null)
		{
			leftTree.changeX();
		}
		x = index;
		index+=20;
		if(rightTree != null)
		{
			rightTree.changeX();
		}
	}
	/**
	 * Recursively changes all y coordinates
	 */
	public void  changeY(int L)
	{
		y = L;
		if(leftTree != null)
		{
			leftTree.changeY(L+40);
		}
		if(rightTree != null)
		{
			rightTree.changeY(L+40);
		}
	}
	/**
	 * Reflects the tree by recursively switching all left and right trees
	 */
	public void reflectCoords()
	{
		bst<E> temp = leftTree;
		leftTree = rightTree;
		rightTree = temp;
		if(leftTree != null)
		{
			leftTree.reflectCoords();
		}
		if(rightTree != null)
		{
			rightTree.reflectCoords();
		}
	}
	
	
	/**
	 * Prints all elements of the tree in order
	 */
	public void printTree()
	{
		if(leftTree != null)
		{
			leftTree.printTree();
		}
		System.out.println(" " + data + " " + x + " " + y);
		if(rightTree != null)
		{
			rightTree.printTree();
		}
	}
	/**
	 * prints elements of the tree in tree form
	 * found online at http://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
	 * @param root
	 * @param level
	 */
	public void printBinaryTree(bst root, int level){
	    if(root==null)
	         return;
	    printBinaryTree(root.rightTree, level+1);
	    if(level!=0){
	        for(int i=0;i<level-1;i++)
	            System.out.print("|\t");
	            System.out.println("|-------"+root.data);
	    }
	    else
	        System.out.println(root.data);
	    printBinaryTree(root.leftTree, level+1);
	}
	
	/**
	 * Used to test bst
	 * @param args
	 */
	public static void main(String[] args) {
		//initialization of bst
		bst<Integer> b = new bst<Integer>();
		//add 15 numbers
		//size won't always be 15 because there can be duplicates
		b.add(25, b.comp);
		for(int x = 0; x < 15; x++)
		{
			int tnum = (int)(Math.random()*100);
			System.out.println("Adding - " + tnum);
			b.add(tnum,b.comp);
		}
		System.out.println("\n\n");
		
		//print tree
		b.printBinaryTree(b,0);
		System.out.println("\n");
		
		//remove 25
		b.remove(25);
		
		//reprint tree
		b.printBinaryTree(b,0);
		System.out.println("\n");
		
		//print tree in order
		b.printTree();
		System.out.println("\n");
		b.redoCoords();
		b.printTree();
		System.out.println("\n");
		
		//size
		System.out.println("Size = " + b.size());
	}
	/**
	 * @return comparator
	 */
	public Comparator<E> getComp() 
	{
		return comp;
	}
	/**
	 * sets the compartator to the default comp for when the  tree is restored from file
	 */
	public void setComp() 
	{
		comp = ((new Comparator<E>()
			      { // begin Comparator definition
	         public int compare(E a, E b) {return a.compareTo(b);}
	         public boolean equals(Object obj, Object obj2) {return obj == obj2;}
	      } // end Comparator definition
	      ));
		if(leftTree != null)
		{
			leftTree.setComp();
		}
		if(rightTree != null)
		{
			rightTree.setComp();
		}
	}
	
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	
	

}
