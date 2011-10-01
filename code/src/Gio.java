import java.io.File;			//for configuration file functionality
import java.io.FileInputStream;		//for configuration file functionality and reading serialized objects
import java.io.FileOutputStream;	//for writing the new weights config file
import java.io.IOException;		//for configuration file functionality
import java.io.InputStream;		//for configuration file functionality
import java.util.Date;
import java.util.Properties;		//for configuration file functionality
import java.util.logging.*;		//for logger functionality
import org.apache.commons.cli.*;	//for command line options


/*  to load a new database from a folder, but not use cbr on a new object. overwrites old db (-n option)
 *  ./PrivacyAdvisor -b -f -n ./new_policy_history [-c config_file_location][-w weight_config_file_loc][-d db_file_location]
 *  to compare policy stored in p.txt, assuming config in default location is valid and used
 *  ./PrivacyAdvisor -T p.txt
 */

public class Gio {


	private static Logger logger = Logger.getLogger("");		//create logger object
	private FileHandler fh = null;					//creates filehandler for logging
	private String genConfig = null;					//location of general configuration file
	private String inWeightsLoc = null;						//location of input weights configuration file, if specified.
	private String outWeightsLoc = null;						//location of output weights configuration file, if specified.
	private String inDBLoc = null;					//input location of the database
	private String outDBLoc = null;					//output location of the database
	private String p3pDirLocation = null;					//location of p3p objects (a folder containing only those objects
	private String p3pLocation = null;				//location of a single p3p to be parsed
	private PolicyDatabase pdb = null;				//Policy database object
	private boolean newDB = false;				//overwrite/create new database at specified file location
	private boolean building = false;			//if true, the program should load pdb as normal, add any given policies with p/f options, save the DB, and exit
	private String newPolLoc = null;						//the location of the new policy that goes through knn, given by the -T option
	private Properties newWeights = null;				//the revised weights, following LearnAlgorithm. written to disk by shutdown(). also used in loading weights during init()
	private UserIO userInterface = null;				//means of interacting with the user
	private Action userResponse = null;					//preset response to recomendation
	private boolean userInitializes;			//override initialization via userInterface.user_init(args);
	private CBR cbr = null;							//the CBR to use-> with algorithms!!
	private boolean blanketAccept = false;			//provide an automatic yes to suggested action
	
	
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
		options.addOption("config", true, "general configuration file location");
		options.addOption("inWeight", true, "input weights configuration file location");
		options.addOption("inDB", true, "input database file location");
		options.addOption("outWeight", true, "output weights configuration file location");
		options.addOption("outDB", true, "output database file location");
		options.addOption("histPolicy", true, "adding to DB: single policy file location");
		options.addOption("histPolicyDir", true, "adding to DB: multiple policy directory location");
		options.addOption("newDB", true, "create new database in place of old one (doesn't check for existence of old one");
		options.addOption("p", true, "the policy object to process");
		options.addOption("r", true,"response to specified policy"); 
		options.addOption("userIO",true,"user interface");
		options.addOption("userInit",false,"initialization via user interface");	//TODO user_init option
		options.addOption("policyDB",true,"PolicyDatabase backend");
		options.addOption("cbrV",true,"CBR to use"); 
		options.addOption("acceptSug",true,"automatically accept the user suggestion"); 
		
		

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


		genConfig = cmd.getOptionValue("config");
		if (genConfig == null)
		{
			genConfig = "./PrivacyAdviser.cfg";
		}
		inWeightsLoc = cmd.getOptionValue("inWeight"); //don't need to check for null as it is assumed to be in the general config file loaded later
		outWeightsLoc = cmd.getOptionValue("outWeight");
		if(outWeightsLoc == null)
		{
			outWeightsLoc = inWeightsLoc;
		}
		inDBLoc= cmd.getOptionValue("inDB"); //don't need to check for null as it is assumbed to be in the general config file loaded later
		outDBLoc= cmd.getOptionValue("inDB");
		if (outDBLoc == null)
		{
			outDBLoc = inDBLoc;
		}
		newDB= cmd.hasOption("newDB");
		p3pDirLocation = cmd.getOptionValue("histPolicyDir");
		p3pLocation = cmd.getOptionValue("histPolicy");
		newPolLoc = cmd.getOptionValue("p");
		if(cmd.hasOption("r"))
		{
			userResponse = parseAct(cmd.getOptionValue("r"));
		}
		userInterface = selectUI(cmd.getOptionValue("userIO"));
		pdb = selectPDB(cmd.getOptionValue("policyDB"));
		cbr = parseCBR(cmd.getOptionValue("cbrV"));
		
		
		blanketAccept = cmd.hasOption("acceptSug");
		
				
		
