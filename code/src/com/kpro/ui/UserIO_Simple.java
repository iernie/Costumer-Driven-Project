package com.kpro.ui;
import java.util.ArrayList;
import java.util.Properties;

import com.kpro.database.PolicyDatabase;
import com.kpro.dataobjects.PolicyObject;


/**
 * @author ngerstle
 *
 */
public class UserIO_Simple extends UserIO {

	/** does nothing
	 * @see UserIO#showDatabase(PolicyDatabase)
	 */
	public void showDatabase(PolicyDatabase pdb) {
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
	 * @author ngerstle
	 * 
	 * @see UserIO#userResponse(PolicyObject)
	 */
	public PolicyObject userResponse(PolicyObject n)
	{
		if(n.getAction().isOverridden())
		{
			System.out.println("User manually set this result");
		}
		else
		{
			String s = "The case is similar to the following cases:\n";
			for(String i : n.getAction().getReasons())
			{
				s.concat("\t"+i+"\n");	//returns a user understandable list of policies 
			}
			System.out.println(s);
		}
		System.out.println("Privacy Advisor recommends:"+parseAcceptedToString(n.getAction().getAccepted()) + "\n\t based on these criteria:"+n.getAction().getReasons());
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
