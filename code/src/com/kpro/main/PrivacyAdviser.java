package com.kpro.main;
/**
 * PrivacyAdvisor is a class that initialize and loads past privacy policy history, 
 * a new privacy instance, and makes a recommendation.
 *
 *  
 * @author Nicholas Gerstle (ngerstle)
 */

import java.util.logging.*;		//for logger functionality 

import com.kpro.dataobjects.PolicyObject;


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
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		//all initialization
		init(args);
		theIO.showDatabase();
		//process the given case
		if(!theIO.isBuilding()) //actually process an object
		{
			po = theIO.getPO();
			/*int k = 1; //size for k in knn algorithm
			DistanceMetric dm = new distanceMetricTest(weightsConfig);
			PolicyDatabase pdb = theIO.getPDB();
			CBR machinelearn = new CBR(theIO, weightsConfig, new Reduction_KNN(dm,pdb,k), new Conclusion_Simple(dm), new Learn_Constant(weightsConfig));*/
			theIO.getCBR().run(po);
			theIO.showDatabase();
		}
		//close down
		theIO.shutdown();
	}



	/**
	 * Initializes the program- loads general configuration, starts logger, loads weights, loads database
	 * @author ngerstle
	 * @param args accepts optional command line arguments, including location of general config file (default pwd) 
	 * @throws Exception 
	 *
	 */
	public static void init(String[] args) throws Exception
	{
		//enable IO (and parse args
		try {
			theIO = new Gio(args);
		} catch (Exception e) {
			System.err.println("unable to initiatlize. exiting.");
			e.printStackTrace();
			System.exit(1);
		} 
		//theIO = new Gio(args,this);

		if (theIO.getPDB()==null)
			System.err.println("pdb null in PA/init");



		//load the past history && commandline policies 
		theIO.loadDB();//configFile.getProperty("databaseLocation"));
		//System.err.println("Print pdb in pa:init"+theIO.getPDB());
//		System.out.println(theIO.getPDB());
		//done initializing
	}


}
