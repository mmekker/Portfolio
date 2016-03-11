import java.util.ArrayList;
import java.util.Comparator;
/** This is a Heap data structure of generic type E
 * 
 * @author Mike Mekker
 *
 * @param <E>	E is some comparable object
 * field: heap: ArrayList that hold the data
 * field: comp: comparator for type E
 */
public class CompleteHeap<E extends Comparable<E>> implements PQ<E>
{
	private ArrayList<E> heap;
	private Comparator<E> comp;
	
	public CompleteHeap()
	{
		heap = new ArrayList<E>();
		comp = ((new Comparator<E>()
	    	{public int compare(E a, E b) {return a.compareTo(b);}
	         public boolean equals(Object obj, Object obj2) {return obj == obj2;}}));
	}
	public CompleteHeap(Comparator<E> c)
	{
		heap = new ArrayList<E>();
		comp = c;
	}
	public boolean isEmpty()
	{
		return heap.size() == 0;
	}
	
	/**
	 * Adds new value.
	 * Then moves value accordingly.
	 */
	public void add(E val)
	{
		heap.add(val); //adds to last spot in arraylist
		check(heap.size()-1); //fixes new values spot
	}
	/**
	 * Removes the first value of the heap
	 */
	public E remove()
	{
		E ret = heap.get(0);
		heap.remove(0);
		return ret;
	}
	
	/**
	 * Moves the value at the index to its proper spot
	 * @param index
	 */
	private void check(int index)
	{
		printHeap();
		while(index>0)
		{
			if(heap.get(index).compareTo(heap.get(index-1)) < 0)
			{
				E temp = heap.get(index-1);
				heap.set(index-1, heap.get(index));
				heap.set(index, temp);
				printHeap();
				index--;
			}
			else
			{
				break;
			}
		}
	}
	
	/**
	 * Prints the heap in the format:
	 * {E, E, E, E, }
	 */
	private void printHeap()
	{
		System.out.print("{");
		for(E a: heap)
		{
			System.out.print(a + ", ");
		}
		System.out.print("}\n");
	}
	
	/**
	 * adds 20 numbers then removes all of them
	 * @param args
	 */
	public static void main(String[] args)
	{
		CompleteHeap<Integer> c = new CompleteHeap<Integer>();
		for(int x = 0; x < 20; x++)
		{
			int rand = (int)(Math.random()*2);
			System.out.println("Adding " + rand);
			c.add(rand);
		}
		System.out.println("");
		while(!c.isEmpty())
		{
			System.out.println("Removing");
			c.remove();
			c.printHeap();
		}
	}

	public void clear()
	{
		heap = new ArrayList<E>();
	}
	public int size()
	{
		return heap.size();
	}
	public E poll()
	{
		E e = heap.get(0);
		heap.remove(e);
		return e;
	}
	public E get(int i)
	{
		return heap.get(i);
	}
}
