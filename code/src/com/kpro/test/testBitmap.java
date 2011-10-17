package com.kpro.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import com.kpro.algorithm.bitmapDistance;
import com.kpro.dataobjects.*;

import junit.framework.Assert;
import junit.framework.TestCase;

public class testBitmap extends TestCase{
	private bitmapDistance bitmap;
	private Properties weights;
	private PolicyObject POA, POB;

	/*
	public void testCreator(Properties weights){
		bitmap = new bitmapDistance(weights);
		Assert.assertEquals(weights, bitmap);//bad
	}
	*/
	public void testDistance(){
		loadWeights();
		bitmap = new bitmapDistance(weights);
		POA = new PolicyObject();
		POB = new PolicyObject();
		Case temp = new Case();
		temp.addPurpose(Purpose.ADMIN);
		temp.addPurpose(Purpose.DEVELOP);
		temp.addPurpose(Purpose.TELEMARKETING);
		temp.addPurpose(Purpose.OTHER_PURPOSE);
		temp.addRecipient(Recipient.DELIVERY);
		temp.addRecipient(Recipient.OURS);
		temp.addRetention(Retention.NO_RETENTION);
		temp.addRetention(Retention.STATED_PURPOSE);
		temp.setDataType("user.home-info");
		POA.addCase(temp);
		temp=new Case();
		temp.addPurpose(Purpose.ADMIN);
		temp.addPurpose(Purpose.DEVELOP);
//		temp.addPurpose(Purpose.TELEMARKETING);
//		temp.addPurpose(Purpose.OTHER_PURPOSE);
//		temp.addRecipient(Recipient.DELIVERY);
		temp.addRecipient(Recipient.OURS);
		temp.addRetention(Retention.NO_RETENTION);
		temp.addRetention(Retention.STATED_PURPOSE);
		temp.setDataType("business.contact-info.contact.postal");
		POB.addCase(temp);
		
		Assert.assertEquals(18.0,bitmap.getTotalDistance(POA, POB));
	}
	
	
	
	
	
	private void loadWeights()
	{

		//		System.out.println("In loadWeights(): "+System.getProperty("user.dir"));
		try 
		{
			
			
			File localConfig = new File("./src/com/kpro/test/Testweights.cfg");
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
			weights = new Properties();
			weights.load(is);
		}
		catch (IOException e) // TODO: This should probably throw an exception to be handled by userIO. 
		{
			e.printStackTrace();
			System.err.println("IOException reading the weights configuration file. Exiting...\n");
			
//			System.out.println(userInterface instanceof PrivacyAdvisorGUI);
		
		}
		

	}
	
}
