/**
 * PrivacyAdvisor is a class that initialize and loads past privacy policy history, a new privacy instance, and makes a recommendation.
 *
 *  
 * @author Nicholas Gerstle (ngerstle)
 */

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
		//TODO check for right args
		
		//enable IO
		theIO = new Gio(args); 

		//load general configuration file
		configFile = new Properties();
		if(args.length > 0) //if commandline arguement
		{
			configFile = theIO.loadGeneral(args[0]);
		}
		else
		{
			configFile = theIO.loadGeneral();
		}
		
		//start the logger
		String loglevel = configFile.getProperty("loglevel","INFO").toUpperCase();
		String logloc = configFile.getProperty("logloc","./log.txt").toUpperCase();
		logger = theIO.startLogger(logloc,loglevel);
		
		//load the weights configuration file
		weightsConfig = new Properties();
		weightsConfig = theIO.loadWeights(configFile.getProperty("weights.cfg","./weights.cfg"));
		
		
		//load the past history
		
		
	}
	
	

}
