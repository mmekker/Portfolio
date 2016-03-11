package bstree;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//Found at http://www.journaldev.com/2452/java-serialization-example-tutorial-serializable-serialversionuid
/** This class holds methods used to serialize and deserialize objects to and from a file
 * 
 * @author Mike Mekker
 *
 */
public class SerializationTool {
	/** deserialize(String fileName) returns and object saved to a file
	 * 
	 * @param fileName					File which you are reading from
	 * @return Object					returns the object read from the file
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object deserialize(String fileName) throws IOException, ClassNotFoundException 
	{
		FileInputStream FIS = new FileInputStream(fileName);
		ObjectInputStream OIS = new ObjectInputStream(FIS);
        Object obj = OIS.readObject();
        OIS.close();
        return obj;
	}
	/** serialize(Object obj, String fileName) Serializes any object to a specific filename
	 * 
	 * @param obj						object your writing to file
	 * @param fileName					File you are writing to
	 * @throws IOException
	 */
	public static void serialize(Object obj, String fileName) throws IOException 
	{
        FileOutputStream FOS = new FileOutputStream(fileName);
        ObjectOutputStream OOS = new ObjectOutputStream(FOS);
        OOS.writeObject(obj);
        FOS.close();
    }
}
