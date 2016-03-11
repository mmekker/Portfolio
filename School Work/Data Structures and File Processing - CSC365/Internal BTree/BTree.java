import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BTree {
	private BTreeNode root;
	private int size;
	private int order;
	public int blockSize;
	
	public BTree(int n)
	{
		order = n;
		size = 0;
		root = new BTreeNode(order);
		blockSize = (32*(n-1) + 4*(n));
	}
	
	public boolean insert(String key)
	{
		if(root.isFull())//if root is full
		{
			if(root.splitRoot(key))
			{
				size++;
				return true;
			}
		}
		else if(root.insert(key))
		{
			size++;
			return true;
		}
		return false;
	}
	public boolean remove(String key)
	{
		if(root.remove(key))
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
		return root.search(key);
	}
	
	public int getSize()
	{
		return size;
	}
	
	
	public static void main(String[] args)
	{
		BTree tree = new BTree(8);
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
	
        System.out.println("Remove all words again.");
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
        System.out.println("size: " + tree.getSize());
		
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
