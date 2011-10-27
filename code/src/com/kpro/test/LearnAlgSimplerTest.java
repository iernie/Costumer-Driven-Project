package com.kpro.test;

import com.kpro.algorithm.LearnAlgSimpler;
import com.kpro.dataobjects.PolicyObject;
import com.kpro.main.Gio;
import junit.framework.TestCase;

public class LearnAlgSimplerTest extends TestCase {

 
	static Gio theIO;
	private LearnAlgSimpler leanAlg;
	private static PolicyObject po;		

	protected void setUp() throws Exception {

		String[] args = {" ds", "d"};
		System.out.println("LearnAlgSimplerTest running");
		try {
			theIO = new Gio(args);
		} catch (Exception e) {
			System.err.println("unable to initiatlize. exiting.");
			e.printStackTrace();
			System.exit(1);
		} 
		//theIO = new Gio(args,this);
			if (theIO.getPDB()==null){
			System.err.println("pdb null in PA/init");
		}
		theIO.loadDB();
		
		
		theIO.showDatabase();
		//process the given case
		if(!theIO.isBuilding()) //actually process an object
		{
			po = theIO.getPO();
			/*int k = 1; //size for k in knn algorithm
			DistanceMetric dm = new distanceMetricTest(weightsConfig);
			PolicyDatabase pdb = theIO.getPDB();
			CBR machinelearn = new CBR(theIO, weightsConfig, new Reduction_KNN(dm,pdb,k), new Conclusion_Simple(dm), new Learn_Constant(weightsConfig));*/
			theIO.getCBR().run(po);
			theIO.showDatabase();
		}
		//close down
		theIO.shutdown();
		
	}
//	public static void main(String[] args) {
//		System.out.println("sadf");
//	}

	public void testApplyML() {
		
		//An error here? -ulf
//		assertEquals(12, leanAlg.forTest(theIO));
		//fail("Not yet implemented");
	}

	public void testLearnAlgSimpler() {
		System.out.println("derr");
		//fail("Not yet implemented");
	}
}

