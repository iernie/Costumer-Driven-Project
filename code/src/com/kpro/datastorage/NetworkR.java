package com.kpro.datastorage;

import com.kpro.dataobjects.Action;
import com.kpro.dataobjects.PolicyObject;

/**
 * Network resources class. Abstract. See javadocs for what contains what. Should allow request for an action 
 * (if local knowledge is insufficient), as well as saving to the community database. 
 * 
 * 
 * @author ngerstle
 * @version 17.10.11.1
 */
public abstract class NetworkR {

	private String location = ""; /** Where the database is located*/
	private final String type = ""; /** What kind of database it is- define in subclasses*/
	
	/**
	 * Constructor- should accept as minimum the location of the networked database.
	 * 
	 * @param location the location of the database in a string. format of string determined by type of database.
	 */
	public NetworkR(String options)
	{
		parseNOptions(options);
	}

	/**
	 * Parse the options passed to the constructor into appropriate class variables.
	 * @param options the options string passed to the constructor.
	 */
	protected abstract void parseNOptions(String options);

	/**
	 * accepts a PolicyObject, and returns the remote suggested action for it (the suggestion from the server).
	 * @param a the PolicyObject to obtain a response for.
	 * @return the action to give the policy object.
	 */
	public abstract Action reqAct(PolicyObject a);
	
	
	/**
	 * sends a PolicyObject to the server. It needs to have complete Action attached.
	 * 
	 * @param a the policy to upload remotely.
	 */
	public abstract void saveObj(PolicyObject a);
	
	/**
	 * closes whatever resources were opened during initialization by the constructor
	 */
	public abstract void disconnect();
	
	/**
	 * returns the server type and location.
	 * 
	 * @return String "A "+ typeOfServer + " database located at "+ locationOfServer
	 */
	public String getInfo()
	{
		return "A "+type+" database located at " + location;
	}
	
	
}
