
import java.io.BufferedReader;	//for configuration file functionality
import java.io.File;			//for configuration file functionality
import java.io.FileInputStream;	//for configuration file functionality
import java.io.FileReader;		//for configuration file functionality
import java.io.IOException;		//for configuration file functionality
import java.io.InputStream;		//for configuration file functionality
import java.util.Properties;	//for configuration file functionality
import java.util.logging.*;		//for logger functionality

public class Gio {

	
	private static Logger logger = Logger.getLogger("");		//create logger object
    	private FileHandler fh; ;					//creates filehandler for logging
	
	/**
	 * Constructor fo gio class. There should only be one. Consider this a singleton instance to call I/O messages on.
	 * 
	 *  @author ngerstle
	 */
	public Gio() {
	}

	/**
	 * Loads the general config from default location, "./PrivacyAdviser.cfg"
	 * 
	 * @author ngerstle
	 * @return properties object corresponding to given configuration file
	 */
	public Properties loadGeneral()
	{
		return loadGeneral("./PivacyAdviser.cfg");
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
	void loadDB()
	{
	}
}