		if(!(building=(cmd.hasOption("b")))) //only build, nothing else
		{
			newPolLoc = cmd.getOptionValue("t");
		}
		if(!cmd.hasOption("p"))
		{
			building = true;
		}
		else
		{
			building = false;
		}
		
	}

	private CBR parseCBR(String string) {
		return (new CBR(this)).parse(string);
		
	}

	private PolicyDatabase selectPDB(String optionValue) {
		// TODO Add other PolicyDatabase classes, when other classes are made
		return PDatabase.getInstance(inDBLoc, outDBLoc);
	}

	private UserIO selectUI(String optionValue) {
		// TODO Add other UserIO classes, when other classes are made
		return new UserIO_Simple();
	}

	private Action parseAct(String optionValue) {
		// TODO remove this later
		return new Action().parse(optionValue);
	}

	/**
	 * Loads the general config file from either commandline location or default of './PrivacyAdviser.cfg'
	 *  
	 * @return properties object corresponding to given configuration file
	 * @author ngerstle
	 */
	public Properties loadGeneral()
	{
		return loadFromConfig(genConfig);
	}

	/**
	 * Loads the general configuration file, either from provided string, or default location (./PrivacyAdviser.cfg)
	 *
	 *  
	 * @param location of configuration file
	 * @return properties object corresponding to given configuration file
	 * @author ngerstle
	 */
	//TODO MUST BE CALLED AFTER loadCLO
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
		if(cbr == null)
		{
			cbr = parseCBR(configFile.getProperty("cbrV"));
		}
		if(inWeightsLoc == null)
		{
			inWeightsLoc = configFile.getProperty("inweights.cfg","./weights.cfg");
		}
		if(outWeightsLoc == null)
		{
			outWeightsLoc = configFile.getProperty("outweights.cfg","./weights.cfg");
		}
		if(inDBLoc == null)
		{
			inDBLoc = configFile.getProperty("inDBLocation");
		}
		if(outDBLoc == null)
		{
			outDBLoc = configFile.getProperty("outDBLocation");
		}
		if(p3pDirLocation == null)
		{
			p3pDirLocation = configFile.getProperty("");
		}
		if(p3pLocation == null)
		{
			p3pLocation = configFile.getProperty("");;
		}
		if(pdb == null)
		{
			pdb = selectPDB(configFile.getProperty("PolicyDB"));
		}
		if(newDB == false)
		{
			newDB = Boolean.parseBoolean(configFile.getProperty("newDB"));;
		}
		if(newPolLoc == null)
		{
			newPolLoc = configFile.getProperty("newPolicyLocation");
		}
		if(userInterface == null)
		{
			userInterface = selectUI(configFile.getProperty("UserInterface"));
		}
		if(userResponse == null)
		{
			userResponse = parseAct(configFile.getProperty("userResponse"));
		}
		if(userInitializes == false)
		{
			userInitializes = Boolean.parseBoolean(configFile.getProperty("userInitializes"));
		}
		if(cbr == null)
		{
			cbr = parseCBR(configFile.getProperty("cbrV"));
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
			File localConfig = new File(inWeightsLoc);
			InputStream is = null;
			if(localConfig.exists())
			{
				is = new FileInputStream(localConfig);
			}
			else
			{
				System.err.println("No weights file is available at "+inWeightsLoc+" . Please place one in the working directory.");
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
	public void loadDB()
	{
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
			if(!pLoc.exists()){
				System.err.println("no file found at p3p policy location specified by the -p option");
				System.exit(1);
			}
			p = (new P3PParser()).parse(pLoc.getAbsolutePath());
			p.setContext(new Context(new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis()),p3pLocation));
			pdb.addPolicy(p);
		}
		else 
		{
			pLoc = new File(p3pDirLocation);
			String[] pfiles = pLoc.list();
			for(int i=0;i<pfiles.length;i++)
			{
				pLoc = new File(pfiles[i]);
				if(!pLoc.exists()){
					System.err.println("no file found at p3p policy location specified by the -p option");
					System.exit(1);
				}
				p = (new P3PParser()).parse(pLoc.getAbsolutePath());
				p.setContext(new Context(new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis()),pfiles[i]));
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
		writePropertyFile(newWeights,outWeightsLoc);
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
		if(userResponse == null)
		{
			return userInterface.userResponse(n);
		}
		else
		{
			if(blanketAccept)
			{
				return n.setAction(n.getAction().setOverride(true));
			}
			else
			{
				return n.setAction(userResponse);
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
		File pLoc = new File(newPolLoc);
		if(!pLoc.exists()){
			System.err.println("no file found at p3p policy location specified by the -T option");
			System.exit(1);
		}

		p = (new P3PParser()).parse(pLoc.getAbsolutePath());
		p.setContext(new Context(new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis()),p3pLocation));
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

	
	public CBR getCBR() {
		return cbr;
	}
	
	
}
