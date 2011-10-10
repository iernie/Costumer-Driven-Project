package com.kpro.main;
import java.io.File;			//for configuration file functionality
import java.io.FileInputStream;		//for configuration file functionality and reading serialized objects
import java.io.FileOutputStream;	//for writing the new weights config file
import java.io.IOException;		//for configuration file functionality
import java.io.InputStream;		//for configuration file functionality
import java.util.Date;
import java.util.Properties;		//for configuration file functionality
import java.util.logging.*;		//for logger functionality
import org.apache.commons.cli.*;	//for command line options

import com.kpro.algorithm.CBR;
import com.kpro.database.PDatabase;
import com.kpro.database.PolicyDatabase;
import com.kpro.dataobjects.Action;
import com.kpro.dataobjects.Context;
import com.kpro.dataobjects.PolicyObject;
import com.kpro.parser.P3PParser;
import com.kpro.ui.UserIO;
import com.kpro.ui.UserIO_Simple;


/*  to load a new database from a folder, but not use cbr on a new object. overwrites old db (-n option)
 *  ./PrivacyAdvisor -b -f -n ./new_policy_history [-c config_file_location][-w weight_config_file_loc][-d db_file_location]
 *  to compare policy stored in p.txt, assuming config in default location is valid and used
 *  ./PrivacyAdvisor -T p.txt
 */


/* Thinking:
 * 
 * user init > commandline > config file
 * 
 * so assume config at general location (load with loadgen(defaultloc))
 * check to see if commandline config (load with loadgen(newloc))
 * handle other commandline options
 * handle userinit
 * 
 */

public class Gio {


	private static Logger logger = Logger.getLogger("");		//create logger object
	private FileHandler fh = null;					//creates filehandler for logging
	private Properties genProps = new Properties(); //holds all the property values
	
	private Properties origWeights = null;				//the loaded weights file.
	private Properties newWeights = null;				//the revised weights, following LearnAlgorithm. written to disk by shutdown(). also used in loading weights during init()
	private UserIO userInterface = null;				//means of interacting with the user
	private PolicyDatabase pdb;				//Policy database object

	/**
	 * Constructor fo gio class. There should only be one. Consider this a singleton instance to call I/O messages on.
	 * Constructs and parses command line arguements as well.
	 * 
	 *  @author ngerstle
	 */
	public Gio(String[] args) 
	{
			
		loadFromConfig("./PrivacyAdviser.cfg");
		loadCLO(args);
		
		
		//TODO add method to check validity of genProps (after each file load, clo load, and ui load).
		
		if(genProps.getProperty("genConfig")!="./PrivacyAdvisor.cfg")
		{
			loadFromConfig(genProps.getProperty("genConfig"));
			loadCLO(args);
		}
		
		//start the logger
		logger = startLogger(genProps.getProperty("loglocation","./LOG.txt"),genProps.getProperty("loglevel","INFO"));
		selectUI(genProps.getProperty("UserIO"));
		
		if(Boolean.parseBoolean(genProps.getProperty("userInit","false")))
		{
			genProps = userInterface.user_init(genProps);
		}
		
		selectPDB(genProps.getProperty("policyDB"));
		
		//load the weights configuration file
		origWeights = new Properties();
		origWeights = loadWeights();
		

	}

	/**
	 * accepts the direct commandline options, then parses & implements them.
	 * 
	 * @param args
	 * @author ngerstle
	 */
	private void loadCLO(String[] args) 
	{
		Options options = new Options();
		
		String[][] clolist= 
		{
				{"genConfig","true","general configuration file location"},
				{"inWeightsLoc", "true", "input weights configuration file location"},
				{"inDBLoc", "true", "input database file location"},
				{"outWeightsLoc", "true", "output weights configuration file location"},
				{"outDBLoc", "true", "output database file location"},
				{"P3PLocation","true", "adding to DB: single policy file location"},
				{"P3PDirLocation","true", "adding to DB: multiple policy directory location"},
				{"newDB","false", "create new database in place of old one (doesn't check for existence of old one"},
				{"newPolicyLoc","true", "the policy object to process"},
				{"userResponse","true","response to specified policy"},
				{"userIO","true","user interface"},
				{"userInit","false","initialization via user interface"},
				{"policyDB","true","PolicyDatabase backend"},
				{"cbrV","true","CBR to use"},
				{"blanketAccept","false","automatically accept the user suggestion"}, 
				{"loglevel","true","level of things save to the log- see java logging details"}
		};
		
		for(String[] i : clolist)
		{
			options.addOption(i[0], Boolean.parseBoolean(i[1]),i[2]);
		}

		CommandLineParser parser = new PosixParser();
		CommandLine cmd = null;
		try
		{
			cmd = parser.parse( options, args);
		}
		catch (ParseException e)
		{
			System.err.println("Error parsing commandline arguements.");
			e.printStackTrace();
			System.exit(3);
		}

		for(String[] i : clolist)
		{
			if(cmd.hasOption(i[0]))
			{
				genProps.setProperty(i[0],cmd.getOptionValue(i[0]));
			}
		}
		
		
		
	}
	
	
	/**
	 * converts a string into a valid CBR
	 * 
	 * @param string the string to parse
	 * @return the CBR to use
	 * @author ngerstle
	 */
	private CBR parseCBR(String string) {
		
		return (string == null)?(null):(new CBR(this)).parse(string);

	}

