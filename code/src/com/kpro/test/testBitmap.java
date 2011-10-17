package com.kpro.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import com.kpro.algorithm.bitmapDistance;

import junit.framework.Assert;
import junit.framework.TestCase;

public class testBitmap extends TestCase{
	private bitmapDistance bitmap;
	private Properties weights;
	
	public void testCreator(Properties weights){
		bitmap = new bitmapDistance(weights);
		Assert.assertEquals(weights, bitmap);//bad
	}
	public void testDistance(){
		
		bitmap = new bitmapDistance(weights);
		Assert.assertEquals(weights, bitmap);//bad
	}
	private void loadWeights()
	{

		//		System.out.println("In loadWeights(): "+System.getProperty("user.dir"));
		try 
		{
			
			
			File localConfig = new File("./weights.cfg");
//			System.out.println(genProps.getProperty("inWeightsLoc"));
			InputStream is = null;
			if(localConfig.exists())
			{
				is = new FileInputStream(localConfig);
			}
			else // TODO: This should probably throw an exception to be handled by userIO. 
			{
				System.err.println("No weights file is available at "+
						" . Please place one in the working directory.");
				
//				System.out.println(userInterface instanceof PrivacyAdvisorGUI);
				
			}
			weightsConfig = new Properties();
			weightsConfig.load(is);
		}
		catch (IOException e) // TODO: This should probably throw an exception to be handled by userIO. 
		{
			e.printStackTrace();
			System.err.println("IOException reading the weights configuration file. Exiting...\n");
			
//			System.out.println(userInterface instanceof PrivacyAdvisorGUI);
		
		}
		

	}
	
}
