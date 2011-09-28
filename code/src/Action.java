import java.util.ArrayList;

/**
*	holds results of a algorithmic comparison- t/f on approve, with the nearest neighbor, as well as a string & enum for exception, if it is one
*/
public class Action
{
	private boolean accept; //accept the policy
	private ArrayList<PolicyObject> reason; //basis for result. set by knn
	private boolean override= false; //the generated results was overriden by the user
	//TODO add exceptions to rule, override, etc
	
	
	public Action(boolean accept, ArrayList<PolicyObject> reason,
			boolean override) {
		this.accept = accept;
		this.reason = reason;
		this.override = override;
	}
	
		
	public boolean isAccept() {
		return accept;
	}
	
	public void setAccept(boolean accept) {
		this.accept = accept;
	}
	public ArrayList<PolicyObject> getReason() {
		return reason;
	}
	public void setReason(ArrayList<PolicyObject> reason) {
		this.reason = reason;
	}
	
	/**
	 * converts the internal accept/reject values to a String
	 * 
	 * @author ngerstle
	 * @return a string that can be sent to the user with a accept/reject
	 */
	public String getAccptS() {
		if(accept)
		{
			return "\tAccept";
		}
		else
		{
			return "\tReject";
		}
	}
	/**
	 * 
	 * @return a string that verbalizes why the policy was accepted or rejected
	 */
	public String getReasonS() {
		if(override)
		{
			return "User manually set this result";
		}
		else
		{
			String s = "The case is similar to the following cases:\n";
			for(PolicyObject i : reason)
			{
				s.concat("\t"+i.getName()+"\n");	//returns a user understandable list of policies 
			}
			return s;
		}
	}
	
}