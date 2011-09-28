import java.util.Collection;
import java.util.Iterator;

/**
 * This is abstract class for databases. All policy 
 * databases must implement this interface. Auto-generated
 * via eclipse from PDatabase.java, then made abstract.
 * Does enforce singleton-ness here. need to do it in each subclass.
 * 
 * @author ngerstle
 */

public abstract class PolicyDatabase implements Iterable<PolicyObject> {

	
	protected static PolicyDatabase i;
	protected Collection<PolicyObject> idb;
	public static String location;


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
	 * Loads database from a file dLoc
	 * 
	 * @param dLoc the location of the database file on disk
	 * 
	 * @author ngerstle
	 */
	public void loadDB()
	{
		loadDB(location);
	}
	
	
	/**
	 * Loads database from a file dLoc
	 * we have this public just in case we want to be able to load
	 * a db from a file other than where we plan to store it.
	 * 
	 * @param dLoc the location of the database file on disk
	 * 
	 * @author ngerstle
	 */
	public abstract void loadDB(String dLoc);

	/**
	*	Public method to read POLICY object from disk
	*	
	*	@param  Name of the file to be read
	*	@return Object of type PolicyLight (dummy object), that is read from the file
	*	@authors Henrik Knutsen, Aman Kaur
	*/
	public Iterator<PolicyObject> getDBIt() {
		return idb.iterator();
	}

	public void addPolicy(PolicyObject n) {
		idb.add(n);
	}

	public Iterator<PolicyObject> iterator() {
		return idb.iterator();
	}

	public void closeDB()
	{
		closeDB(location);
	}
	
	public abstract void closeDB(String dLoc);

	protected PolicyDatabase() {
		super();
	}

}