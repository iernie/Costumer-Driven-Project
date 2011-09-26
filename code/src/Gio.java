import java.io.BufferedReader;		//for configuration file functionality
import java.io.File;			//for configuration file functionality
import java.io.FileInputStream;		//for configuration file functionality and reading serialized objects
import java.io.FileNotFoundException;
import java.io.FileOutputStream;	//to write serialized objects to file
import java.io.FileReader;		//for configuration file functionality
import java.io.IOException;		//for configuration file functionality
import java.io.InputStream;		//for configuration file functionality
import java.util.Properties;		//for configuration file functionality
import java.util.logging.*;		//for logger functionality
import org.apache.commons.cli.*;	//for command line options
import java.io.ObjectInputStream;       //to read serialized objects from file
import java.io.ObjectOutputStream;      //to read serialized objects from file



public class Gio {


	private static Logger logger = Logger.getLogger("");		//create logger object
	private FileHandler fh; ;					//creates filehandler for logging
	private String genConfig;					//location of general configuration file
	private String wConfig;						//location of weights configuration file, if specified.
	private String dLocation;
	private String p3pDirLocation;					//location of p3p objects (a folder containing only those objects
	private String p3pLocation;				//location of a single p3p to be parsed
	private PDatabase pdb = null;				//Policy database object
	private Boolean newDB = false;				//overwrite/create new database at specified file location


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
		options.addOption("p", true, "single policy file location");
		options.addOption("f", true, "multiple policy directory location");
		options.addOption("n", true, "create new database in place of old one (doesn't check for existence of old one");


		CommandLineParser parser = new PosixParser();
		CommandLine cmd = null;
		try
		{
			cmd = parser.parse( options, args);
		}
		catch (ParseException e)
		{
			System.out.println("Error parsing commandline arguements.");
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

		if((p3pDirLocation == null) && (p3pLocation == null))
		{
			System.out.println("no p3p parse option passed");
			System.exit(0);
		}
	}

	/**
	 * Loads the general config file from either commandline location or default of './PrivacyAdviser.cfg'
	 * 
	 * @author ngerstle
	 * @return properties object corresponding to given configuration file
	 */
	public Properties loadGeneral()
	{
		return loadGeneral(genConfig);
	}

	/**
	 * Loads the general config, either from provided string, or default location (./PrivacyAdviser.cfg)
	 * 
	 * @author ngerstle
	 * @param location of configuration file
	 * @return properties object corresponding to given configuration file
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
				System.out.println("No configuration file at "+fileLoc+ ". Please place one in the working directory.");
				System.exit(3);
			}
			configFile.load(is);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			System.out.println("IOException reading first configuration file. Exiting...\n");
			System.exit(1);
		}	
		return configFile;
	}

	/**
	 * Loads the weights configuration file, from the provided location
	 * 
	 * @author ngerstle
	 * @param location of configuration file
	 * @return properties object corresponding to given configuration file
	 */

	Properties loadWeights(String fileLoc)
	{
		if(wConfig != null)
		{
			fileLoc = wConfig;
		}
		Properties configFile = new Properties();

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
				System.out.println("No weights file is available at "+fileLoc+" . Please place one in the working directory.");
				System.exit(3);
			}
			configFile.load(is);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.out.println("IOException reading the weights configuration file. Exiting...\n");
			System.exit(1);
		}	
		return configFile;
	}


	/**
	 * startLogger initializes and returns a file at logLoc with the results of logging at level logLevel. 
	 * @param logLoc	location of the output log file- a string
	 * @param logLevel	logging level (is parsed by level.parse())
	 * @return	Logger object to log to.
	 */

	Logger startLogger(String logLoc, String logLevel)
	{
		try 
		{
			fh = new FileHandler(logLoc);		//sets output log file at logLoc
		}
		catch (SecurityException e) 
		{
			e.printStackTrace();
			System.out.println("SecurityException establishing logger. Exiting...\n");
			System.exit(1);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.out.println("IOException establishing logger. Exiting...\n");
			System.exit(1);
		}			
		fh.setFormatter(new SimpleFormatter()); 	//format of log is 'human-readable' simpleformat
		logger.addHandler(fh);						//attach formatter to logger
		logger.setLevel(Level.parse(logLevel));		//set log level
		return logger;
	}


	/**
	 * Loads the case history into cache.
	 * 
	 * @author ngerstle
	 * 
	 */
	void loadDB(String dLoc)
	{

		if(dLocation != null)
		{
			dLoc = wConfig;
		}


		//load database from "dLoc"

		PDatabase pdb = null;
		if(newDB)
		{
			//create new db
			pdb = PDatabase.getInstance();
		}
		else
		{
			try
			{
				FileInputStream fis = new FileInputStream(dLoc);
				ObjectInputStream ois = new ObjectInputStream(fis);
				pdb = (PDatabase)ois.readObject();
				ois.close();
				fis.close();
			}
			catch(Exception e)
			{
				System.out.println("Exception deserializing PDatabase at " + dLoc +" .\n");
				e.printStackTrace();
				System.exit(3);
			}
		}
	
		
		loadCLPolicies();
	}

	
	/** 
	 * loads policies from commandline (either -p or -f)
	 * 
	 * @author ngerstle
	 */
	private void loadCLPolicies() {
		//we already checked to make sure we have one of the options avaliable
		File pLoc = null;
		BufferedReader reader = null;
		
		
		if(p3pLocation != null)
		{
			pLoc = new File(p3pLocation);
			//todo add the current time
			try {
				reader = new BufferedReader(new FileReader(p3pLocation));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				System.out.println("no file found at p3p policy location specified by the -p option");
				System.exit(1);
			}
			char[] xml = new char[(int) pLoc.length()];
			try {
				reader.read(xml, 0, (int) pLoc.length());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("error reading file at location specified by the -p option");
				System.exit(1);
			}
			String policyx  = new String(xml);
			pdb.addPolicy((new P3PParser()).parse(policyx));
		}
		else 
		{
			pLoc = new File(p3pDirLocation);
			String[] pfiles = pLoc.list();
			for(int i=0;i<pfiles.length;i++)
			{
				pLoc = new File(pfiles[i]);
				//todo add the current time
				try {
					reader = new BufferedReader(new FileReader(pfiles[i]));
				} catch (FileNotFoundException e1) {
					//file would need to disappear between list and this
					e1.printStackTrace();
					System.out.println("no file found at p3p policy location specified by the -d option");
					System.exit(1);
				}
				char[] xml = new char[(int) pLoc.length()];
				try {
					reader.read(xml, 0, (int) pLoc.length());
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("error reading file at location specified by the -d option");
					System.exit(1);
				}
				String policyx  = new String(xml);
				pdb.addPolicy((new P3PParser()).parse(policyx));	
			}
			
		}
	}
}
