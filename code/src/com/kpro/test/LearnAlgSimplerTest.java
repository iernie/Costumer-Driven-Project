package com.kpro.test;

import com.kpro.algorithm.LearnAlgSimpler;
import com.kpro.main.Gio;
import junit.framework.TestCase;

public class LearnAlgSimplerTest extends TestCase {

 
	static Gio theIO;
	private LearnAlgSimpler leanAlg;
//	private static PolicyObject po;	

	
	protected void setUp() throws Exception {

		System.out.println("testttttt");
		String[] args = {"e","e"};
		try {
			theIO = new Gio(args);
		} catch (Exception e) {
			System.err.println("unable to initiatlize. exiting.");
			e.printStackTrace();
			System.exit(1);
		} 
		theIO.loadDB();
		
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

