import java.util.ArrayList;

/**
*	This singleton class will store all past P3P/contexts instances in a hashmap.
*
*	@author Nicholas Gerstle
*	@version 1.0
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
	*	public method to add an object to the database
	*	
	*	@param i the policyObject to add to the database
	*	@author Nicholas Gerstle
	*/
	public void addObject(PolicyObject i)
	{
		idb.add(i);
	}


	//TODO add interator, distance finder, etc, accessors here
}
