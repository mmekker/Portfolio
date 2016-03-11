import java.nio.ByteBuffer;

public class PBTreeNode {
	public String[] keys;
	public int[] links;
	private int order;
	private CMS c;
	boolean save;
	
	public PBTreeNode(int n, CMS cc)
	{
		keys = new String[n-1];
		links = new int[n];
		for(int x = 0; x < links.length; x++)
		{
			links[x] = 0;
		}
		order = n;
		c = cc;
		save = false;
	}
	
	public boolean isLeaf()
	{
		return links[0] == 0;
	}
	
	public boolean isFull()
	{
		return keys[order-2] != null;
	}
	
	public static byte[] intToByteArray(int value)
	{
	    return new byte[] {
	            (byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	}
	public static int byteArrayToInt(byte[] b)
	{
		ByteBuffer bb = ByteBuffer.wrap(b);
		return bb.getInt();
	}
	public void writeNode(PBTreeNode n, int linkNum)//node to byte[]
	{
		byte[] b = new byte[256];
		for(int x = 0; x < n.keys.length; x++)//loop through keys
		{
			byte[] keyb;
			if(n.keys[x] != null)
			{
				keyb = n.keys[x].getBytes();
			}
			else
			{
				keyb = new byte[32];
			}
			for(int y = 0; y < 32; y++)//add each character
			{
				
				if(y < keyb.length)
				{
					b[(32*x)+y] = keyb[y];
				}
				else
				{
					b[(32*x)+y] = 0;
				}
			}
		}
		for(int x = 0; x < n.links.length; x++)
		{
			byte[] num = intToByteArray(n.links[x]);
			for(int y = 0; y < 4; y++)
			{
				b[224+(4*x)+y] = num[y];//where links start + link number + 0-3 index of num byte[]
			}
		}
		c.write(linkNum, b);
	}
	
	public PBTreeNode getLink(int linkNum)
	{
		PBTreeNode node = new PBTreeNode(order, c);
		byte[] b = new byte[256];
		c.read(links[linkNum], b);
		//get keys
		String tempHold = "";
		for(int x = 0; x < order-1; x++)
		{
			if(b[x*32] != 0)
			{
				for(int y = 0; y < 32; y++)
				{
					if(b[(x*32)+y] != 0)
					{
						tempHold += (char)b[(x*32)+y];
					}
					else
					{
						break;
					}
				}
				node.keys[x] = tempHold;
				tempHold = "";
			}
		}
		//get links
		byte[] ints = new byte[4];
		ByteBuffer bb;
		for(int x = 0; x < order; x++)
		{
			//get byte[] to be int
			for(int y = 0; y < 4; y++)
			{
				ints[y] = b[224+(4*x)+y];
			}
			bb = ByteBuffer.wrap(ints);
			node.links[x] = bb.getInt();
		}
		return node;
	}
	
	public boolean splitRoot(String key)
	{
		//Create new left and right nodes
		PBTreeNode l = new PBTreeNode(order, c);
		PBTreeNode r = new PBTreeNode(order, c);
		//Switch keys to new nodes
		for(int x = 0; x < (order-1)/2; x++)
		{
			l.keys[x] = this.keys[x];
			r.keys[x] = this.keys[x+(order/2)];
		}
		//Switch links
		for(int x = 0; x < order/2; x++)
		{
			l.links[x] = this.links[x];
			r.links[x] = this.links[x+(order/2)];
		}
		//Fix root keys
		keys[0] = keys[(order-1)/2];
		for(int x = 1; x < keys.length; x++)
		{
			keys[x] = null;
		}
		//Fix root links
		int lnum = c.getNextIndex();
		int rnum = c.getNextIndex();
		links[0] = lnum;
		links[1] = rnum;
		writeNode(l, lnum);
		writeNode(r, rnum);
		for(int x = 2; x < links.length; x++)
		{
			links[x] = 0;
		}
		save = true;
		return insert(key);
	}
	
	public boolean insert(String key)
	{
		//Search for link to check next
		for(int x = 0; x < keys.length; x++)
		{
        	if(key.equals("Dan"))
        	{
        		System.out.print("");
        	}
			if(keys[x] == null)
			{
				if(links[x] == 0)
				{
					save = true;
					keys[x] = key;
					return true;
				}
				else
				{
					PBTreeNode linkx = getLink(x);
					if(linkx.isFull() == false)
					{
						boolean ret = linkx.insert(key);//save success
						if(linkx.save)//if node changed
						{
							linkx.save = false;//reset save
							writeNode(linkx, links[x]);//re-write it
						}
						return ret;//return success
					}
					else//if the node is full
					{
						splitFullNode(linkx);//save the two links
						PBTreeNode linkx1 = getLink(x+1);
						save = true;
						return insert(key);
					}
				}
			}
			else if(key.compareTo(keys[x]) == 0)
			{
				return false;
			}
			else if(key.compareTo(keys[x]) < 0)
			{
				if(links[x] != 0)
				{
					PBTreeNode linkx = getLink(x);
					if(linkx.isFull() == false)
					{
						boolean ret = linkx.insert(key);//save success
						if(linkx.save)//if node changed
						{
							linkx.save = false;//reset save
							writeNode(linkx, links[x]);//re-write it
						}
						return ret;//return success
					}
					else
					{
						splitFullNode(linkx);
						save = true;
						return insert(key);
					}
				}
				else//add to this node
				{
					for(int i = 0; i >= keys.length; i++)
					{
						if(key.compareTo(keys[i]) == 0)
						{
							return false;
						}
					}
					for(int i = keys.length-2; i >= x; i--)
					{
						keys[i+1] = keys[i];
					}
					keys[x] = key;
					save = true;
					return true;
				}
			}
			else if(x == order-2)
			{
				PBTreeNode linkx1 = getLink(x+1);
				boolean ret = linkx1.insert(key);//save success
				if(linkx1.save)//if node changed
				{
					linkx1.save = false;//reset save
					writeNode(linkx1, links[x+1]);//re-write it
				}
				return ret;//return success
			}
		}//end for loop to search for insert spot
		return true;
	}//end insert
	
	public void splitFullNode(PBTreeNode fNode)
	{
		int midIndex = 0;
		String mid = fNode.keys[(order-1)/2];
		for(int x = 0; x < keys.length; x++)
		{
			if(keys[x] == null)
			{
				keys[x] = mid;
				midIndex = x;
				break;
			}
			else if(mid.compareTo(keys[x]) < 0)
			{//slide all the values over
				for(int i = keys.length-1; i > x; i--)
				{
					keys[i] = keys[i-1];
					links[i+1] = links[i];
				}
				keys[x] = mid;
				midIndex = x;
				break;
			}
		}
		PBTreeNode right = new PBTreeNode(order, c);//make new right node
		for(int x = 0; x < (order/2)-1; x++)
		{
			right.keys[x] = fNode.keys[x+(order/2)];
			right.links[x] = fNode.links[x+(order/2)];
		}
		right.links[order/2-1] = fNode.links[order-1];
		int rnum = c.getNextIndex();
		links[midIndex+1] = rnum;
		writeNode(right, rnum);
		for(int x = (order/2)-1; x < order-1; x++)
		{
			fNode.keys[x] = null;
			fNode.links[x+1] = 0;
		}
		writeNode(fNode, links[midIndex]);
	}
	
	public boolean check()
	{
		if(checkLinks())
		{
			return true;
		}
		for(int x = 0; x < links.length; x++)
		{
			if(links[x] != 0)
			{
				if(getLink(x).check())
				{
					return true;
				}
			}
		}
		return false;
	}
	public boolean check2()
	{
		if(checkKeys())
		{
			return true;
		}
		for(int x = 0; x < links.length; x++)
		{
			if(links[x] != 0)
			{
				if(getLink(x).check2())
				{
					return true;
				}
			}
		}
		return false;
	}
	public boolean checkKeys()
	{
		PBTreeNode linkx10 = getLink(0);
		PBTreeNode linkx11 = getLink(1);
		PBTreeNode linkx12 = getLink(2);
		PBTreeNode linkx13 = getLink(3);
		PBTreeNode linkx14 = getLink(4);
		PBTreeNode linkx15 = getLink(5);
		PBTreeNode linkx16 = getLink(6);
		PBTreeNode linkx17 = getLink(7);
		for(int x = 0; x < keys.length; x++)
		{
			if(x < keys.length -1)
			{
				if(keys[x] == null || keys[x+1] == null)
				{
					break;
				}
				if(keys[x].compareTo(keys[x+1]) > 0)
				{
					return true;
				}
				if(links[x] != 0)
				{
					if(keys[x].compareTo(getLink(x).gethighest()) < 0)
					{
						return true;
					}
				}
				if(links[x+1] != 0)
				{
					if(keys[x].compareTo(getLink(x+1).keys[0]) > 0)
					{
						return true;
					}
				}
			}
			else
			{
				if(links[x] != 0)
				{
					if(keys[x].compareTo(getLink(x).gethighest()) < 0)
					{
						return true;
					}
				}
				if(links[x+1] != 0)
				{
					if(keys[x].compareTo(getLink(x+1).keys[0]) > 0)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	public String gethighest()
	{
		String highest = "";
		for(int x = keys.length-1; x >= 0; x--)
		{
			if(keys[x] != null)
			{
				highest = keys[x];
				break;
			}
		}
		return highest;
	}
	public boolean checkLinks()
	{
		boolean test = false;
		for(int x = 0; x < links.length; x++)
		{
			if(links[x] == 0)
			{
				test = true;
			}
			if(test == true && links[x] != 0)
			{
				return true;
			}
		}
		return false;
	}
	
	
	

	public boolean remove(String key)
	{
		//node is minimal if it has 3 values
		//Check if value is in current node
		for(int x = 0; x < keys.length; x++)
		{
			if(x == order-2 && keys[x] != null && key.compareTo(keys[x]) > 0 && !isLeaf())
			{
				if(!getLink(x+1).isMinimal())
				{
					PBTreeNode linkx1 = getLink(x+1);
					boolean ret = linkx1.remove(key);//save success
					if(linkx1.save)//if node changed
					{
						linkx1.save = false;//reset save
						writeNode(linkx1, links[x+1]);//re-write it
					}
					return ret;//return success
				}
				else if(links[x] != 0 && !getLink(x).isMinimal())//if it is minimal and has a non-minimal neighbor to the left
				{
					stealLeft(x,x,x+1);//from, middle, left
					//delete value from node
					PBTreeNode linkx1 = getLink(x+1);
					boolean ret = linkx1.remove(key);//save success
					if(linkx1.save)//if node changed
					{
						linkx1.save = false;//reset save
						writeNode(linkx1, links[x+1]);//re-write it
					}
					return ret;//return success
				}
				else
				{
					merge(x,x+1);
					//delete from new node
					return remove(key);
				}
			}
			if(keys[x] == null && !isLeaf())
			{
				if(!getLink(x).isMinimal())
				{
					PBTreeNode linkx = getLink(x);
					boolean ret = linkx.remove(key);//save success
					if(linkx.save)//if node changed
					{
						linkx.save = false;//reset save
						writeNode(linkx, links[x]);//re-write it
					}
					return ret;//return success
				}
				else if(x < (order-1) && links[x+1] != 0 && !getLink(x+1).isMinimal())//if it is minimal and has a non-minimal neighbor to the right
				{
					stealRight(x,x,x+1);
					//delete value from node
					PBTreeNode linkx = getLink(x);
					boolean ret = linkx.remove(key);//save success
					if(linkx.save)//if node changed
					{
						linkx.save = false;//reset save
						writeNode(linkx, links[x]);//re-write it
					}
					return ret;//return success
				}
				else if(x > 0 && links[x-1] != 0 && !getLink(x-1).isMinimal())//if it is minimal and has a non-minimal neighbor to the left
				{
					stealLeft(x-1,x-1,x);//from, middle, left
					//delete value from node
					PBTreeNode linkx = getLink(x);
					boolean ret = linkx.remove(key);//save success
					if(linkx.save)//if node changed
					{
						linkx.save = false;//reset save
						writeNode(linkx, links[x]);//re-write it
					}
					return ret;//return success
				}
				else//if it is minimal and has only minimal neighbors
				{
					if(links[x+1] != 0)
					{
						merge(x,x+1);
						//delete from new node
						return remove(key);
					}
					else
					{
						merge(x-1,x);
						//delete from new node
						return remove(key);
					}
				}
			}
			else if(keys[x] == null && isLeaf())
			{
				//System.out.println("Tree cannot remove " + key + " because it is not in the tree.");
				return false;
			}
			else if(key.compareTo(keys[x]) == 0)
			{
				if(isLeaf())//if node is a leaf ==> delete it + shift left		
				{
					keys[x] = null;
					if(x != order-2)
					{
						for(int i = x; i < keys.length-1; i++)
						{
							keys[i] = keys[i+1];
							links[i+1] = links[i+2];
						}
						keys[order-2] = null;
						links[order-1] = 0;
						save = true;
						return true;
					}
				}
				else//find predecessor replace current with predecessor ==> delete predecessor
				{
					String pre = getLink(x).findPre();
					keys[x] = pre;
					if(!getLink(x).isMinimal())
					{
						PBTreeNode linkx = getLink(x);
						boolean ret = linkx.remove(pre);//save success
						if(linkx.save)//if node changed
						{
							linkx.save = false;//reset save
							writeNode(linkx, links[x]);//re-write it
						}
						return ret;//return success
					}
					else if(x+1 <= (order-1) && !getLink(x+1).isMinimal())
					{
						stealRight(x,x,x+1);
						PBTreeNode linkx = getLink(x);
						boolean ret = linkx.remove(pre);//save success
						if(linkx.save)//if node changed
						{
							linkx.save = false;//reset save
							writeNode(linkx, links[x]);//re-write it
						}
						return ret;//return success
					}
					else if(x-1 >= 0 && !getLink(x-1).isMinimal())
					{
						stealLeft(x-1,x-1,x);//from, middle, left
						PBTreeNode linkx = getLink(x);
						boolean ret = linkx.remove(pre);//save success
						if(linkx.save)//if node changed
						{
							linkx.save = false;//reset save
							writeNode(linkx, links[x]);//re-write it
						}
						return ret;//return success
					}
					else
					{
						merge(x,x+1);
						PBTreeNode linkx = getLink(x);
						boolean ret = linkx.remove(pre);//save success
						if(linkx.save)//if node changed
						{
							linkx.save = false;//reset save
							writeNode(linkx, links[x]);//re-write it
						}
						return ret;//return success
					}
				}
			}
			else if(key.compareTo(keys[x]) < 0 && !isLeaf())//else find next node to go into
			{
				if(!getLink(x).isMinimal())//check if next node is non-minimal
				{
					PBTreeNode linkx = getLink(x);
					boolean ret = linkx.remove(key);//save success
					if(linkx.save)//if node changed
					{
						linkx.save = false;//reset save
						writeNode(linkx, links[x]);//re-write it
					}
					return ret;//return success
				}
				else if(x+1 <= (order-1) && !getLink(x+1).isMinimal())//if it is minimal and has a non-minimal neighbor to the right
				{
					stealRight(x,x,x+1);
					//delete value from node
					return remove(key);
				}
				else if(x-1 >= 0 && !getLink(x-1).isMinimal())//if it is minimal and has a non-minimal neighbor to the left
				{
					stealLeft(x-1,x-1,x);//from, middle, left
					//delete value from node
					return remove(key);
				}
				else//if it is minimal and has only minimal neighbors
				{
					merge(x,x+1);
					//delete from new node
					return remove(key);
				}
			}
		}
		return true;
	}//end remove
	
	public void stealLeft(int from, int mid, int to)
	{
		PBTreeNode f = getLink(from);
		PBTreeNode t = getLink(to);
		//move middle value from parent to minimal child
		for(int x = (order/2)-1; x > 0; x--)
		{
			t.keys[x] = t.keys[x-1];
		}
		t.keys[0] = keys[mid];
		for(int x = (order/2); x > 0; x--)
		{
			t.links[x] = t.links[x-1];
		}
		//move end value of non-minimal child to parent
		//transfer link from non-minimal to minimal
		for(int i = f.keys.length-1; i >= 0; i--)
		{
			if(f.keys[i] != null)
			{
				keys[mid] = f.keys[i];
				f.keys[i] = null;
				t.links[0] = f.links[i+1];
				f.links[i+1] = 0;
				break;
			}
		}
		save = true;
		writeNode(f, links[from]);
		writeNode(t, links[to]);
	}
	public void stealRight(int to, int mid, int from)
	{
		PBTreeNode f = getLink(from);
		PBTreeNode t = getLink(to);
		//move middle value from parent to minimal child
		t.keys[(order/2)-1] = keys[mid];
		//move end value of non-minimal child to parent
		keys[mid] = f.keys[0];
		//move link from non-minimal to minimal
		t.links[order/2] = f.links[0];
		//shift non-minimal to the left
		for(int i = 0; i < f.keys.length-1; i++)
		{
			f.keys[i] = f.keys[i+1];
			f.links[i] = f.links[i+1];
		}
		f.links[order-2] = f.links[order-1];
		f.keys[order-2] = null;
		f.links[order-1] = 0;
		save = true;
		writeNode(f, links[from]);
		writeNode(t, links[to]);
	}
	public void merge(int left, int right)
	{
		//create new node with 2 children and middle value from parent
		PBTreeNode n = new PBTreeNode(order, c);
		for(int x = 0; x < (order/2)-1; x++)
		{
			n.keys[x] = getLink(left).keys[x];
		}
		n.keys[3] = keys[left];
		for(int x = 0; x < (order/2)-1; x++)
		{
			n.keys[x+(order/2)] = getLink(right).keys[x];
		}
		
		for(int x = 0; x < (order/2); x++)
		{
			n.links[x] = getLink(left).links[x];
		}
		for(int x = 0; x < (order/2); x++)
		{
			n.links[x+(order/2)] = getLink(right).links[x];
		}
		c.realloc(links[right]);
		if(left < order-2)
		{
			for(int i = left; i <= keys.length-2; i++)
			{
				keys[i] = keys[i+1];
				links[i+1] = links[i+2];
			}
			keys[order-2] = null;
			links[order-1] = 0;
			if(keys[0] == null)
			{
				keys = n.keys;
				links = n.links;
			}
			else
			{
				int nnum = c.getNextIndex();
				links[left] = nnum;
				writeNode(n, nnum);
			}
		}
		else
		{
			int nnum = c.getNextIndex();
			links[left] = nnum;
			writeNode(n, nnum);
			keys[order-2] = null;
			links[order-1] = 0;
		}
		save = true;
	}
	
	public String search(String key)
	{
		for(int x = 0; x < keys.length; x++)
		{
			if(keys[x] == null)
			{
				if(isLeaf())
				{
					return null;
				}
				else
				{
					return getLink(x).search(key);
				}
			}
			else if(key.compareTo(keys[x]) == 0)
			{
				return key;
			}
			else if(key.compareTo(keys[x]) < 0)
			{
				if(isLeaf())
				{
					return null;
				}
				else
				{
					return getLink(x).search(key);
				}
			}
		}
		return null;
	}

	private String findPre()
	{
		int i = 0;
		for(int x =keys.length-1; x >= 0; x--)
		{
			if(keys[x] != null)
			{
				i = x;
				break;
			}
		}
		if(isLeaf())
		{
			return keys[i];
		}
		else
		{
			
			return getLink(i+1).findPre();
		}
	}
	private boolean isMinimal()
	{
		int i = (order/2)-1;
		return (keys[i] == null);
	}
}//end class








