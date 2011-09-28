import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;		//to serialize this class
import java.util.ArrayList;
import java.util.Iterator;



/**
*	This singleton class will store all past P3P/contexts instances in a hashmap.
*
*	@author Nicholas Gerstle, Henrik Knutsen, Aman Kaur
*	@version 1.0
*/

class PDatabase	implements Serializable, Iterable<PolicyObject>, PolicyDatabase
{
	/**
	 * generated serial id based on warning on implementing Serializable
	 */
	private static final long serialVersionUID = -8940764926428061908L;
	private static PolicyDatabase i = new PDatabase(); //singleton instance of this class
	private  ArrayList<PolicyObject> idb; //internal database
	public static String location;
	
	/**
	*	Dummy main to test reading and writing.
	*	
	*	@param String args[]
	*	@authors Henrik Knutsen
	*/
	/*
	public void main(String args[]) {
		/* Initializing testing policies 
		PolicyLight WritePolicy = new PolicyLight("Henrik", "Admin", 7.5, 20);
		PolicyLight ReadPolicy = null;
		
		/* Writing Policy to disk 
		writePolicy(WritePolicy);
		/* Reading data in "Policy.name" to TempPolicy 
		ReadPolicy = readPolicy(WritePolicy.name);
		
		System.out.println(TempPolicy);
	}*/


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
	public static PolicyDatabase getInstance()
	{
		return i;
	}
	/**
	*	public constructor method to ensure singleton-ness
	*
	*	@param loc the location of the serialized db on file
	*	@return reference to PDatabase
	*	@author Nicholas Gerstle
	*/
	public static PolicyDatabase getInstance(String loc)
	{
		location = loc;
		return i;
	}	
	
	/**
	*	Public method to write an POLICYLIGHT object to disk
	*	
	*	@param The object of type PolicyLight (dummy object) to be written
	*	@authors Henrik Knutsen, Aman Kaur
	*/
	//TO DO unless there's a more efficient way, we don't write individual policies to disk, so remove this method, or change PolicyLight to Policy
/*	public void writePolicy(PolicyLight Policy) {
		try {
			// Creating a stream to create the file "Policy.name" and write bytes to it
			// Name of the file can be changed to whatever is wanted
			FileOutputStream fos = new FileOutputStream(Policy.name);
			// Creating a stream convering object to byte data 
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			// Writing datastream of object to file 
			oos.writeObject(Policy);
			// Closing streams 
			fos.close();
			oos.flush(); 
			oos.close();
		} 
		catch(Exception e) {
			// Simple error handling, show error and shut down 
			System.out.println("Exception during serialization: " + e); 
			System.exit(0); 
		}
	} 
*/

	
	/**
	*	Public method to read POLICY object from disk
	*	
	*	@param  Name of the file to be read
	*	@return Object of type PolicyLight (dummy object), that is read from the file
	*	@authors Henrik Knutsen, Aman Kaur
	*/
	//TO DO unless there's a more efficient way, we don't write individual policies to disk, so remove this method, or change PolicyLight to Policy
/*	public PolicyLight readPolicy(String filename) {
		// Initializing the object to be returned 
		PolicyLight TempPolicy = null;
		try { 		
			// Creating a stream to read bytes from file "filename" 
			FileInputStream fis = new FileInputStream(filename);
			// Converting bytedata from FileInputStream to an object 
			ObjectInputStream ois = new ObjectInputStream(fis);
			// Casting the read object and storing it in return variable 
			TempPolicy = (PolicyLight)ois.readObject(); 
			// Closing the streams 
			fis.close();
			ois.close();
		}
		catch(Exception e) {
			// Simple error handling, show error and shut down
			System.out.println("Exception during deserialization: " + e); 
			System.exit(0); 
		} 
		
		return TempPolicy;
	}
*/
	
	


	/* (non-Javadoc)
	 * @see PolicyDatabase#getDBIt()
	 */
	@Override
	public Iterator<PolicyObject> getDBIt()
	{
		return idb.iterator();
	}


	/* (non-Javadoc)
	 * @see PolicyDatabase#addPolicy(PolicyObject)
	 */
	@Override
	public void addPolicy(PolicyObject n)
	{
		idb.add(n);
	}

	/* (non-Javadoc)
	 * @see PolicyDatabase#iterator()
	 */
	@Override
	public Iterator<PolicyObject> iterator() {
		return idb.iterator();
	}
	
	/* (non-Javadoc)
	 * @see PolicyDatabase#closeDB()
	 */
	@Override
	public void closeDB()
	{
		try {
			// Creating a stream to create the file "Policy.name" and write bytes to it
			// Name of the file can be changed to whatever is wanted
			FileOutputStream fos = new FileOutputStream(PDatabase.location);
			// Creating a stream convering object to byte data 
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			// Writing datastream of object to file 
			oos.writeObject(this);
			// Closing streams 
			fos.close();
			oos.flush(); 
			oos.close();
		} 
		catch(Exception e) {
			// Simple error handling, show error and shut down 
			System.out.println("Exception during serialization of policy database: " + e); 
			System.exit(0); 
		}
	}
	
	/**
	 * Loads database from a file dLoc
	 * 
	 * @param dLoc the location of the database file on disk
	 * 
	 * @author ngerstle
	 */
	public static PolicyDatabase loadDB(String dLoc)
	{
		//TODO add check & warning for overwriting existing database-> exception?
		try
		{
			FileInputStream fis = new FileInputStream(dLoc);
			ObjectInputStream ois = new ObjectInputStream(fis);
			i = (PolicyDatabase)ois.readObject();
			PDatabase.location = dLoc;
			ois.close();
			fis.close();
		}
		catch(Exception e)
		{
			System.out.println("Exception deserializing PDatabase at " + dLoc +" .\n");
			e.printStackTrace();
			System.exit(3);
		}
		return  i;
	}
	
	
	

	//TODO return any Policies matching a domain
}
