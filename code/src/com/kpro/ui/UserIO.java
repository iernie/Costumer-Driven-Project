package com.kpro.ui;

import java.util.ArrayList;
import java.util.Properties;
import com.kpro.dataobjects.PolicyObject;
import com.kpro.datastorage.PolicyDatabase;


/**
* The UserIO provides an abstract model of all the methods a user interface method must implement. There are five essential portions: construction of the user interface if necessary, via the object constructor; user reconfiguration (of the same options found in configuration file or on the commandline); the ability to display the database and all loaded policies; user revision, in which the suggested solution is provided to the user so the user can accept or reject it; and shutdown/deconstruction of any resources needed for the interface.
*
*
*/
public abstract class UserIO {

	public UserIO() {
		
	}
	
	
	/**
	 * returns a modified Properties to use init on.
	 * 
	 * @param genProps the default values for all commandline arguments
	 * @return the values to initialize the program with- same as usual args from main
	 */
	public abstract void user_init(Properties genProps);
	
	/**
	 * display the contents of the database
	 * 
	 * @param pdb the database to display
	 * @author ngerstle
	 */
	public abstract void showDatabase(PolicyDatabase pdb);

	/**
	 * gets any policies not already provided for the history
	 * 
	 * @deprecated 
	 * @return an arraylist of policy objects to be added to history prior to the CBR run.
	 * @author ngerstle
	 */
	public abstract ArrayList<PolicyObject> loadHistory();
	
	/**
	 * Displays recommended action for policyObject, and returns used accept verion-
	 * same thing if no change, or altered if user disagrees.
	 * 
	 * @param n the policy display
	 * @return the policy given
	 * @author ngerstle
	 */
	public abstract PolicyObject userResponse(PolicyObject n);
	
	/**
	 * closes all resources used by UserIO - windows, files, streams, etc
	 * 
	 * @author ngerstle
	 */
	public abstract void closeResources();


}
