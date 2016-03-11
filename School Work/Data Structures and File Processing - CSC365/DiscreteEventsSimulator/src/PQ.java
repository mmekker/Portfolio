
public interface PQ<E extends Comparable<E>>
{
	public void add(E val);
	public E remove();
	public void clear();
}
