package com.kpro.sample;

import com.kpro.main.Gio;

public class GioTest {
	

	
	
	public static void main(String[] args) {
		testClassifier();
	}
	
	/**
	 * load a p3p object and return a recommendation
	 */
	private static void testClassifier(){
		Gio gio;
		String[] args = new String[1];

		String loc = "/Users/ulfnore/CustomerDrivenProject/Costumer-Driven-Project/code/P3Ps/snapfish.xml";
		args[0] = "-newPolicyLoc "+loc;
		
		try {
			gio = new Gio(args);

			gio.loadDB();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	

}
