import java.util.Iterator;


/**
 * This is a generic interface for databases. All policy 
 * databases must implement this interface. Auto-generated
 * via eclipse from PDatabase.java.
 * @author ngerstle
 *
 */
interface PolicyDatabase {

	/**
	 *	returns an iterator over the arraylist of policies.
	 *	@return iterator over policy arraylist
	 *	@author ngerstle	*
	 */
	public abstract Iterator<PolicyObject> getDBIt();

	/**
	 *	add a new policy object to the database
	 *
	 *	@param n the new policy object
	 *	@author ngerstle
	 */
	public abstract void addPolicy(PolicyObject n);

	/**
	 *	return an iterator over the policy obejcts this contains
	 *
	 * 	@return iterator over internal ArrayList<PolicyObject>
	 */
	public abstract Iterator<PolicyObject> iterator();

	/**
	 * close the database- save it to the location on disk.
	 * 
	 * @author ngerstle
	 */
	public abstract void closeDB();

}