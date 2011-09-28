import java.io.FileInputStream;		//for reading/writing serialized databases
import java.io.FileOutputStream;	//for reading/writing serialized databases
import java.io.ObjectInputStream;	//for reading/writing serialized databases
import java.io.ObjectOutputStream;	//for reading/writing serialized databases
import java.io.Serializable;		//to serialize this class
import java.util.ArrayList;			//internal collection for policies



/**
*	This singleton class will store all past P3P/contexts instances in a hashmap.
*
*	@author Nicholas Gerstle, Henrik Knutsen, Aman Kaur
*	@version 1.0
*/

class PDatabase extends PolicyDatabase implements Serializable 
{
	/**
	 * generated serial id based on warning on implementing Serializable
	 */
	private static final long serialVersionUID = -8940764926428061908L;
	private static PolicyDatabase i = new PDatabase(); //singleton instance of this class
	public static String location; //where is the database stored on disk/as a file?
	

	/**
	*	private constructor to ensure singleton-ness,
	* deciding what type of collection to use
	*
	*	@author Nicholas Gerstle
	*/
	private	PDatabase()	
	{
		idb = new ArrayList<PolicyObject>();
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
	 * See PolicyDatabase.java. implements loading/closing the db via a serialize PDatabase object.
	 * @see PolicyDatabase#closeDB()
	 *
	 * @param dLoc the location of the database file on disk
	 * 
	 * @author ngerstle
	 * @return 
	 */
	public void loadDB(String dLoc)
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
		
	}


	/**
	 * See PolicyDatabase.java. implements loading/closing the db via a serialize PDatabase object.
	 * @see PolicyDatabase#closeDB()
	 */
	public void closeDB(String dLoc) 
	{
		try {
			// Creating a stream to create the file "Policy.name" and write bytes to it
			// Name of the file can be changed to whatever is wanted
			FileOutputStream fos = new FileOutputStream(dLoc);
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
	
	
	
	

	//TODO return any Policies matching a domain
}
