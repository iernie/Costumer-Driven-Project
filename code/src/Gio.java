
import java.io.BufferedReader;	//for configuration file functionality
import java.io.File;			//for configuration file functionality
import java.io.FileInputStream;	//for configuration file functionality
import java.io.FileReader;		//for configuration file functionality
import java.io.IOException;		//for configuration file functionality
import java.io.InputStream;		//for configuration file functionality
import java.util.Properties;	//for configuration file functionality

public class Gio {

	
		
	
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return configFile;
	}
	
	Properties loadWeights(String fileLoc)
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
				System.out.println("No weights file is available at "+fileLoc+" . Please place one in the working directory.");
				System.exit(3);
			}
			configFile.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return configFile;
	}
	
	
	
	
	Logger startLogger()
	{
		
	}
	db loadDB()
	{
	}
}
