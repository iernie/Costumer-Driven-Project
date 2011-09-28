import java.util.Collection; //for storing policies
import java.util.Iterator; //for interating over the collection

/**
 * This is abstract class for databases. All policy 
 * databases must implement this interface. Auto-generated
 * via eclipse from PDatabase.java, then made abstract.
 * Does enforce singleton-ness here. need to do it in each subclass.
 * 
 * @author ngerstle
 * @version 29.09.11.1
 */
//TODO check for correct singleton-ness

public abstract class PolicyDatabase implements Iterable<PolicyObject> {

	
	protected static PolicyDatabase i; //the only reference to this (for singleton)
	protected Collection<PolicyObject> idb; //the collection of objects
	public static String location;	//where the database is located


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
	*	@author ngerstle
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

	public void addPolicy(PolicyObject n) {
		idb.add(n);
	}

	/**
	 * @return an iterator over the internal collection
	 */
	public Iterator<PolicyObject> iterator() {
		return idb.iterator();
	}

	/**
	 * shouldn't be needed, but implement for faster access in Reduction_KNN
	 * @return
	 */
	public Collection<PolicyObject> getCollection()
	{
		return idb;
	}
	
	/**
	 * calls closeDB(PolicyDatabase.location)
	 */
	public void closeDB()
	{
		closeDB(location);
	}
	
	
	/**
	 * should implement writing the information contained by the PolicyDatabase to a file/disk
	 * @param dLoc the location to save the data
	 * @author ngerstle
	 */
	public abstract void closeDB(String dLoc);

	protected PolicyDatabase() {
		super();
	}

}