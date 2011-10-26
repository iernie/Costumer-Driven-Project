package com.kpro.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.kpro.algorithm.Conclusion_Simple;
import com.kpro.algorithm.Reduction_KNN;
import com.kpro.algorithm.bitmapDistance;
import com.kpro.dataobjects.*;
import com.kpro.datastorage.PDatabase;
import com.kpro.datastorage.PolicyDatabase;

public class testReduction_KNN extends TestCase{
	private PolicyDatabase Set;
	private PolicyObject target;
	private bitmapDistance bitmap;
	private Properties weights;
	private Reduction_KNN KNN;
	
	private void CreateSet(){
		//case 1
		//Set = new PolicyDatabase();
		Case case1 = new Case();
		case1.addPurpose(Purpose.ADMIN);
		case1.addPurpose(Purpose.DEVELOP);
		case1.addPurpose(Purpose.TELEMARKETING);
		case1.addPurpose(Purpose.OTHER_PURPOSE);
		case1.addRecipient(Recipient.DELIVERY);
		case1.addRecipient(Recipient.OURS);
		case1.addRetention(Retention.NO_RETENTION);
		case1.addRetention(Retention.STATED_PURPOSE);
		case1.setDataType("user.home-info");
		//case2
		Case case2=new Case();
		case2.addPurpose(Purpose.ADMIN);
		case2.addPurpose(Purpose.DEVELOP);
//		temp.addPurpose(Purpose.TELEMARKETING);
//		temp.addPurpose(Purpose.OTHER_PURPOSE);
//		temp.addRecipient(Recipient.DELIVERY);
		case2.addRecipient(Recipient.OURS);
		case2.addRetention(Retention.NO_RETENTION);
		case2.addRetention(Retention.STATED_PURPOSE);
		case2.setDataType("business.contact-info.contact.postal");
		Case case3= new Case();
		case3.addPurpose(Purpose.ADMIN);
		case3.addPurpose(Purpose.DEVELOP);
		case3.addPurpose(Purpose.CONTACT);
		case3.addPurpose(Purpose.TELEMARKETING);
		case3.addPurpose(Purpose.OTHER_PURPOSE);
		case3.addRecipient(Recipient.OURS);
		case3.addRecipient(Recipient.OTHER_RECIPIENT);
		case3.addRecipient(Recipient.UNRELATED);
		case3.addRetention(Retention.BUSINESS_PRACTICES);
		case3.addRetention(Retention.INDEFINITELY);
		case3.setDataType("user.home-info");
		Case case4= new Case();
		case4.addPurpose(Purpose.ADMIN);
		case4.addPurpose(Purpose.DEVELOP);
		case4.addPurpose(Purpose.CONTACT);
		case4.addPurpose(Purpose.TELEMARKETING);
		case4.addPurpose(Purpose.OTHER_PURPOSE);
		case4.addRecipient(Recipient.OURS);
		case4.addRecipient(Recipient.OTHER_RECIPIENT);
		case4.addRecipient(Recipient.UNRELATED);
		case4.addRetention(Retention.BUSINESS_PRACTICES);
		case4.addRetention(Retention.INDEFINITELY);
		case4.setDataType("business");
		Case case5= new Case();
		case5.addPurpose(Purpose.ADMIN);
		case5.addPurpose(Purpose.DEVELOP);
		case5.addPurpose(Purpose.CONTACT);
		case5.addPurpose(Purpose.TELEMARKETING);
		case5.addPurpose(Purpose.OTHER_PURPOSE);
		case5.addRecipient(Recipient.OURS);
		case5.addRecipient(Recipient.OTHER_RECIPIENT);
		case5.addRecipient(Recipient.UNRELATED);
		case5.addRetention(Retention.BUSINESS_PRACTICES);
		case5.addRetention(Retention.INDEFINITELY);
		case5.setDataType("dynamic.bitstream");
		Case case6= new Case();
		case6.addPurpose(Purpose.HISTORICAL);
		case6.addPurpose(Purpose.CONTACT);
		case6.addPurpose(Purpose.INDIVIDUAL_ANALYSIS);
		case6.addPurpose(Purpose.CURRENT);
		case6.addRecipient(Recipient.OURS);
		case6.addRecipient(Recipient.DELIVERY);
		case6.addRecipient(Recipient.SAME);
		case6.addRetention(Retention.BUSINESS_PRACTICES);
		case6.addRetention(Retention.LEGAL_REQUIREMENT);
		case6.setDataType("user");
		Case case7= new Case();
		case7.addPurpose(Purpose.HISTORICAL);
		case7.addPurpose(Purpose.CONTACT);
		case7.addPurpose(Purpose.INDIVIDUAL_ANALYSIS);
		case7.addPurpose(Purpose.CURRENT);
		case7.addRecipient(Recipient.OURS);
		case7.addRecipient(Recipient.DELIVERY);
		case7.addRecipient(Recipient.SAME);
		case7.addRetention(Retention.BUSINESS_PRACTICES);
		case7.addRetention(Retention.LEGAL_REQUIREMENT);
		case7.setDataType("business");
		Action acA=new Action();
		Action acD=new Action();
		acA.setAccepted(true);
		acD.setAccepted(false);
		//0
		target=new PolicyObject();
		target.addCase(case1);
		target.addCase(case3);
		target.addCase(case7);
		target.setAction(acD);
		Set.addPolicy(target);
		//1
		target=new PolicyObject();
		target.addCase(case2);
		target.addCase(case3);
		target.addCase(case4);
		target.addCase(case6);
		target.setAction(acA);
		Set.addPolicy(target);
		//2
		target=new PolicyObject();
		target.addCase(case7);
		target.addCase(case2);
		target.addCase(case1);
		target.setAction(acD);
		Set.addPolicy(target);
		//3
		target=new PolicyObject();
		target.addCase(case7);
		target.addCase(case6);
		target.setAction(acD);
		Set.addPolicy(target);
		//4
		target=new PolicyObject();
		target.addCase(case3);
		target.addCase(case5);
		target.addCase(case7);
		target.setAction(acA);
		//5
		Set.addPolicy(target);
		target=new PolicyObject();
		target.addCase(case5);
		target.addCase(case2);
		target.addCase(case3);
		target.setAction(acD);
		Set.addPolicy(target);
		//target
		target=new PolicyObject();
		target.addCase(case6);
		target.addCase(case4);
		target.addCase(case5);

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
	
	public void testDistance(){
		loadWeights();
		CreateSet();
		bitmap= new bitmapDistance(weights);
		KNN=new Reduction_KNN(bitmap,Set,3);
		
		Case case1 = new Case();
		case1.addPurpose(Purpose.ADMIN);
		case1.addPurpose(Purpose.DEVELOP);
		case1.addPurpose(Purpose.TELEMARKETING);
		case1.addPurpose(Purpose.OTHER_PURPOSE);
		case1.addRecipient(Recipient.DELIVERY);
		case1.addRecipient(Recipient.OURS);
		case1.addRetention(Retention.NO_RETENTION);
		case1.addRetention(Retention.STATED_PURPOSE);
		case1.setDataType("user.home-info");
		//case2
		Case case2=new Case();
		case2.addPurpose(Purpose.ADMIN);
		case2.addPurpose(Purpose.DEVELOP);
//		temp.addPurpose(Purpose.TELEMARKETING);
//		temp.addPurpose(Purpose.OTHER_PURPOSE);
//		temp.addRecipient(Recipient.DELIVERY);
		case2.addRecipient(Recipient.OURS);
		case2.addRetention(Retention.NO_RETENTION);
		case2.addRetention(Retention.STATED_PURPOSE);
		case2.setDataType("business.contact-info.contact.postal");
		Case case3= new Case();
		case3.addPurpose(Purpose.ADMIN);
		case3.addPurpose(Purpose.DEVELOP);
		case3.addPurpose(Purpose.CONTACT);
		case3.addPurpose(Purpose.TELEMARKETING);
		case3.addPurpose(Purpose.OTHER_PURPOSE);
		case3.addRecipient(Recipient.OURS);
		case3.addRecipient(Recipient.OTHER_RECIPIENT);
		case3.addRecipient(Recipient.UNRELATED);
		case3.addRetention(Retention.BUSINESS_PRACTICES);
		case3.addRetention(Retention.INDEFINITELY);
		case3.setDataType("user.home-info");
		Case case4= new Case();
		case4.addPurpose(Purpose.ADMIN);
		case4.addPurpose(Purpose.DEVELOP);
		case4.addPurpose(Purpose.CONTACT);
		case4.addPurpose(Purpose.TELEMARKETING);
		case4.addPurpose(Purpose.OTHER_PURPOSE);
		case4.addRecipient(Recipient.OURS);
		case4.addRecipient(Recipient.OTHER_RECIPIENT);
		case4.addRecipient(Recipient.UNRELATED);
		case4.addRetention(Retention.BUSINESS_PRACTICES);
		case4.addRetention(Retention.INDEFINITELY);
		case4.setDataType("business");
		Case case5= new Case();
		case5.addPurpose(Purpose.ADMIN);
		case5.addPurpose(Purpose.DEVELOP);
		case5.addPurpose(Purpose.CONTACT);
		case5.addPurpose(Purpose.TELEMARKETING);
		case5.addPurpose(Purpose.OTHER_PURPOSE);
		case5.addRecipient(Recipient.OURS);
		case5.addRecipient(Recipient.OTHER_RECIPIENT);
		case5.addRecipient(Recipient.UNRELATED);
		case5.addRetention(Retention.BUSINESS_PRACTICES);
		case5.addRetention(Retention.INDEFINITELY);
		case5.setDataType("dynamic.bitstream");
		Case case6= new Case();
		case6.addPurpose(Purpose.HISTORICAL);
		case6.addPurpose(Purpose.CONTACT);
		case6.addPurpose(Purpose.INDIVIDUAL_ANALYSIS);
		case6.addPurpose(Purpose.CURRENT);
		case6.addRecipient(Recipient.OURS);
		case6.addRecipient(Recipient.DELIVERY);
		case6.addRecipient(Recipient.SAME);
		case6.addRetention(Retention.BUSINESS_PRACTICES);
		case6.addRetention(Retention.LEGAL_REQUIREMENT);
		case6.setDataType("user");
		Case case7= new Case();
		case7.addPurpose(Purpose.HISTORICAL);
		case7.addPurpose(Purpose.CONTACT);
		case7.addPurpose(Purpose.INDIVIDUAL_ANALYSIS);
		case7.addPurpose(Purpose.CURRENT);
		case7.addRecipient(Recipient.OURS);
		case7.addRecipient(Recipient.DELIVERY);
		case7.addRecipient(Recipient.SAME);
		case7.addRetention(Retention.BUSINESS_PRACTICES);
		case7.addRetention(Retention.LEGAL_REQUIREMENT);
		case7.setDataType("business");
		
		Action acA=new Action();
		Action acD=new Action();
		acA.setAccepted(true);
		acD.setAccepted(false);
		
		target=new PolicyObject();
		target.addCase(case6);
		target.addCase(case4);
		target.addCase(case5);
		PolicyObject one, two, three;
		one=new PolicyObject();
		one.addCase(case3);
		one.addCase(case5);
		one.addCase(case7);
		one.setAction(acA);
		
		two=new PolicyObject();
		two.addCase(case2);
		two.addCase(case3);
		two.addCase(case4);
		two.addCase(case6);
		two.setAction(acA);
		
		three=new PolicyObject();
		three.addCase(case5);
		three.addCase(case2);
		three.addCase(case3);
		three.setAction(acD);
	
		
		Assert.assertEquals(0,KNN.reduce(target).get(0).getCase(0).compareTo(one.getCase(0)));
		Assert.assertEquals(0,KNN.reduce(target).get(1).getCase(0).compareTo(two.getCase(0)));
		Assert.assertEquals(0,KNN.reduce(target).get(2).getCase(0).compareTo(three.getCase(0)));
		}
}