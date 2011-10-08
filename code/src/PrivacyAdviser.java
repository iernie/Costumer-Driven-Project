/**
 * PrivacyAdvisor is a class that initialize and loads past privacy policy history, a new privacy instance, and makes a recommendation.
 *
 *  
 * @author Nicholas Gerstle (ngerstle)
 */

import java.util.logging.*;		//for logger functionality 


/**
 * Main class.
 * 
 * @version 29.09.11.1
 * @author ngerstle
 *
 */


public class PrivacyAdviser {
//TODO actually use logging
//TODO provide junit tests for generic algorithms


	static Gio theIO;									//interface to outside world
	static Logger logger;								//logger
	private static PolicyObject po;				//the new policyObject to accept/reject/etc
	


	/**
	 * Program in following sequence- init, load stuff for cbr, run cbr, shutdown.
	 * should alter for more flexible cbr options (different algorithms, selected by switch
	 * statement on ReduceChoice, ConclusionChoice, LearnChoice, etc, once those are in the
	 * config file/cli
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
		if(!theIO.isBuilding()) //actually process an object
		{
			po = theIO.getPO();
			/*int k = 1; //size for k in knn algorithm
			DistanceMetric dm = new distanceMetricTest(weightsConfig);
			PolicyDatabase pdb = theIO.getPDB();
			CBR machinelearn = new CBR(theIO, weightsConfig, new Reduction_KNN(dm,pdb,k), new Conclusion_Simple(dm), new Learn_Constant(weightsConfig));*/
			theIO.getCBR().run(po);
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

		if (theIO.getPDB()==null)
			System.err.println("pdb null in PA/init");
		


		//load the past history && commandline policies 
		theIO.loadDB();//configFile.getProperty("databaseLocation"));
		
		
		//TODO place for user_init code?? should have 4 user interfaces- commandline interactive, commandline options only, gui, config file 
		/*where user_init = (-u flag) || (no -b and no -T)
		if(theIO.user_init())
		{
			prompt from user, but load default values
			UI.prompt for -c, -w, -p, -d, -f, -n, -b, -Te
		}*/
		
		//done initializing
	}

		
	
	
	
	



}
