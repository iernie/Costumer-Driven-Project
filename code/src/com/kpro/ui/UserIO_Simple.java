package com.kpro.ui;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import com.kpro.dataobjects.Action;
import com.kpro.dataobjects.PolicyObject;
import com.kpro.datastorage.PolicyDatabase;


/**
 * @author ngerstle
 * @author ulfnore
 */
public class UserIO_Simple extends UserIO {

	public UserIO_Simple() {

		System.out.println("UserIO_Simple constructed!");
	}
	
	/** does nothing
	 * @see UserIO#showDatabase(PolicyDatabase)
	 */
	@Override
	public void showDatabase(PolicyDatabase pdb) {
		String response;		
		Scanner sc = new Scanner(System.in);
		 
		//do{
			System.out.println("Would you like to see the database now? (y/[n])");
			response = sc.nextLine();
		//}while(!(response.equalsIgnoreCase("y") || response.equalsIgnoreCase("n")));
		if(response.equalsIgnoreCase("y"))
		{
			System.out.println();
			for(PolicyObject po : pdb)
				System.out.println("\n\n"+po);		
			sc.next(".|\\p{Print}|\\p{Space}|\n");
		}

	}

	/** does nothing
	 * @see UserIO#loadHistory()
	 */
	@Override
	public ArrayList<PolicyObject> loadHistory() {
		return null;
	}

	
	/**
	 * A super simple, static user display of the result on command line. does not wait for user response
	 * 
	 * @param n the policy display
	 * @return the policy given
	 * 
	 * @author ngerstle
	 * @author ulfnore
	 * 
	 * @see UserIO#userResponse(PolicyObject)
	 */
	@Override
	public PolicyObject userResponse(PolicyObject n)
	{
		String response;		
		Scanner sc = new Scanner(System.in);
		
		// Print the recommendation and the reasons for it
		String s = "The case is similar to the following cases:\n";
		for(String i : n.getAction().getReasons())
			s = s+"\t"+ i + "\n";
		System.out.println(s);

		System.out.println("Privacy Advisor recommends that you "+parseAcceptedToString(n.getAction().getAccepted()) + 
						   " the new policy, \n\t based on these criteria:"+n.getAction().getReasons());
		
		System.out.println("Confidence is "+n.getAction().getConfidence());
		// prompt if user wants to override
	//	do{
			System.out.println("Override recommendation? (y/[n])");
			response = sc.nextLine();
	//	}while(!(response.equalsIgnoreCase("y") || response.equalsIgnoreCase("n")));
		if(response.equalsIgnoreCase("y"))
		{
			Action a = n.getAction();
			a.setAccepted(!a.getAccepted());
			a.setOverride(true);
			n.setAction(a);
			//add code to backtrack later
		}
		//do{
			System.out.println("Would you like the system to remember your preference permanently? ([y]/n)");
			response = sc.nextLine();
		//}while(!(response.equalsIgnoreCase("y") || response.equalsIgnoreCase("n")));
		// update action if overridden
		if(response.equalsIgnoreCase("n"))
		{
			return null;
		}
		else
		{
			return n;
			
		}
	}
	
	private String parseAcceptedToString(boolean accepted)
	{
		if(accepted)
		{
			return "Accept";
		}
		else
		{
			return "Reject";
		}
	}

	/**
	 * nothing to close
	 * 
	 * @author ngerstle
	 */
	@Override
	public void closeResources() {}


	@Override
	public Properties user_init(Properties genProps) {
		System.out.println("UserIO_Simple does not allowing the user to configure initialization settings at this time.");
		return genProps;
	}

}
