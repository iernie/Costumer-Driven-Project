package com.kpro.datastorage;
import java.util.ArrayList;
import java.util.Collection; //for storing policies
import java.util.Iterator; //for interating over the collection
import com.kpro.dataobjects.PolicyObject;

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
	protected static String inLocation;	//where the database is loaded from
	protected static String outLocation;	//where the database is to be written to


	/**
	*	public constructor method to ensure singleton-ness
	*
	*	@param loc the inlocation of the serialized db on file
	*	@return reference to PDatabase
	*	@author ngerstle
	*/
	//public abstract PolicyDatabase getInstance(String inloc, String outloc);
	/*{
	 * /TODO force implmentation of getInstance in subclasses (static) 
		inLocation = inloc;
		outLocation = outloc;
		return i;
	}*/	
	
	
	/**
	 * Loads database from a file dLoc
	 * 
	 * @param dLoc the inlocation of the database file on disk
	 * 
	 * @author ngerstle
	 */
	public void loadDB()
	{
		loadDB(inLocation);
	}
	
	
	/**
	 * Loads database from a file dLoc
	 * we have this public just in case we want to be able to load
	 * a db from a file other than where we plan to store it.
	 * 
	 * @param dLoc the inlocation of the database file on disk
	 * 
	 * @author ngerstle
	 */
	public abstract void loadDB(String dLoc);


	/**
	* Adds a PolicyObject to the database
	* @param n the policy to add to the database
	*/
	public void addPolicy(PolicyObject n) {
		idb.add(n);
	}

	/**
	 * provides an iterator over the database's PolicyObjects
	 * @return an iterator over the internal collection
	 */
	@Override
	public Iterator<PolicyObject> iterator() {
		return idb.iterator();
	}

	
	/**
	 * calls closeDB(PolicyDatabase.outlocation).
	 * needed due to lack of a good destructor system, as far as java goes (according to google)
	 */
	public void closeDB()
	{
		closeDB(outLocation);
	}
	
	
	/**
	 * should implement writing the information contained by the PolicyDatabase to a file/disk
	 * 
	 * @param dLoc the inlocation to save the data
	 * @author ngerstle
	 */
	public abstract void closeDB(String dLoc);

	protected PolicyDatabase() {
		super();
	}
	
	/**
	 * This class should return all policys for the given domain.
	 * 
	 * @param domain the domain to look for
	 * @author ngerstle
	 */
	public abstract ArrayList<PolicyObject> getDomain(String domain);
	
	
	/**
	 * Standard toString method
	 * @author ulfnore
	 */
	@Override
	public String toString(){
		String outStr = this.getClass().toString() + " " + inLocation + " " + outLocation + "\n";
		for(PolicyObject po : this)
			outStr += po;
		return outStr;
	}
	
	
}