	/**
	 * Should parse a string to select, initialize, and return one of the policy databases coded
	 * 
	 * @param optionValue the string to parse
	 * @return the policy database being used
	 * @author ngerstle
	 */
	private void selectPDB(String optionValue) {
		// TODO Add other PolicyDatabase classes, when other classes are made
		pdb = PDatabase.getInstance(genProps.getProperty("inDBLoc"), genProps.getProperty("outDBLoc",genProps.getProperty("inDBLoc")));
		if(pdb==null)
		{
			System.err.println("pdb null in selectPDB");
		}
	}

	/**
	 * Should parse a string to select, initialize, and return the user interface selected
	 * 
	 * @param optionValue the string to parse
	 * @return the user interface to use
	 * @author ngerstle
	 */
	private void selectUI(String optionValue) {
		// TODO Add other UserIO classes, when other classes are made
		userInterface = new UserIO_Simple();
	}

	
	/**
	 * Should parse a string to select, initialize, and return one of the actions (result of checking an object) coded.
	 * 
	 * @param optionValue the string to parse
	 * @return the action to apply to the new policy
	 * @author ngerstle
	 */
	private Action parseAct(String optionValue) {
		// TODO remove this later
		return (optionValue == null)?(null):(new Action().parse(optionValue));
	}

	/**
	 * Loads the general configuration file, either from provided string, or default location (./PrivacyAdviser.cfg)
	 *
	 * @param location of configuration file
	 * @return properties object corresponding to given configuration file
	 * @author ngerstle
	 */
	public Properties loadFromConfig(String fileLoc)
	{
		Properties configFile = new Properties();

		try {
			File localConfig = new File(fileLoc);
			InputStream is = null;
			if(localConfig.exists())
			{
				is = new FileInputStream(localConfig);
			}
			else
			{
				System.err.println("No configuration file at "+fileLoc+ ". Please place one in the working directory.");
				System.exit(3);
			}
			configFile.load(is);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			System.err.println("IOException reading first configuration file. Exiting...\n");
			System.exit(1);
		}
		return configFile;
	}


	

	/**
	 * Loads the weights configuration file, from the provided location
	 * 
	 * @param location of configuration file
	 * @return properties object corresponding to given configuration file
	 * @author ngerstle
	 */
	public Properties loadWeights()
	{

		try 
		{
			if(genProps.getProperty("inWeightsLoc") == null)
			{
				System.err.println("inWeightsLoc in Gio/LoadWeights is null");
			}
			File localConfig = new File(genProps.getProperty("inWeightsLoc"));
			
			InputStream is = null;
			if(localConfig.exists())
			{
				is = new FileInputStream(localConfig);
			}
			else
			{
				System.err.println("No weights file is available at "+genProps.getProperty("inWeightsLoc")+" . Please place one in the working directory.");
				System.exit(3);
			}
			origWeights = new Properties();
			origWeights.load(is);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.err.println("IOException reading the weights configuration file. Exiting...\n");
			System.exit(1);
		}
		return origWeights;
	}

	
	/**
	 * startLogger initializes and returns a file at logLoc with the results of logging at level logLevel.
	 *  
	 * @param logLoc	location of the output log file- a string
	 * @param logLevel	logging level (is parsed by level.parse())
	 * @return	Logger object to log to.
	 * @author ngerstle
	 */
	public Logger startLogger(String logLoc, String logLevel)
	{
		try 
		{
			fh = new FileHandler(logLoc);		//sets output log file at logLoc
		}
		catch (SecurityException e) 
		{
			e.printStackTrace();
			System.err.println("SecurityException establishing logger. Exiting...\n");
			System.exit(1);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.err.println("IOException establishing logger. Exiting...\n");
			System.exit(1);
		}			
		fh.setFormatter(new SimpleFormatter()); 	//format of log is 'human-readable' simpleformat
		logger.addHandler(fh);						//attach formatter to logger
		logger.setLevel(Level.parse(logLevel));		//set log level
		return logger;
	}

	/**
	 * Loads the case history into cache. 
	 * This is where the background database chosen.
	 * 
	 * @param dLoc the location of the database
	 * @author ngerstle
	 * 
	 */
	public void loadDB()
	{
		if(!Boolean.parseBoolean(genProps.getProperty("newDB")))
		{
			pdb.loadDB();
		}
		loadCLPolicies();
	}

