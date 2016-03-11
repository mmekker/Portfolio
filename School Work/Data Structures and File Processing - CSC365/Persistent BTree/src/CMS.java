import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class CMS
{
	private int cacheSize;//how many blocks it can hold
	private int blockSize;//how big the blocks are
	public byte[][] cache;//the actual cache
	private boolean[] changed;//whether or not the blocks have been changed
	private int[] blockNumbers;//the block numbers
	private FLRAF f;//where things are being added
	public ArrayList<Integer> openBlocks;
	int nextIndex = 0;
	Stack<Integer> stack = new Stack<Integer>();

	//takes in int for how big you want  it to be and a flraf
	public CMS(int cacheSize, FLRAF f)
	{
		this.cacheSize = cacheSize;
		this.blockSize = f.getBlockSize();
		this.cache = new byte[cacheSize][blockSize]; //[row][column]
		this.changed = new boolean[cacheSize];
		this.blockNumbers = new int[cacheSize];
		this.f = f;
		this.openBlocks = new ArrayList<Integer>();
	}
	
	//save a specific row to the flraf
	public void save(int row)
	{
		try
		{
			f.write(blockNumbers[row], cache[row]);
		}
		catch(IOException ex)
		{
			System.out.println("Failed to write to FLRAF.");
		}
		changed[row] = false;
	}
	//saves all blocks in cache
	public void saveAll()
	{
		for(int x = 0; x < cacheSize; x++)
		{	
			if(changed[x])//checks if they have been changed first
			{
				save(x);
			}
		}
	}

	//used to find the next row in the cache to write a node to
	public int findRow()
	{
		for(int x = 0; x < cacheSize; x++) //check for open spots
			if(cache[x] == null){return x;}
		for(int x = 0; x < cacheSize; x++) //check for unchanged spots
			if(changed[x] == false){return x;}
		saveAll(); //if all are changed
		return 0; //return first spot
	}

	//brings node into the cache and changes it
	public void write(int blockNum, byte[] b)
	{
		for(int x = 0; x < cacheSize; x++)//checks to see if node is already in the cache
		{
			if(blockNumbers[x] == blockNum)
			{
				cache[x] = b;
				changed[x] = true;
				return;
			}
		}
		int row = findRow();//if not add it to the cache
		cache[row] = b;
		changed[row] = true;
		blockNumbers[row] = blockNum;
	}

	//brings a specific node into the cache and reads it
	public void read(int blockNum, byte[] b)
	{
		for(int x = 0; x < cacheSize; x++)//check to see if it is already in the cache
		{
			if(blockNumbers[x] == blockNum)
			{
				for(int y = 0; y < b.length; y++)
				{
					b[y] = cache[x][y];
				}
				return;
			}
		}
		int row = findRow();//if not find where it can add it to the cache and then read it
		try
		{
			f.read(blockNum, cache[row]);

		}
		catch(IOException ex)
		{
			System.out.println("Failed to read from FLRAF.");
		}
		blockNumbers[row] = blockNum;
		for(int x = 0; x < b.length; x++)
		{
			b[x] = cache[row][x];
		}
	}

	//getter for block size
	public int getBlockSize()
	{
		return blockSize;
	}

	public int getNextIndex()
	{
		if(!stack.isEmpty())
		{
			return stack.pop();
		}
		nextIndex++;
		return nextIndex-1;
	}

	public void realloc(int i)
	{
		stack.push(i);
	}
}