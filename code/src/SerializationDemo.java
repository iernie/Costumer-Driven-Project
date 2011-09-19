import java.io.*; 
//test
/**
 * 
 * @author Aman
 * This class shows the serializaton process 
 * It creates 2 objects Obj1 and Obj2 and saves them as 2 serialized objects (to the outputstream in a file)
 *
 */
public class SerializationDemo { 
	public static void main(String args[]) { 
		// Object serialization 
		try {
			MyClass obj1 = new MyClass("Aman", 23, 2.7e10); // creating object of a serialized class and initializing its values
			System.out.println("object1: " + obj1); //displaying the object values
			MyClass obj2 = new MyClass("simran", 25, 2.7e10); // creating object of a serialized class and initializing its values
			System.out.println("object1: " + obj2); //displaying the object values
			
			FileOutputStream fos = new FileOutputStream("serial1");// creating an output stream to save the object 
			FileOutputStream fos1 = new FileOutputStream("serial2");// creating an output stream to save the object
			
			ObjectOutputStream oos = new ObjectOutputStream(fos); // initializing output stream to write obj1
			ObjectOutputStream oos1 = new ObjectOutputStream(fos1);// initializing output stream to write obj2
			
			oos.writeObject(obj1); // writing to output stream obj1
			oos1.writeObject(obj2);// writing to output stream obj2
			
			oos.flush(); 
			oos.close(); 
		} 
		catch(Exception e) { 
			System.out.println("Exception during serialization: " + e); 
			System.exit(0); 
		} 
	} 
}