	/** 
	 * loads [additional] policies from commandline (either -p or -f)
	 * 
	 * @author ngerstle
	 */
	private void loadCLPolicies() {
		//we already checked to make sure we have one of the options avaliable
		File pLoc = null;
		PolicyObject p = null;

		if(genProps.getProperty("p3pLocation",null) != null)
		{
			pLoc = new File(genProps.getProperty("p3pLocation"));
			if(!pLoc.exists()){
				System.err.println("no file found at p3p policy location specified by the -p3p option: "+genProps.getProperty("p3pLocation"));
				System.err.println("current location is "+System.getProperty("user.dir"));
				System.exit(1);
			}
			p = (new P3PParser()).parse(pLoc.getAbsolutePath());
			if(p.getContext().getDomain()==null)
			{
				p.setContext(new Context(new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis()),genProps.getProperty("p3pLocation")));
			}			
			pdb.addPolicy(p);
		}
		if(genProps.getProperty("p3pDirLocation",null) != null)
		{
			pLoc = new File(genProps.getProperty("p3pDirLocation"));
			String[] pfiles = pLoc.list();
			for(int i=0;i<pfiles.length;i++)
			{
				pLoc = new File(pfiles[i]);
				if(!pLoc.exists()){
					System.err.println("no file found at p3p policy location specified by the -histPolicyDir option");
					System.exit(1);
				}
				p = (new P3PParser()).parse(pLoc.getAbsolutePath());
				if(p.getContext().getDomain()==null)
				{
					p.setContext(new Context(new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis()),pfiles[i]));
				}
				pdb.addPolicy(p);
			}

		}
	}

	/**
	 * returns the only policy database
	 * 
	 * @return the policy database
	 * @author ngerstle
	 */
	public PolicyDatabase getPDB()
	{
		return pdb;
	}

	/**
	 * closes resources and write everything to file
	 * 
	 * @author ngerstle
	 */
	public void shutdown() {
		pdb.closeDB(); //save the db
		if(newWeights == null)
		{
			newWeights = origWeights;
		}
		writePropertyFile(newWeights,genProps.getProperty("outWeightsLoc",genProps.getProperty("inWeightsLoc")));
		userInterface.closeResources(); 
	}
	

	/**
	 * writes a property file to disk
	 * 
	 * @param wprops the property file to write
	 * @param wloc	where to write to
	 * @author ngerstle
	 */
	private void writePropertyFile(Properties wprops, String wloc)
	{
		if(wprops==null)
			System.out.println("wrops null in gio/writepropertyfile");
		if(wloc ==null)
			System.out.println("wloc null in gio/writepropertyfile");
		try 
		{
			wprops.store(new FileOutputStream(wloc), null);
		} 
		catch (IOException e) 
		{
			System.err.println("Error writing weights to file.");
			e.printStackTrace();
			System.exit(3);
		}
	}


	/**
	 * Generates handles response. This is were we would pass stuff to cli or gui, etc
	 * 
	 * @param n the processed policy object
	 * @return the policyObjected as accepted by user (potentially modified
	 * @author ngerstle
	 */
	public PolicyObject userResponse(PolicyObject n) {
		if((parseAct(genProps.getProperty("userResponse",null)) == null) && !Boolean.parseBoolean(genProps.getProperty("blanketAccept")))
		{
			return userInterface.userResponse(n);
		}
		else
		{
			if(Boolean.parseBoolean(genProps.getProperty("blanketAccept")))
			{
				return n.setAction(n.getAction().setOverride(true));
			}
			else
			{
				return n.setAction(parseAct(genProps.getProperty("userResponse",null)));
			}
		}
	}
	

	/**
	 * returns the policy object from the T option
	 * 
	 * @return the policy object to be processed
	 * @author ngerstle
	 */
	public PolicyObject getPO() {

		PolicyObject p = null;
		if(genProps.getProperty("newPolicyLoc",null) == null)
			System.err.println("newPolLoc == null in gio:getPO");
		File pLoc = new File(genProps.getProperty("newPolicyLoc",null));
		if(!pLoc.exists()){
			System.err.println("no file found at p3p policy location specified by the new policy option");
			System.exit(1);
		}

		p = (new P3PParser()).parse(pLoc.getAbsolutePath());
		//TODO make sure that the context is parsed if avaliable
		if(p.getContext().getDomain()==null)
		{
			p.setContext(new Context(new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis()),genProps.getProperty("newPolicyLoc")));
		}
		return p;
	}

	/**
	 * returns the -b option if present- whether or not to solely build a database, or build and call CBR.run()
	 *  
	 * @return true if a CBR should NOT be run
	 * @author ngerstle
	 */
	public boolean isBuilding() {
		return (genProps.getProperty("newPolicyLoc",null)==null);
	}

	/**
	 * saves the new weights to a buffer variable before writing in the shutdown call
	 * 
	 * @param newWeightP the new weights file to save
	 * @author ngerstle
	 */
	public void setWeights(Properties newWeightP) {
		newWeights = newWeightP;

	}

	
	/**
	 * returns the CBR to use
	 * 
	 * @return the cbr to use
	 * @author ngerstle
	 */
	public CBR getCBR() {
		return parseCBR(genProps.getProperty("cbrV",null));
	}


}
