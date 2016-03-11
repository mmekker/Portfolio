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
	public int read(int blockNum, byte[] b) throws IOException
	{
		seek(blockNum * blockSize);
		return super.read(b);
	}
	public int getBlockSize() {
		return blockSize;
	}
	public void write(int blockNum, byte[] b) throws IOException
	{
		seek(blockNum * blockSize);
		super.write(b);
	}
	public static void main(String[] Args)
	{
		System.out.println(("a").compareTo("a"));
	}
}
