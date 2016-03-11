import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class tester
{
	public static void main(String[] args) throws IOException
	{
		FLRAF f = new FLRAF(32);
		byte[] b = new byte[32];
		String[] words = {"apple", "orange", "peach", "banana", "watermelon"};
		for(int x = 0; x < words.length; x++)
		{
			write(f, words[x], 32);
		}
		for(int x = 0; x < f.length()/32; x++)
		{
			System.out.println(read(f, x));
		}
		
		/*String filename = "./words.txt";
		String line = null;
        try
        {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) 
            {
            	
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
        }*/
		
	}
	public static String read(FLRAF f, int blockNum) throws IOException
	{
		byte[] bb = new byte[32];
		String ret = "";
		bb = f.read(blockNum);
		ret += "\"";
		for(int y = 0; y < bb.length; y++)
		{
			ret += (char) bb[y];
		}
		ret += " \"";
		return ret;
	}
	public static void write(FLRAF f, String s, int blockNum) throws IOException
	{
		byte[] b = new byte[32];
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
