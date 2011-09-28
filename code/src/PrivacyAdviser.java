/**
 * PrivacyAdvisor is a class that initialize and loads past privacy policy history, a new privacy instance, and makes a recommendation.
 *
 *  
 * @author Nicholas Gerstle (ngerstle)
 */

import java.util.ArrayList;
import java.util.Properties;	//for configuration file functionality
import java.util.logging.*;		//for logger functionality 


/**
 * Main class.
 * 
 * @version 060911.1
 * @author ngerstle
 *
 */


public class PrivacyAdviser {



	static Gio theIO;									//interface to outside world
	private static Properties configFile;		//configuration file with location of other things, logger
	private static Properties weightsConfig;	//configuration file for weights
	static Logger logger;								//logger
	private static PolicyObject po;				//the new policyObject to accept/reject/etc


	/**
	 * Program in following sequence- init, load db, load new instance, classify instance, confirm, close.
	 * 
	 * 
	 * @author ngerstle
	 * 
	 * @param args accepts optional command line arguments, including location of general config file (default pwd) 
	 */
	public static void main(String[] args) {
		//all initialization
		init(args);
		//process the given case
		//TODO change null to actual policy object
		if(!theIO.isBuilding()) //actually process an object
		{
			po = process(po);
			//get user response
			po = theIO.userResponse(po);
			//save to database
			theIO.getPDB().addPolicy(po);
		}
		//close down
		theIO.shutdown();
	}





	/**
	 * Initializes the program- loads general configuration, starts logger, loads weights, loads database
	 * 
	 * 
	 * @author ngerstle
	 * 
	 * @param args accepts optional command line arguments, including location of general config file (default pwd) 
	 *
	 */
	public static void init(String[] args)
	{
		//enable IO (and parse args
		theIO = new Gio(args); 

		//load general configuration file
		configFile = theIO.loadGeneral();


		//start the logger
		String loglevel = configFile.getProperty("loglevel","INFO").toUpperCase();
		String logloc = configFile.getProperty("logloc","./log.txt").toUpperCase();
		logger = theIO.startLogger(logloc,loglevel);

		//load the weights configuration file
		weightsConfig = new Properties();
		weightsConfig = theIO.loadWeights(configFile.getProperty("weights.cfg","./weights.cfg"));


		//load the past history && commandline policies 
		theIO.loadDB(configFile.getProperty("databaseLocation"));	

		//load the policyObject
		po = theIO.getPO();
		//done initializing
	}

	/**
	 * Accepts a parsed PolicyObject that needs a action attached to it
	 * 
	 * 
	 * @author ngerstle
	 * 
	 * @param newPO the new policy to be processed
	 * @return the same policy object with an action
	 */
	private static PolicyObject process(PolicyObject newPO) {
		kNearestNeighbors knn = new kNearestNeighbors(new distanceMetricTest(weightsConfig),theIO.getPDB(), 1);
		ArrayList<PolicyObject> nearestPOs = knn.run(newPO);
		newPO.setAction(conclusion(nearestPOs));
		return newPO;
	}	
	
	/**
	 * draws a conclusion from the knearest neighbors of what the resultant action should be
	 * @param knearestns
	 * @return the action to take
	 */
	private static Action conclusion(ArrayList<PolicyObject> knearestns)
	{
		//TODO make this take a 'conclusion' class or something for more generic evaluations of results
		return simpleConclusion(knearestns);
	}
	
	/**
	 * a simple conclusion that just bases the result on the closest policy, copying action, and citing it as the reason
	 * @param knearestns
	 * @return the action to take
	 */
	private static Action simpleConclusion(ArrayList<PolicyObject> knearestns)
	{
		PolicyObject nearestPO = knearestns.get(0);
		ArrayList<PolicyObject> reasons = new ArrayList<PolicyObject>();
		reasons.add(nearestPO);
		Action a = new Action(nearestPO.getAction().isAccept(), reasons, false);
		return a;
	}



}
