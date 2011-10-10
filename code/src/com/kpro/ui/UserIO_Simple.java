package com.kpro.ui;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import com.kpro.database.PolicyDatabase;
import com.kpro.dataobjects.PolicyObject;


/**
 * @author ngerstle
 * @author ulfnore
 */
public class UserIO_Simple extends UserIO {

	public UserIO_Simple() {
		// TODO Auto-generated constructor stub
		System.out.println("UserIO_Simple constructed!");
	}
	
	/** does nothing
	 * @see UserIO#showDatabase(PolicyDatabase)
	 */
	public void showDatabase(PolicyDatabase pdb) {
		// TODO: 
		System.out.println();
		for(PolicyObject po : pdb)
			System.out.println("\n\n"+po);				
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
	public PolicyObject userResponse(PolicyObject n)
	{
		String response;		
		Scanner sc = new Scanner(System.in);
		
		// Print the recommendation and the reasons for it
		String s = "The case is similar to the following cases:\n";
		for(String i : n.getAction().getReasons())
			s = s+"\t"+ i + "\n";
		System.out.println(s);

		System.out.println("\nPrivacy Advisor recommends:"+parseAcceptedToString(n.getAction().getAccepted()) + 
						   "\n\t based on these criteria:"+n.getAction().getReasons());
		
		
		// prompt if user wants to override
		do{
			System.out.println("Ovverride recommendation? (y/n)\n");
			response = sc.nextLine();
		}while(response != "y" && response != "n");
		
		
		// update action if overridden
		if(response == "y")
			n.getAction().setOverride(true);

		return n;
	}
	
	private String parseAcceptedToString(boolean accepted)
	{
		if(accepted)
		{
			return "\tAccepted";
		}
		else
		{
			return "\tRejected";
		}
	}

	/**
	 * nothing to close
	 * 
	 * @author ngerstle
	 */
	public void closeResources() {
		
	}


	@Override
	public Properties user_init(Properties genProps) {
		return genProps;
	}

}
