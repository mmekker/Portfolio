public class BTreeNode {
	public String[] keys;
	public BTreeNode[] links;
	private int order;
	
	public BTreeNode(int n)
	{
		keys = new String[n-1];
		links = new BTreeNode[n];
		order = n;
	}
	
	public boolean isLeaf()
	{
		return links[0] == null;
	}
	
	public boolean isFull()
	{
		return keys[order-2] != null;
	}
	
	public boolean splitRoot(String key)
	{
		//Create new left and right nodes
		BTreeNode l = new BTreeNode(order);
		BTreeNode r = new BTreeNode(order);
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
		links[0] = l;
		links[1] = r;
		for(int x = 2; x < links.length; x++)
		{
			links[x] = null;
		}
		return insert(key);
	}
	
	public boolean insert(String key)
	{
		//Search for link to check next
		for(int x = 0; x < keys.length; x++)
		{
			if(keys[x] == null)
			{
				if(links[x] == null)
				{
					keys[x] = key;
					return true;
				}
				else
				{
					if(links[x].isFull() == false)
					{
						return links[x].insert(key);
					}
					else//if the node is full
					{
						splitFullNode(links[x]);
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
				if(links[x] != null)
				{
					if(links[x].isFull() == false)
					{
						return links[x].insert(key);
					}
					else
					{
						splitFullNode(links[x]);
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
					return true;
				}
			}
			else if(x == order-2)
			{
				return links[x+1].insert(key);
			}
		}//end for loop to search for insert spot
		return true;
	}//end insert
	
	public void splitFullNode(BTreeNode fNode)
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
		BTreeNode right = new BTreeNode(order);//make new right node
		for(int x = 0; x < (order/2)-1; x++)
		{
			right.keys[x] = fNode.keys[x+(order/2)];
			right.links[x] = fNode.links[x+(order/2)];
		}
		right.links[order/2-1] = fNode.links[order-1];
		links[midIndex+1] = right;
		for(int x = (order/2)-1; x < order-1; x++)
		{
			links[midIndex].keys[x] = null;
			links[midIndex].links[x+1] = null;
		}
	}
	
	public boolean check()
	{
		if(checkLinks())
		{
			return true;
		}
		for(int x = 0; x < links.length; x++)
		{
			if(links[x] != null)
			{
				if(links[x].check())
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
			if(links[x] != null)
			{
				if(links[x].check2())
				{
					return true;
				}
			}
		}
		return false;
	}
	public boolean checkKeys()
	{
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
				if(links[x] != null)
				{
					if(keys[x].compareTo(links[x].gethighest()) < 0)
					{
						return true;
					}
				}
				if(links[x+1] != null)
				{
					if(keys[x].compareTo(links[x+1].keys[0]) > 0)
					{
						return true;
					}
				}
			}
			else
			{
				if(links[x] != null)
				{
					if(keys[x].compareTo(links[x].gethighest()) < 0)
					{
						return true;
					}
				}
				if(links[x+1] != null)
				{
					if(keys[x].compareTo(links[x+1].keys[0]) > 0)
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
			if(links[x] == null)
			{
				test = true;
			}
			if(test == true && links[x] != null)
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
				if(!links[x+1].isMinimal())
				{
					return links[x+1].remove(key);
				}
				else if(links[x] != null && !links[x].isMinimal())//if it is minimal and has a non-minimal neighbor to the left
				{
					stealLeft(x,x,x+1);//from, middle, left
					//delete value from node
					return links[x+1].remove(key);
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
				if(!links[x].isMinimal())
				{
					return links[x].remove(key);
				}
				else if(x < (order-1) && links[x+1] != null && !links[x+1].isMinimal())//if it is minimal and has a non-minimal neighbor to the right
				{
					stealRight(x,x,x+1);
					//delete value from node
					return links[x].remove(key);
				}
				else if(x > 0 && links[x-1] != null && !links[x-1].isMinimal())//if it is minimal and has a non-minimal neighbor to the left
				{
					stealLeft(x-1,x-1,x);//from, middle, left
					//delete value from node
					return links[x].remove(key);
				}
				else//if it is minimal and has only minimal neighbors
				{
					if(links[x+1] != null)
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
						links[order-1] = null;
						return true;
					}
				}
				else//find predecessor replace current with predecessor ==> delete predecessor
				{
					String pre = links[x].findPre();
					keys[x] = pre;
					if(!links[x].isMinimal())
					{
						links[x].remove(pre);
					}
					else if(x+1 <= (order-1) && !links[x+1].isMinimal())
					{
						stealRight(x,x,x+1);
						links[x].remove(pre);
					}
					else if(x-1 >= 0 && !links[x-1].isMinimal())
					{
						stealLeft(x-1,x-1,x);//from, middle, left
						links[x].remove(pre);
					}
					else
					{
						merge(x,x+1);
						links[x].remove(pre);
					}
					return true;
				}
			}
			else if(key.compareTo(keys[x]) < 0 && !isLeaf())//else find next node to go into
			{
				if(!links[x].isMinimal())//check if next node is non-minimal
				{
					return links[x].remove(key);//delete value from that node
				}
				else if(x+1 <= (order-1) && !links[x+1].isMinimal())//if it is minimal and has a non-minimal neighbor to the right
				{
					stealRight(x,x,x+1);
					//delete value from node
					return remove(key);
				}
				else if(x-1 >= 0 && !links[x-1].isMinimal())//if it is minimal and has a non-minimal neighbor to the left
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
		//move middle value from parent to minimal child
		for(int x = (order/2)-1; x > 0; x--)
		{
			links[to].keys[x] = links[to].keys[x-1];
		}
		links[to].keys[0] = keys[mid];
		for(int x = (order/2); x > 0; x--)
		{
			links[to].links[x] = links[to].links[x-1];
		}
		//move end value of non-minimal child to parent
		//transfer link from non-minimal to minimal
		for(int i = links[from].keys.length-1; i >= 0; i--)
		{
			if(links[from].keys[i] != null)
			{
				keys[mid] = links[from].keys[i];
				links[from].keys[i] = null;
				links[to].links[0] = links[from].links[i+1];
				links[from].links[i+1] = null;
				break;
			}
		}
	}
	public void stealRight(int to, int mid, int from)
	{
		//move middle value from parent to minimal child
		links[to].keys[(order/2)-1] = keys[mid];
		//move end value of non-minimal child to parent
		keys[mid] = links[from].keys[0];
		//move link from non-minimal to minimal
		links[to].links[order/2] = links[from].links[0];
		//shift non-minimal to the left
		for(int i = 0; i < links[from].keys.length-1; i++)
		{
			links[from].keys[i] = links[from].keys[i+1];
			links[from].links[i] = links[from].links[i+1];
		}
		links[from].links[order-2] = links[from].links[order-1];
		links[from].keys[order-2] = null;
		links[from].links[order-1] = null;
	}
	public void merge(int left, int right)
	{
		//create new node with 2 children and middle value from parent
		BTreeNode n = new BTreeNode(order);
		for(int x = 0; x < (order/2)-1; x++)
		{
			n.keys[x] = links[left].keys[x];
		}
		n.keys[3] = keys[left];
		for(int x = 0; x < (order/2)-1; x++)
		{
			n.keys[x+(order/2)] = links[right].keys[x];
		}
		
		for(int x = 0; x < (order/2); x++)
		{
			n.links[x] = links[left].links[x];
		}
		for(int x = 0; x < (order/2); x++)
		{
			n.links[x+(order/2)] = links[right].links[x];
		}
		
		if(left < order-2)
		{
			for(int i = left; i <= keys.length-2; i++)
			{
				keys[i] = keys[i+1];
				links[i+1] = links[i+2];
			}
			keys[order-2] = null;
			links[order-1] = null;
			if(keys[0] == null)
			{
				keys = n.keys;
				links = n.links;
			}
			else
			{
				links[left] = n;
			}
		}
		else
		{
			links[left] = n;
			keys[order-2] = null;
			links[order-1] = null;
		}
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
					return links[x].search(key);
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
					return links[x].search(key);
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
			
			return links[i+1].findPre();
		}
	}
	private boolean isMinimal()
	{
		int i = (order/2)-1;
		return (keys[i] == null);
	}
}//end class








