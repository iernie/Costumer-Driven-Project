
public abstract class UserIO {

	public UserIO() {
		
	}
	
	
	/**
	 * A super simple, static user display of the result on command line. does not wait for user response
	 * @param n the policy display
	 * @return the policy given
	 */
	public abstract PolicyObject userResponse(PolicyObject n);
	
	

}
