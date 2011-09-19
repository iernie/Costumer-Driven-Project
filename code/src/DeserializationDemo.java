import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 
 * @author Aman
 * This class reads the serialized objects from file system and displaying it
 *
 */

public class DeserializationDemo { 
	public static void main(String args[]) { 
		// Object deserialization 
		try { 
			MyClass obj1,obj2; 
			
			FileInputStream fis = new FileInputStream("serial1"); // creating a stream to read obj1
			FileInputStream fis1 = new FileInputStream("serial2");// creating a stream to read obj2
			
			ObjectInputStream ois = new ObjectInputStream(fis); 
			obj1 = (MyClass)ois.readObject();  // reading and casting the saved obj1
			ois = new ObjectInputStream(fis1);
			obj2 = (MyClass)ois.readObject();// reading and casting the saved obj2
			ois.close(); 
			
			System.out.println("object1: " + obj1);
			System.out.println("object2: " + obj2);
		} 
		
		catch(Exception e) { 
			System.out.println("Exception during deserialization: " + e); 
			System.exit(0); 
		} 
	} 
} 