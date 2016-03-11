import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
public class FLRAF extends  RandomAccessFile{
	private int blockSize;
	public FLRAF(int size) throws FileNotFoundException
	{
		super("raf.flraf","rw");
		blockSize = size;
	}
	public byte[] read(int blockNum) throws IOException
	{
		byte[] b = new byte[blockSize];//makes byte array to be returned
		for(int x = 0; x < blockSize; x++)//reads each byte and adds it to b
		{
			seek((blockNum * blockSize) + x);
			b[x] = readByte();
		}
		return b;
	}
	public int getBlockSize() {
		return blockSize;
	}
	public void write(int blockNum, byte[] b) throws IOException
	{
		byte[] newB = new byte[blockSize];
		if(b.length < blockSize)//if b is less then block size
		{
			for(int x = 0; x < blockSize; x++)
			{
				if(x < b.length)//put b into newB
				{
					newB[x] = b[x];
				}
				else//then pad with spaces
				{
					newB[x] = ' ';
				}
			}
		}
		else if(b.length > blockSize)//if b is too big
		{
			for(int x = 0; x < blockSize; x++)
			{
				newB[x] = b[x];//then only take the first blockSize bytes
			}
		}
		else//else write b as is
		{
			newB = b;
		}
		seek(blockNum * blockSize);
		write(newB);
	}
}
