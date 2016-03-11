import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;


public class tester
{
	public static void main(String[] args) throws IOException
	{
		byte[] b = new byte[4];
		b = intToByteArray(85);
		int x = byteArrayToInt(b);
		System.out.println("b: " + x);
		
		/*FLRAF f = new FLRAF(32);
		CMS c = new CMS(4, f);
		write(c, "banana", 0);
		write(c, "apple", 1);
		write(c, "peach", 2);
		write(c, "pineapple", 3);
		write(c, "orange", 4);
		write(c, "lemon", 5);
		write(c, "lime", 6);
		write(c, "cherry", 7);
		write(c, "watermelon", 8);
		write(c, "berry", 9);
		c.saveAll();
		for(int x = 0; x < 10; x++)
		{
			if(x == 6)
			{
				System.out.print("");
			}
			System.out.println("Block " + x + ": " + read(c, x));
		}*/



		/*System.out.println("Start.");
		String filename = "./words.txt";
		String line = null;
		int count = 0;
		try
		{
		    FileReader fileReader = new FileReader(filename);
		    BufferedReader bufferedReader = new BufferedReader(fileReader);
		    while((line = bufferedReader.readLine()) != null) 
		    {
		    	write(c, line, count, 32);
				count++;
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
		System.out.println("End.");*/
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
	public static String read(CMS f, int blockNum) throws IOException
	{
		int blockSize = f.getBlockSize();
		byte[] b = new byte[blockSize];
		String ret = "";
		f.read(blockNum, b);
		ret += "\"";
		for(int x = 0; x < blockSize; x++)
		{
			if((char) b[x] == ' ')
			{
				break;
			}
			ret += (char) b[x];
		}
		ret += "\"";
		return ret;
	}
	public static void write(CMS f, String s, int blockNum) throws IOException
	{
		int blockSize = f.getBlockSize();
		byte[] b = new byte[blockSize];
		for(int y = 0; y < b.length; y++)
		{
			if(y < s.length())
			{
				b[y] = (byte) s.charAt(y);
			}
			else
			{
				b[y] = ' ';
			}
		}
		f.write(blockNum, b);
	}
}
