package com.kpro.datastorage;

import java.io.FileInputStream;		//for reading/writing serialized databases
import java.io.FileOutputStream;	//for reading/writing serialized databases
import java.io.ObjectInputStream;	//for reading/writing serialized databases
import java.io.ObjectOutputStream;	//for reading/writing serialized databases
import java.io.Serializable;		//to serialize this class
import java.util.ArrayList;			//internal collection for policies

import com.kpro.dataobjects.PolicyObject;



/**
*	This singleton class will store all past P3P/contexts instances in a hashmap.
*
*	@author Nicholas Gerstle, Henrik Knutsen, Aman Kaur
*	@version 1.0
*/

public class PDatabase extends PolicyDatabase implements Serializable 
{
	/**
	 * generated serial id based on warning on implementing Serializable
	 */
	private static final long serialVersionUID = -8940764926428061908L;


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
	 * See PolicyDatabase.java. implements loading/closing the db via a serialize PDatabase object.
	 * WILL overwrite existing database if called more than once.
	 *
	 * @param dLoc the location of the database file on disk
	 * @author ngerstle
	 * @see PolicyDatabase#closeDB()
	 */
	@Override
	public void loadDB(String dLoc)
	{
		try
		{
			FileInputStream fis = new FileInputStream(dLoc);
			ObjectInputStream ois = new ObjectInputStream(fis);
			i = (PolicyDatabase)ois.readObject();
			PolicyDatabase.inLocation = dLoc;
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
	@Override
	public void closeDB(String dLoc) 
	{
		try {
			// Creating a stream to create the file "Policy.name" and write bytes to it
			// Name of the file can be changed to whatever is wanted
			if(dLoc==null)
			{
				dLoc = outLocation;
			}
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
			e.printStackTrace();
			System.exit(0); 
		}
	}


	/**
	 * returns a list of all policies from a given domain.
	 * 
	 * @param the domain to check
	 * @return an arraylist of policies from given domain
	 * @author ngerstle
	 * @see  PolicyDatabase#getDomain()
	 */
	@Override
	public ArrayList<PolicyObject> getDomain(String domain) {
		ArrayList<PolicyObject> results = new ArrayList<PolicyObject>();
		for(PolicyObject i : idb)
		{
			if( domain.compareToIgnoreCase(i.getContextDomain()) == 0)
			{
				results.add(i);
			}
		}
		return results;
	}




	/**
	 * 
	 */
	public static synchronized PolicyDatabase getInstance(String inloc, String outloc) {
		inLocation = inloc;
		outLocation = outloc;
		if(i==null)
		{
			i= new PDatabase();
		}
		
		return i;
	}
	
	


	


}
