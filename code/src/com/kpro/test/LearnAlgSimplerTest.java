package com.kpro.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.kpro.algorithm.LearnAlgSimpler;
import com.kpro.dataobjects.Action;
import com.kpro.dataobjects.Case;
import com.kpro.dataobjects.Context;
import com.kpro.dataobjects.PolicyObject;
import com.kpro.dataobjects.Purpose;
import com.kpro.dataobjects.Recipient;
import com.kpro.dataobjects.Retention;
import com.kpro.datastorage.PDatabase;
import com.kpro.datastorage.PolicyDatabase;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * This class is a junit test class to test the class LearnAlgSimpler
 * For this junit test to work correctly some changes have to be made in LearnAlgSimpler.java
 * These are:
 * 		-comment out "extends LearnAlgorithm"
 * 		-comment out the constructor 
 * 		-make the applyML method take in a Properties and a PolicyDatabase,
 * 		 and make the method public, like this: 
 * 		 			public Properties applyML(Properties prop, PolicyDatabase pd)
 * 		-change the two first lines in applyML to:
 * 					Properties weights = prop;
 *					pdb = pd;
 *		
 * 		
 * @author Nesha
 *
 */
public class LearnAlgSimplerTest extends TestCase {

 
	private PolicyDatabase pdb;
	private PolicyObject PO;
	private LearnAlgSimpler ls;
	
	/**
	 * Sets up a PolicyDatabase with PolicyObjects and their cases
	 */
	protected void setUp(){
		Case case1 = new Case();
		case1.addPurpose(Purpose.ADMIN);
		case1.addPurpose(Purpose.DEVELOP);
		case1.addPurpose(Purpose.TELEMARKETING);
		case1.addPurpose(Purpose.OTHER_PURPOSE);
		case1.addRecipient(Recipient.DELIVERY);
		case1.addRecipient(Recipient.OURS);
		case1.addRetention(Retention.NO_RETENTION);
		case1.addRetention(Retention.STATED_PURPOSE);

		Case case2=new Case();
		case2.addPurpose(Purpose.ADMIN);
		case2.addPurpose(Purpose.DEVELOP);
		case2.addRecipient(Recipient.OURS);
		case2.addRetention(Retention.NO_RETENTION);
		case2.addRetention(Retention.STATED_PURPOSE);

		Action action = new Action();
		action.setAccepted(true);
		
		pdb = PDatabase.getInstance("outDBLoc","inDBLoc");

		PO = new PolicyObject();
		PO.addCase(case1);
		PO.addCase(case2);
		
		PO.setAction(action);
		PO.setContext(new Context(null, null, "a"));
		pdb.addPolicy(PO);

		PO = new PolicyObject();
		PO.addCase(case1);
		PO.addCase(case2);
		PO.setAction(action);
		PO.setContext(new Context(null, null, "b"));
		pdb.addPolicy(PO);
		
	}

	/**
	 * loads the weights file given as parameter and its values/keys as Properties
	 * 
	 * @param path to a weights file 
	 * @return weights Properties 
	 */
	private Properties loadWeights(String path){
		Properties weights = null;
		try {
			File localConfig = new File(path);
			InputStream is = null;
			if(localConfig.exists())
			{
				is = new FileInputStream(localConfig);
			}
			else {
				System.err.println("No weights file at " + path);
				System.exit(0);
				
			}
			weights = new Properties();
			weights.load(is);
		}
		catch (IOException e){
			System.err.println("IOException reading the weights configuration file");
			e.printStackTrace();
		
		}
		return weights;
	}
	
	/**
	 * The actual testing is done here
	 * 
	 * Some lines are commented out because it shows error messages
	 * when the LearnAlgSimpler.java isn't as it should be when this
	 * junit test class is run (see the description of this class).
	 * 
	 * Uncomment the commented lines before running the junit test
	 */
	public void testApplyML() {
		System.out.println("running junit test for LearnAlgSimpler");
		Properties weights = loadWeights("./src/com/kpro/test/weightsLearnAlg.cfg");
		Properties correctAnswer = loadWeights("./src/com/kpro/test/weightsLearnAlgAnswer.cfg");

//		ls = new LearnAlgSimpler();
//	
//		Assert.assertEquals(ls.applyML(weights, pdb), correctAnswer);
//		Assert.assertTrue(ls.applyML(weights, pdb).equals(correctAnswer) );
//		
//		correctAnswer.put("test", "aesfs");
//		Assert.assertFalse(ls.applyML(weights, pdb).equals(correctAnswer) );
		
	}
}

