package com.kpro.main;

import java.io.File;			//for configuration file functionality
import java.io.FileInputStream;		//for configuration file functionality and reading serialized objects
import java.io.FileOutputStream;	//for writing the new weights config file
import java.io.IOException;		//for configuration file functionality
import java.io.InputStream;		//for configuration file functionality
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Properties;		//for configuration file functionality
import java.util.logging.*;		//for logger functionality
import org.apache.commons.cli.*;	//for command line options

import com.kpro.algorithm.CBR;
import com.kpro.parser.P3PParser;

import com.kpro.dataobjects.*;
import com.kpro.datastorage.*;
import com.kpro.ui.*;





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


	private static Logger logger = Logger.getLogger("");	/**create logger object*/
	private FileHandler fh = null;							/**creates filehandler for logging*/
	private Properties genProps = new Properties(); 		/**holds all the property values*/
	private Properties origWeights = null;					/**the loaded weights file.*/
	private Properties newWeights = null;					/**the revised weights, following LearnAlgorithm. 
															written to disk by shutdown().
															also used in loading weights during init()*/
	private UserIO userInterface = null;					/**means of interacting with the user*/
	private PolicyDatabase pdb;								/**Policy database object*/
	private NetworkR nr;									/** Network Resource (community advice database)*/
	private PolicyObject po;								/** The new PolicyObject that is parsed **/

	
	/**
	 * Constructor fo gio class. There should only be one. Consider this a singleton instance to call I/O messages on.
	 * Constructs and parses command line arguements as well.
	 * 
	 * @throws Exception Mostly from loadWeights, but should also happen for loadFromConfig
	 */
	public Gio(String[] args) throws Exception 
	{
		this(args,null);
	}

	
	

	/**
	 * A constructor permitting a user interface class to launch everything and be in control.
	 * 
	 * @param args any commandline arguements
	 * @param ui the known UserIO object
	 * @throws Exception Mostly from loadWeights, but should also happen for loadFromConfig
	 */
	public Gio(String[] args, UserIO ui) throws Exception
	{
		
		userInterface = ui;
		genProps = loadFromConfig("./PrivacyAdviser.cfg");
		loadCLO(args);
		//TODO add method to check validity of genProps (after each file load, clo load, and ui load).

		if((genProps.getProperty("genConfig")!=null) &&(genProps.getProperty("genConfig")!="./PrivacyAdvisor.cfg"))
		{
			System.err.println("clo config call");
			genProps = loadFromConfig(genProps.getProperty("genConfig")); //TODO merge, not override
			loadCLO(args);
		}

		//start the logger
		logger = startLogger(genProps.getProperty("loglocation","./LOG.txt"),genProps.getProperty("loglevel","INFO"));
		if(userInterface ==null)
		{
			selectUI(genProps.getProperty("userIO"));
		}
		selectPDB(genProps.getProperty("policyDB"));

		//load the weights configuration file
		origWeights = new Properties();
		origWeights = loadWeights();
		if(Boolean.parseBoolean(genProps.getProperty("useNet","false")))
		{
			startNetwork();
		}
		else
		{
			nr = null;
		}
	}
	

	/**
	* call the user interface's general configuration method if the userInit option is true, and a user interface exists
	*/
	public void configUI() {
		if(Boolean.parseBoolean(genProps.getProperty("userInit","false")) && !(userInterface==null))
		{
			userInterface.user_init(genProps);
		}
	}
	
	public void setGenProps(Properties genProps) {
		this.genProps = genProps;
	}

	/**
	 * accepts the direct commandline options, then parses & implements them.
	 * 
	 * @param args
	 */
	//TODO add exception for invalid options
	private void loadCLO(String[] args) 
	{
		Options options = new Options();

		String[][] clolist= 
			{
				//{"variable/optionname", "description"},
				{"genConfig","general configuration file location"},
				{"inWeightsLoc", "input weights configuration file location"},
				{"inDBLoc", "input database file location"},
				{"outWeightsLoc", "output weights configuration file location"},
				{"outDBLoc",  "output database file location"},
				{"p3pLocation", "adding to DB: single policy file location"},
				{"p3pDirLocation", "adding to DB: multiple policy directory location"},
				{"newDB", "create new database in place of old one (doesn't check for existence of old one"},
				{"newPolicyLoc", "the policy object to process"},
				{"userResponse","response to specified policy"},
				{"userIO","user interface"},
				{"userInit","initialization via user interface"},
				{"policyDB","PolicyDatabase backend"},
				{"cbrV","CBR to use"},
				{"blanketAccept","automatically accept the user suggestion"}, 
				{"loglevel","level of things save to the log- see java logging details"},
				{"policyDB","PolicyDatabase backend"},
				{"NetworkRType","Network Resource type"},
				{"NetworkROptions","Network Resource options"},
				{"confidenceLevel","Confidence threshold for consulting a networked resource"},
				{"useNet","use networking options"},
				{"loglocation","where to save the log file"},
				{"loglevel","the java logging level to use. See online documentation for enums."}
			};

		for(String[] i : clolist)
		{
			options.addOption(i[0],true,i[1]);
		}

		CommandLineParser parser = new PosixParser();
		CommandLine cmd = null;
		try
		{
			cmd = parser.parse( options, args);
		}
		catch (ParseException e)
		{
			System.err.println("Error parsing commandline arguments.");
			e.printStackTrace();
			System.exit(3);
		}
		/*
		for(String i : args)
		{
			System.err.println(i);
		}
		*/
		for(String[] i : clolist)
		{
			if(cmd.hasOption(i[0]))
			{
				System.err.println("found option i: "+i);
				genProps.setProperty(i[0],cmd.getOptionValue(i[0]));
			}
		}
		System.err.println(genProps);


	}


	/**
	 * converts a string into a valid CBR
	 * 
	 * @param string the string to parse
	 * @return the CBR to use
	 * @throws Exception 
	 */
	private CBR parseCBR(String string) throws Exception {

		try {
			return (string == null)?(null):(new CBR(this)).parse(string);
		} catch (Exception e) {
			System.err.println("error parsing CBR, exiting.");
			e.printStackTrace();
			System.exit(5);
			return null;
		}

	}

	/**
	 * Should parse a string to select, initialize, and return one of the policy databases coded
	 * 
	 * @param optionValue the string to parse
	 * @return the policy database being used
	 */
	private void selectPDB(String optionValue) {
		try {
			Class<?> cls = Class.forName("com.kpro.datastorage."+optionValue);
			
			@SuppressWarnings("rawtypes")
			Class[] params = new Class[2];
			params[0] = String.class;
			params[1] = String.class;
			Method m = cls.getDeclaredMethod("getInstance", params);
			
			Object[] argslist = new Object[2];
			argslist[0] = genProps.getProperty("inDBLoc");
			argslist[1] = genProps.getProperty("outDBLoc",genProps.getProperty("inDBLoc"));
			pdb = (PolicyDatabase) m.invoke(null, argslist);
		} catch (Exception e) {
			System.err.println("Selected PolicyDatabase not found");
		}

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
	 */
	private void selectUI(String optionValue) {
		try {
			Class<?> cls = Class.forName("com.kpro.ui."+optionValue);
			userInterface = (UserIO) cls.newInstance();
		} catch (Exception e) {
			System.err.println("Selected UserIO not found");
		}
	}


	/**
	 * Should parse a string to select, initialize, and return one of the actions (result of checking an object) coded.
	 * 
	 * @param optionValue the string to parse
	 * @return the action to apply to the new policy
	 */
	private Action parseAct(String optionValue) {
		return (optionValue == null)?(null):(new Action().parse(optionValue));
	}

	/**
	 * Loads the general configuration file, either from provided string, or default location (./PrivacyAdviser.cfg)
	 *
	 * @param location of configuration file
	 * @return properties object corresponding to given configuration file
	 */
	//TODO add exception for invalid options
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
	 * @param location of configuration file <---- ????
	 * @return properties object corresponding to given configuration file
	 * @throws Exception if there's an issue reading the file (if it doesn't exist, or has an IO error)
	 */
	public Properties loadWeights() throws Exception
	{
		try 
		{
			if(genProps.getProperty("inWeightsLoc") == null)
			{
				System.err.println("inWeightsLoc in Gio/LoadWeights is null");
			}
			File localConfig = new File(genProps.getProperty("inWeightsLoc"));
			//			System.out.println(genProps.getProperty("inWeightsLoc"));
			InputStream is = null;
			if(localConfig.exists())
			{
				is = new FileInputStream(localConfig);
			}
			else 
			{
				System.err.println("No weights file is available at "+genProps.getProperty("inWeightsLoc")+
						" . Please place one in the working directory.");
				throw new Exception("In class Gio.java:loadWeights(), file "+genProps.getProperty("inWeightsLoc")+" doesn't exist.");
			}
			origWeights = new Properties();
			origWeights.load(is);
		} 
		catch (IOException e)  
		{
			e.printStackTrace();
			System.err.println("IOException reading the weights configuration file....\n");
			throw new Exception("In class Gio.java:loadWeights(), IOException loading the weights from file "+genProps.getProperty("inWeightsLoc")+" .");
		}
		
		return origWeights;
	}


	/**
	 * startLogger initializes and returns a file at logLoc with the results of logging at level logLevel.
	 *  
	 * @param logLoc	location of the output log file- a string
	 * @param logLevel	logging level (is parsed by level.parse())
	 * @return	Logger object to log to.
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
	 * 
	 */
	public void loadDB()
	{
		try {
			//TODO what about if we want to create a new db?
			if(!Boolean.parseBoolean(genProps.getProperty("newDB")))
			{
				pdb.loadDB();
			}			
		} catch (Exception e) {
			System.err.println("Something wrong with loading DB");
		}
		try {
			loadCLPolicies();				
		} catch (Exception e) {
			System.err.println("Error. Probably wrong path to P3P folder");
		}
	}

	/** 
	 * loads [additional] policies from commandline (either -p or -f)
	 * 
	 */
	private void loadCLPolicies() {
		//we already checked to make sure we have one of the options avaliable
		File pLoc = null;
		PolicyObject p = null;

		if(genProps.getProperty("p3pLocation",null) != null)
		{
			pLoc = new File(genProps.getProperty("p3pLocation"));
			if(!pLoc.exists()){
				System.err.println("no file found at p3p policy location specified by the -p3p option: "+
						genProps.getProperty("p3pLocation"));
				System.err.println("current location is "+System.getProperty("user.dir"));
				System.exit(1);
			}
			try
			{
				p = (new P3PParser()).parse(pLoc.getAbsolutePath());
				if(p.getContextDomain()==null)
				{
					p.setContext(new Context(new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis()),genProps.getProperty("p3pLocation")));
				}
				pdb.addPolicy(p);
			}
			catch(Exception e)
			{
				System.err.println("Error with parsing or database");
				e.printStackTrace();
				//System.exit(5);
			}
		}
		if(genProps.getProperty("p3pDirLocation",null) != null)
		{
			pLoc = new File(genProps.getProperty("p3pDirLocation"));
			File[] pfiles = pLoc.listFiles();

			//System.err.println("pfiles for p3pDirLocation: "+pfiles);
			for(int i=0;i<pfiles.length;i++)
			{

				pLoc = (pfiles[i]);
				if(!pLoc.exists()){
					System.err.println("no file found at p3p policy location specified by the -p3pDirLocation option, "+ 
							genProps.getProperty("p3pDirLocation"));
					System.exit(1);
				}
				try
				{

					p = (new P3PParser()).parse(pLoc.getAbsolutePath());
					if(p.getContext().getDomain()==null)
					{
						p.setContext(new Context(new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis()),pfiles[i].getAbsolutePath()));
					}
					pdb.addPolicy(p);
				}
				catch(Exception e)
				{
					System.err.println("Einar needs to fix this parsing error.");
					e.printStackTrace();
				}			
			}
		}
	}

	/**
	 * returns the only policy database
	 * 
	 * @return the policy database
	 */
	public PolicyDatabase getPDB()
	{
		return pdb;
	}

	/**
	 * closes resources and write everything to file
	 * 
	 */
	public void shutdown() {
		
		pdb.closeDB(); //save the db
		if(newWeights == null)
		{
			newWeights = origWeights;
		}
		writePropertyFile(newWeights,genProps.getProperty("outWeightsLoc",genProps.getProperty("inWeightsLoc")));
		userInterface.closeResources();
		System.out.println("preferences saved and closed without error.");
	}


	/**
	 * writes a property file to disk
	 * 
	 * @param wprops the property file to write
	 * @param wloc	where to write to
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
	 */
	public PolicyObject userResponse(PolicyObject n) {
		if((parseAct(genProps.getProperty("userResponse",null)) == null) && 
				!Boolean.parseBoolean(genProps.getProperty("blanketAccept", "false")))
		{
			return userInterface.userResponse(n);
		}
		else
		{
			if(Boolean.parseBoolean(genProps.getProperty("blanketAccept", "false")))
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
	 * returns the policy object from the policyObject option
	 * 
	 * @return the policy object to be processed
	 */
	public void loadPO() {

		if(genProps.getProperty("newPolicyLoc",null) == null)
			System.err.println("newPolLoc == null in gio:loadPO");
		File pLoc = new File(genProps.getProperty("newPolicyLoc",null));
		if(!pLoc.exists()){
			System.err.println("no file found at p3p policy location specified by the new policy option");
			System.exit(1);
		}

		po = (new P3PParser()).parse(pLoc.getAbsolutePath());
		//TODO make sure that the context is parsed if avaliable
		if(po.getContext().getDomain()==null)
		{
			po.setContext(new Context(new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis()),genProps.getProperty("newPolicyLoc")));
		}
	}
	
	public PolicyObject getPO() {
		return po;
	}

	/**
	 * returns the true if it should only build
	 *  
	 * @return true if a CBR should NOT be run
	 */
	public boolean isBuilding() {
		return (genProps.getProperty("newPolicyLoc",null)==null);
	}

	/**
	 * saves the new weights to a buffer variable before writing in the shutdown call
	 * 
	 * @param newWeightP the new weights file to save
	 */
	public void setWeights(Properties newWeightP) {
		newWeights = newWeightP;

	}


	/**
	 * returns the CBR to use
	 * 
	 * @return the cbr to use
	 * @throws Exception 
	 */
	public CBR getCBR() throws Exception {
		return parseCBR(genProps.getProperty("cbrV",null));
	}

	/**
	 * returns the originally imported set of weights
	 * @return the weights for policy attributes
	 */
	public Properties getWeights() {
		return origWeights;
	}

	/**
	 * shows the database on the user interface, if the user interface exists and no user response is specied 
	 * and there is no 'blanketAccept' option.
	 */
	public void showDatabase() {
		if(userInterface!=null) //the user interface exists
		{
			if (!genProps.contains("userResponse")) // if we have no preknown user response
			{	
				if  (genProps.getProperty("blanketAccept",null)==null) //if we have no blanket accept
				{
					userInterface.showDatabase(pdb); //we must be running interactive
				}
			} 
		}
		
	}


	/**
	 * GUI classes should use this to ensure the user passes valid files to load.
	 * 	
	 * @param filepath path of the file to check
	 * @return true if the file exists, else false
	 */
	public boolean fileExists(String filepath)
	{
		return (new File(filepath)).exists(); 
	}


	
	/**
	 * Starts the NetworkR specificied by the configuration settings.
	 * 
	 * @throws ClassNotFoundException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	private void startNetwork() throws ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException {
		//System.err.println("startnetwork called");
		Class<?> cls = Class.forName("com.kpro.datastorage."+genProps.getProperty("NetworkRType"));
		if(cls == null)
		{
			System.err.println("NetworkRType incorrect- null in Gio.startNetwork()");
		}
		nr = (NetworkR) cls.getDeclaredConstructors()[0].newInstance(genProps.getProperty("NetworkROptions"));
		//System.err.println("nr in startNetwork="+nr);
	}
	
	public NetworkR getNR()
	{
		return nr;
	}

	/**
	 * gets the confidence level threshold from the configuration
	 * @return the confidence threshold
	 */
	public double getConfLevel() {
		return Double.parseDouble(genProps.getProperty("confidenceLevel","1.0"));
	}

}
