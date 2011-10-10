package com.kpro.dataobjects;
import java.util.ArrayList;

/**
*	holds results of a algorithmic comparison- t/f on approve, with the nearest neighbor, as well as a string & enum for exception, if it is one
*/
public class Action
{
	private boolean accepted; //accept the policy
	private ArrayList<PolicyObject> reason; //basis for result. set by knn
	private boolean override; //the generated results was overriden by the user
	private double confidence; //the confidence in the final value- should be between 0 and 1
	//TODO add exceptions to rule, override, etc
	
	
	public Action()
	{
		this.override = false;
		this.confidence = 0;
	}
	
	public Action(boolean accept, ArrayList<PolicyObject> reason, double confidence,
			boolean override) {
		this.accepted = accept;
		this.reason = reason;
		this.setConfidence(confidence);
		this.override = override;
	}
	
		
	public boolean isAccepted() {
		return accepted;
	}
	
	public void setAccepted(boolean accept) {
		this.accepted = accept;
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
	public String getAccepted() {
		if(accepted)
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
	public String getReasons() {
		if(override)
		{
			return "User manually set this result";
		}
		else
		{
			String s = "The case is similar to the following cases:\n";
			for(PolicyObject i : reason)
			{
				s.concat("\t"+i.getContextDomain()+"\n");	//returns a user understandable list of policies 
			}
			return s;
		}
	}


	public void setConfidence(double confidence) {
		if((0<= confidence)&&(confidence <=1))
			this.confidence = confidence;
		else
			this.confidence = -confidence;
	}


	public double getConfidence() {
		return confidence;
	}


	public Action parse(String optionValue) {
		// TODO make this actually parse, rather than return a generic no
		return new Action(false, null, 1, true);
	}

	public Action setOverride(boolean b) {
		override = b;
		return this;
	}
	
}