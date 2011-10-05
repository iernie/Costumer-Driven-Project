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
	private String genConfig = "./PrivacyAdviser.cfg";					//location of general configuration file
	private String inWeightsLoc = null;						//location of input weights configuration file, if specified.
	private String outWeightsLoc = null;						//location of output weights configuration file, if specified.
	private String inDBLoc = null;					//input location of the database
	private String outDBLoc = null;					//output location of the database
	private String p3pDirLocation = null;					//location of p3p objects (a folder containing only those objects
	private String p3pLocation = null;				//location of a single p3p to be parsed
	private PolicyDatabase pdb;				//Policy database object
	private boolean newDB = false;				//overwrite/create new database at specified file location
	private boolean building = false;			//if true, the program should load pdb as normal, add any given policies with p/f options, save the DB, and exit
	private String newPolLoc = null;						//the location of the new policy that goes through knn, given by the -T option
	private Properties origWeights = null;				//the loaded weights file.
	private Properties newWeights = null;				//the revised weights, following LearnAlgorithm. written to disk by shutdown(). also used in loading weights during init()
	private UserIO userInterface = null;				//means of interacting with the user
	private Action userResponse = null;					//preset response to recomendation
	private boolean userInitializes;			//override initialization via userInterface.user_init(args);
	private CBR cbr = null;							//the CBR to use-> with algorithms!!
	private boolean blanketAccept = false;			//provide an automatic yes to suggested action
	private String loglevel;			//what level to log at: INFO, SEVERE, etc, etc	
	private String logloc ;				//where the log should go
	

	/**
	 * Constructor fo gio class. There should only be one. Consider this a singleton instance to call I/O messages on.
	 * Constructs and parses command line arguements as well.
	 * 
	 *  @author ngerstle
	 */
	public Gio(String[] args) 
	{
			
		loadFromConfig(genConfig);
		
		loadCLO(args);
		
		if(genConfig!="./PrivacyAdvisor.cfg")
		{
			loadFromConfig(genConfig);
			loadCLO(args);
		}
		
		//start the logger
		logger = startLogger(logloc,loglevel);
		
		
		if(userInitializes)
		{
			//TODO pass the current program init values elegantly (both genConfig, config, and CLO), and store return
			userInterface.user_init(null);
		}

		
		

		//load the weights configuration file
		origWeights = new Properties();
		origWeights = loadWeights();
		
		
	}

	/**
	 * accepts the direct commanline options, then parses & implements them.
	 * @param args
	 */
	//TODO refactor a 'tad'
	private void loadCLO(String[] args) 
	{
		// create Options object
		Options options = new Options();

		// add the options
		options.addOption("config", true, "general configuration file location");
		options.addOption("inWeight", true, "input weights configuration file location");
		options.addOption("inDB", true, "input database file location");
		options.addOption("outWeight", true, "output weights configuration file location");
		options.addOption("outDB", true, "output database file location");
		options.addOption("histPolicyS", true, "adding to DB: single policy file location");
		options.addOption("histPolicyDir", true, "adding to DB: multiple policy directory location");
		options.addOption("newDB", false, "create new database in place of old one (doesn't check for existence of old one");
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

		
		if(cmd.hasOption("config"))
		{
			genConfig = cmd.getOptionValue("config");
		}
		if(cmd.hasOption("inWeight"))
		{
			inWeightsLoc = cmd.getOptionValue("inWeight");
		}
		if(cmd.hasOption("outWeight"))
		{
			outWeightsLoc = cmd.getOptionValue("outWeight");
		}
		else
		{
			outWeightsLoc = inWeightsLoc;
		}
		
		if(cmd.hasOption("inDB"))
		{
			inDBLoc= cmd.getOptionValue("inDB");
		}
		if(cmd.hasOption("outDB"))
		{
			outDBLoc= cmd.getOptionValue("outDB");
		}
		else
		{
			outDBLoc = inDBLoc;
		}
		newDB= cmd.hasOption("newDB");
		
		if(cmd.hasOption("histPolicyDir"))
		{
			p3pDirLocation = cmd.getOptionValue("histPolicyDir");
		}
		else if (cmd.hasOption("histPolicyS"))
		{
			p3pLocation = cmd.getOptionValue("histPolicyS");
		}
		
		if (cmd.hasOption("p"))
		{
		newPolLoc = cmd.getOptionValue("p");
		}
		if(cmd.hasOption("r"))
		{
			userResponse = parseAct(cmd.getOptionValue("r"));
		}
		if(cmd.hasOption("userIO"))
		{
			selectUI(cmd.getOptionValue("userIO"));
		}
		if(cmd.hasOption("policyDB"))
		{
			selectPDB(cmd.getOptionValue("policyDB"));
		}
		if (cmd.hasOption("cbrV"))
		{
		cbr = parseCBR(cmd.getOptionValue("cbrV"));
		}

		blanketAccept = cmd.hasOption("acceptSug");

		if(!(building=(!cmd.hasOption("p")))) //only build, nothing else
		{
			newPolLoc = cmd.getOptionValue("t");
		}
	}
	
	/*
	private void loadInitClassVars(Iterator<?> k)
	{
//TODO load based on class field names, rather than stupid long if/else lists
			for(Field i : new Class().getFields())
			{
				i.getName();
			}
		
	}
	*/
	
	
	/**
	 * converts a string into a valid CBR
	 * 
	 * @param string the string to parse
	 * @return the CBR to use
	 * @author ngerstle
	 */
	private CBR parseCBR(String string) {
		return (new CBR(this)).parse(string);

	}

	/**
	 * Should parse a string to select, initialize, and return one of the policy databases coded
	 * 
	 * @param optionValue the string to parse
	 * @return the policy database being used
	 * @author ngerstle
	 */
	private void selectPDB(String optionValue) {
		//TODO this should only be called once, 
		// TODO Add other PolicyDatabase classes, when other classes are made
		//System.err.println("inDBLoc = "+inDBLoc);
		//System.err.println("outDBLoc = "+outDBLoc);
	
		
		pdb = PDatabase.getInstance(inDBLoc, outDBLoc);
		if(pdb==null)
		{
			System.err.println("pdb null in selectPDB");
		}
		
		
		//return PDatabase.getInstance(inDBLoc, outDBLoc);
		
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
		return new Action().parse(optionValue);
	}

	/**
	 * Loads the general configuration file, either from provided string, or default location (./PrivacyAdviser.cfg)
	 *
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
		if(configFile.containsKey("inweights"))
		{
			inWeightsLoc = configFile.getProperty("inweights","./weights.cfg");
		}
		if(configFile.containsKey("outweights"))
		{
			outWeightsLoc = configFile.getProperty("outweights","./weights.cfg");
		}
		if(configFile.containsKey("inDBLocation"))
		{
			inDBLoc = configFile.getProperty("inDBLocation");
		}
		if(configFile.containsKey("outDBLocation"))
		{
			outDBLoc = configFile.getProperty("outDBLocation");
		}
		if(configFile.containsKey("p3pHistDirectory"))
		{
			p3pDirLocation = configFile.getProperty("p3pHistDirectory");
		}
		if(configFile.containsKey("p3pHistFile"))
		{
			p3pLocation = configFile.getProperty("p3pHistFile");;
		}
		if(configFile.containsKey("PolicyDB"))
		{
			selectPDB(configFile.getProperty("PolicyDB"));
			
		}
		if(configFile.containsKey("newDB"))
		{
			newDB = Boolean.parseBoolean(configFile.getProperty("newDB"));;
		}
		if(configFile.containsKey("newPolicyLocation"))
		{
			newPolLoc = configFile.getProperty("newPolicyLocation");
		}
		if(configFile.containsKey("UserInterface"))
		{
			selectUI(configFile.getProperty("UserInterface"));
		}
		if(configFile.containsKey("userResponse"))
		{
			userResponse = parseAct(configFile.getProperty("userResponse"));
		}
		if(configFile.containsKey("userInitializes"))
		{
			userInitializes = Boolean.parseBoolean(configFile.getProperty("userInitializes"));
		}
		if(configFile.containsKey("cbrV"))
		{
			cbr = parseCBR(configFile.getProperty("cbrV"));
		}
		loglevel = configFile.getProperty("loglevel","INFO").toUpperCase();
		logloc = configFile.getProperty("logloc","./log.txt").toUpperCase();
		
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
		if(!newDB)
		{
			pdb.loadDB();
		}
		if (pdb==null)
			System.err.println("pdb null in gio/loaddb:1");
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
				System.err.println("no file found at p3p policy location specified by the -p3p option: "+p3pLocation);
				System.err.println("current location is "+System.getProperty("user.dir"));
				System.exit(1);
			}
			p = (new P3PParser()).parse(pLoc.getAbsolutePath());
			p.setContext(new Context(new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis()),p3pLocation));
			if (pdb==null)
				System.err.println("pdb is null in gio/loadclpol");
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
					System.err.println("no file found at p3p policy location specified by the -histPolicyDir option");
					System.exit(1);
				}
				p = (new P3PParser()).parse(pLoc.getAbsolutePath());
				p.setContext(new Context(new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis()),pfiles[i]));
				pdb.addPolicy(p);
			}

		}
	}

	/**
	 * returns the only policy database
	 * @return the policy database
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
		writePropertyFile(newWeights,outWeightsLoc);
		//TODO userInterface.closeResources(); 

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
		return cbr;
	}


}
