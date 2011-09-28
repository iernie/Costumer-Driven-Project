import java.util.ArrayList;


/**
 * @author ngerstle
 *
 */
public class UserIO_Simple extends UserIO {

	/* (non-Javadoc)
	 * @see UserIO#user_init()
	 */
	@Override
	public String[] user_init(String[] args) {
		return args;
	}

	/* (non-Javadoc)
	 * @see UserIO#showDatabase(PolicyDatabase)
	 */
	@Override
	public void showDatabase(PolicyDatabase pdb) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see UserIO#loadHistory()
	 */
	@Override
	public ArrayList<PolicyObject> loadHistory() {
		// TODO Auto-generated method stub
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

}
