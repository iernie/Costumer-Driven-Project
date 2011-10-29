package com.kpro.dataobjects;
import java.util.ArrayList;

/**
*	holds results of a algorithmic comparison- t/f on approve, with the nearest neighbor, as well as a string & enum for exception, if it is one
*/
public class Action
{
	private boolean accepted; //accept the policy
	private ArrayList<String> reasonDomains; //basis for result. set by knn
	private boolean override; //the generated results was overriden by the user
	private double confidence; //the confidence in the final value- should be between 0 and 1
	//TODO add exceptions to rule, override, etc
	
	
	public Action()
	{
		this.override = false;
		this.confidence = 0;
	}
	
	public Action(boolean accept, ArrayList<String> domains, double confidence,
			boolean override) {
		this.accepted = accept;
		this.reasonDomains = domains;
		this.setConfidence(confidence);
		this.override = override;
	}
	
	
	public ArrayList<String> getReason() {
		return reasonDomains;
	}
	public void setReason(ArrayList<String> reason) {
		this.reasonDomains = reason;
	}
	
	/**
	 * converts the internal accept/reject values to a String
	 * 
	 * @author ngerstle, ernie
	 * @return a boolean that can be sent to the user with a accept/reject
	 */
	public String getAcceptedStr(){
		return accepted ? "accepted" : "rejected";
	}
	
	/**
	 * Returns true if the action was accepted, and false otherwise.
	 * @return boolean
	 */
	public boolean getAccepted() {
		return accepted;
	}
		
	/**
	 * Sets the accepted state of the action.
	 * @param boolean accept
	 */
	public void setAccepted(boolean accept) {
		this.accepted = accept;
	}
	
	
	/**
	 * 
	 * @return an arraylist that verbalizes why the policy was accepted or rejected
	 */
	public ArrayList<String> getReasons() {
		return reasonDomains;
	}
	
	/**
	 * Returns true if the action is manually overridden.
	 * @return boolean
	 */
	public boolean isOverridden()
	{
		return override;
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
	
	@Override
	public String toString() {
		String str = accepted == true ?
				"Accepted. " : "Rejected. ";
		str += override ? "Override. " : "No Override. ";
		str += "Confidence: "+ confidence;
			
				
		return str;
	}
	
}