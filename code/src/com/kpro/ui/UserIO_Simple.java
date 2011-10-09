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
		System.out.println("Privacy Advisor recommends:"+n.getAction().getAccptS() + "\n\t based on these criteria:"+n.getAction().getReasonS());
		return n;
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
