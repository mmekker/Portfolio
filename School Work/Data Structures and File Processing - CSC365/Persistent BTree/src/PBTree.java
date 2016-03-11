import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;

public class PBTree {
	FLRAF f;
	CMS c;
	private int size;
	private int order;
	public int blockSize;
	
	//static boolean test;
	
	public PBTree(int n) throws FileNotFoundException
	{
		order = n;
		size = 0;
		blockSize = (32*(n-1) + 4*(n));
		f = new FLRAF(blockSize);
		c = new CMS(4, f);
		writeNewRoot();
	}
	public void writeNewRoot()
	{
		byte[] b = new byte[blockSize];
		c.write(c.getNextIndex(), b);
	}
	
	public PBTreeNode getRoot()
	{
		PBTreeNode root = new PBTreeNode(order, c);
		byte[] b = new byte[blockSize];
		c.read(0, b);
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
				root.keys[x] = tempHold;
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
			root.links[x] = bb.getInt();
		}
		return root;
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
	public boolean insert(String key)
	{
		PBTreeNode nRoot = getRoot();
		if(nRoot.isFull())//if root is full
		{
			boolean ret = nRoot.splitRoot(key);
			if(nRoot.save)
			{
				nRoot.save = false;
				writeNode(nRoot, 0);
			}
			if(ret)
			{
				size++;
				return true;
			}
		}
		else
		{
			boolean ret = nRoot.insert(key);
			if(nRoot.save)
			{
				nRoot.save = false;
				writeNode(nRoot, 0);
			}
			if(ret)
			{
				size++;
				return true;
			}
		}
		return false;
	}
	public boolean remove(String key)
	{
		if(getRoot().remove(key))
		{
			size--;
			return true;
		}
		else
		{
			return false;
		}
	}
	public String search(String key)
	{
		return getRoot().search(key);
	}
	
	public int getSize()
	{
		return size;
	}
	
	
	public static void main(String[] args) throws FileNotFoundException
	{
		PBTree tree = new PBTree(8);
		String filename = "./words.txt";
		String line = null;
		System.out.println("Add all words.");
        try//add all
        {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) 
            {
            	//System.out.println("1+" + line);
            	tree.insert(line);
            }
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) 
        {
            System.out.println("Unable to open file");                
        }
        catch(IOException ex)
        {
            System.out.println("Error reading file");
        }
        System.out.println("size1: " + tree.getSize());

		System.out.println("Add all words again.");
		try//add all again
		{
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) 
            {
            	//System.out.println("2+" + line);
                tree.insert(line);
            }
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) 
        {
            System.out.println("Unable to open file");                
        }
        catch(IOException ex)
        {
            System.out.println("Error reading file");
        }
        System.out.println("size: " + tree.getSize());
	
        System.out.println("Remove all words.");
		try//remove all
        {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) 
            {
            	//System.out.println("1-" + line);
                tree.remove(line);
            }
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) 
        {
            System.out.println("Unable to open file");                
        }
        catch(IOException ex)
        {
            System.out.println("Error reading file");
        }
        System.out.println("size: " + tree.getSize());
	
        /*System.out.println("Remove all words again.");
        try//remove all again
        {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) 
            {
            	//System.out.println("2-" + line);
                tree.remove(line);
            }
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) 
        {
            System.out.println("Unable to open file");                
        }
        catch(IOException ex)
        {
            System.out.println("Error reading file");
        }
        System.out.println("size: " + tree.getSize());*/
		
		/*BTree n = new BTree(8);
		String[] x = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
				"aa","ab","ac","ad","ae","af","ag","ah","ai","aj","ak","al","am","an","ao","ap","aq","ar","as","at","au",
				"av","aw","ax","ay","az"};
		for(int i = 0; i < x.length; i++)
		{
			System.out.println("Adding " + x[i]);
			n.insert(x[i]);
			printTree(n.root);
		}*/
	}
	
	public static void printTree(BTreeNode n)
	{
		printNode(n);
		System.out.println("");
		for(int x = 0; x < n.links.length; x++)
		{
			if(n.links[x] == null)
			{
				break;
			}
			printNode(n.links[x]);
			System.out.print("     ");
		}
		System.out.println("\n");
		for(int x = 0; x < n.links.length; x++)
		{
			if(n.links[x] != null)
			{
				for(int y = 0; y < n.links[x].links.length; y++)
				{
					if(n.links[x].links[y] == null)
					{
						break;
					}
					printNode(n.links[x].links[y]);
					System.out.print("     ");
				}
			}
		}
		System.out.println("\n");
	}
	
	public static void printNode(BTreeNode n)
	{
		for(int x = 0; x < n.keys.length; x++)
		{
			if(n.keys[x] == null)
			{
				System.out.print("|/");
			}
			else
			{
				System.out.print("|" + n.keys[x]);
			}
		}
		System.out.print("|");
	}
}
