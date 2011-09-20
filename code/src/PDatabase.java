import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
*	This singleton class will store all past P3P/contexts instances in a hashmap.
*
*	@author Nicholas Gerstle, Henrik Knutsen
*	@version 1.0
*/

/* WARNING: There is ALOT of redundancy with and within the database classes at the moment.
 * I'll try to clean it up, and also add proper commenting.
 * 
 * Added readPolicy and writePolicy for now, which is using the dummy object in PolicyLight.java
 */

class PDatabase
{
	private static final PDatabase i = new PDatabase(); //singleton instance of this class
	private  ArrayList<PolicyObject> idb; //internal database
	


	/**
	*	private constructor to ensure singleton-ness
	*
	*	@author Nicholas Gerstle
	*/
	private	PDatabase()	
	{
	}
	
	
	/**
	*	public constructor method to ensure singleton-ness
	*
	*	@return reference to PDatabase
	*	@author Nicholas Gerstle
	*/
	public static PDatabase getInstance()
	{
		return i;
	}
	
	/**
	*	public method to write an object to the database
	*	
	*	@param Policy is the PolicyLight (dummy object) to be written
	*	@author Henrik Knutsen
	*/
	
	public void writePolicy(PolicyLight Policy) {
		try {
			FileOutputStream fos = new FileOutputStream(Policy.name);// creating an output stream to save the object 
			
			ObjectOutputStream oos = new ObjectOutputStream(fos); // initializing output stream to write obj1
			
			oos.writeObject(Policy); // writing to output stream obj1
			
			oos.flush(); 
			oos.close();
		} 
		catch(Exception e) { 
			System.out.println("Exception during serialization: " + e); 
			System.exit(0); 
		} 
	} 
	
	/**
	*	public method to read an object to the database
	*	
	*	@param  Filename as a string is the name of the file to be read
	*	@return PolicyLight (dummy object), object that is read from the saved file
	*	@author Henrik Knutsen
	*/
	
	public PolicyLight readPolicy(String filename) {
		PolicyLight TempPolicy = null;
		// Object deserialization 
		try { 			
			FileInputStream fis = new FileInputStream(filename); // creating a stream to read obj1
			//FileInputStream fis1 = new FileInputStream("serial2");// creating a stream to read obj2
			
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			TempPolicy = (PolicyLight)ois.readObject();  // reading and casting the saved obj1
			
			//ois = new ObjectInputStream(fis1);
			//obj2 = (MyClass)ois.readObject();// reading and casting the saved obj2
			
			ois.close();
		}
			//System.out.println("Object1: " + Policy1);
			//System.out.println("object2: " + obj2);
		catch(Exception e) { 
			System.out.println("Exception during deserialization: " + e); 
			System.exit(0); 
		} 
		return TempPolicy;
	}

	//TODO add interator, distance finder, etc, accessors here
}
