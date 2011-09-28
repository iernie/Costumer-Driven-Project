import java.io.File;			//for configuration file functionality
import java.io.FileInputStream;		//for configuration file functionality and reading serialized objects
import java.io.FileOutputStream;	//for writing the new weights config file
import java.io.IOException;		//for configuration file functionality
import java.io.InputStream;		//for configuration file functionality
import java.util.Properties;		//for configuration file functionality
import java.util.logging.*;		//for logger functionality
import org.apache.commons.cli.*;	//for command line options



public class Gio {


	private static Logger logger = Logger.getLogger("");		//create logger object
	private FileHandler fh; ;					//creates filehandler for logging
	public String genConfig;					//location of general configuration file
	public String wConfig;						//location of weights configuration file, if specified.
	private String dLocation;					//location of the database
	private String p3pDirLocation;					//location of p3p objects (a folder containing only those objects
	private String p3pLocation;				//location of a single p3p to be parsed
	private PolicyDatabase pdb = null;				//Policy database object
	private boolean newDB = false;				//overwrite/create new database at specified file location
	private boolean building = false;			//if true, the program should load pdb as normal, add any given policies with p/f options, save the DB, and exit
	private String newPol;						//the location of the new policy that goes through knn, given by the -T option
	private Properties newWeights;				//the revised weights, following LearnAlgorithm. written to disk by shutdown(). also used in loading weights during init()
	private UserIO userInterface;				//means of interacting with the user
	
	/**
	 * Constructor fo gio class. There should only be one. Consider this a singleton instance to call I/O messages on.
	 * Constructs and parses command line arguements as well.
	 * 
	 *  @author ngerstle
	 */
	public Gio(String[] args) 
	{
		// create Options object
		Options options = new Options();

		// add the options
		options.addOption("c", true, "general configuration file location");
		options.addOption("w", true, "weights configuration file location");
		options.addOption("d", true, "database file location");
		options.addOption("p", true, "adding to DB: single policy file location");
		options.addOption("f", true, "adding to DB: multiple policy directory location");
		options.addOption("n", true, "create new database in place of old one (doesn't check for existence of old one");
		options.addOption("b", false, "no policy comparison, only build a database");
		options.addOption("T", true, "the policy object to process");
		
		//TODO -in and -out for database, weights files; preprepared response option; user_init option; userInterface option
		
		/*
		 *to load a new database from a folder, but not use cbr on a new object. overwrites old db (-n option)
		 *
		 *./PrivacyAdvisor -b -f -n ./new_policy_history [-c config_file_location][-w weight_config_file_loc][-d db_file_location]
		 *
		 *
		 *to compare policy stored in p.txt, assuming config in default location is valid and used
		 *
		 *./PrivacyAdvisor -T p.txt
		 */

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


		genConfig = cmd.getOptionValue("c");
		if (genConfig == null)
		{
			genConfig = "./PrivacyAdviser.cfg";
		}
		wConfig = cmd.getOptionValue("w"); //don't need to check for null as it is assumed to be in the general config file loaded later
		dLocation= cmd.getOptionValue("d"); //don't need to check for null as it is assumbed to be in the general config file loaded later
		p3pDirLocation = cmd.getOptionValue("f");
		p3pLocation = cmd.getOptionValue("p");
		
		
		if(!(building=(cmd.hasOption("b")))) //only build, nothing else
		{
			newPol = cmd.getOptionValue("t");
		}
		if((p3pDirLocation == null) && (p3pLocation == null))
		{
			System.err.println("no p3p parse option passed");
			System.exit(0);
		}
		
		//TODO change base on commandline/conf-file options
		userInterface = new UserIO_Simple();
	}

	/**
	 * Loads the general config file from either commandline location or default of './PrivacyAdviser.cfg'
	 *  
	 * @return properties object corresponding to given configuration file
	 * @author ngerstle
	 */
	public Properties loadGeneral()
	{
		return loadGeneral(genConfig);
	}

	/**
	 * Loads the general config, either from provided string, or default location (./PrivacyAdviser.cfg)
	 *  
	 * @param location of configuration file
	 * @return properties object corresponding to given configuration file
	 * @author ngerstle
	 */
	public Properties loadGeneral(String fileLoc)
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
	public Properties loadWeights(String fileLoc)
	{
		if(wConfig != null)
		{
			fileLoc = wConfig;
		}

		try 
		{
			File localConfig = new File(fileLoc);
			InputStream is = null;
			if(localConfig.exists())
			{
				is = new FileInputStream(localConfig);
			}
			else
			{
				System.err.println("No weights file is available at "+fileLoc+" . Please place one in the working directory.");
				System.exit(3);
			}
			newWeights.load(is);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.err.println("IOException reading the weights configuration file. Exiting...\n");
			System.exit(1);
		}
		return newWeights;
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
	public void loadDB(String dLoc)
	{

		if(dLocation != null)
		{
			dLoc = wConfig;
		}


		//load database from "dLoc"
		//TODO change to pick which kind of PolicyDatabase here
		pdb = PDatabase.getInstance(dLoc); 
		if(!newDB)
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
		
		if(p3pLocation != null)
		{
			pLoc = new File(p3pLocation);
			//TODO add the current time
			if(!pLoc.exists()){
				System.err.println("no file found at p3p policy location specified by the -p option");
				System.exit(1);
			}
			p = (new P3PParser()).parse(pLoc.getAbsolutePath());
			p.setContext(new Context(null,null,p3pLocation));
			pdb.addPolicy(p);
		}
		else 
		{
			pLoc = new File(p3pDirLocation);
			String[] pfiles = pLoc.list();
			for(int i=0;i<pfiles.length;i++)
			{
				pLoc = new File(pfiles[i]);
				//TODO add the current time
				if(!pLoc.exists()){
					System.err.println("no file found at p3p policy location specified by the -p option");
					System.exit(1);
				}
				p = (new P3PParser()).parse(pLoc.getAbsolutePath());
				p.setContext(new Context(null,null,pfiles[i]));
				pdb.addPolicy(p);
			}
			
		}
	}
	
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
		writePropertyFile(newWeights,wConfig);//TODO change write location to allow for read from a, write to b
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
		return userInterface.userResponse(n);
	}
	
	/**
	 * returns the policy object from the T option
	 * 
	 * @return the policy object to be processed
	 * @author ngerstle
	 */
	public PolicyObject getPO() {

		PolicyObject p = null;
		File pLoc = new File(newPol);
		//TODO add the current time
		if(!pLoc.exists()){
			System.err.println("no file found at p3p policy location specified by the -T option");
			System.exit(1);
		}

		p = (new P3PParser()).parse(pLoc.getAbsolutePath());
		p.setContext(new Context(null,null,p3pLocation));
		return p;
	}

	/**
	 * returns the -b option if present- whether or not to solely build a database, or build and call CBR.run()
	 *  
	 * @return true if a CBR should NOT be run
	 * @author ngerstle
	 */
	public boolean isBuilding() {
		return building;
	}

	public void setWeights(Properties newWeightP) {
		 newWeights = newWeightP;
		
	}
	
	
}
