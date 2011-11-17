package com.kpro.dataobjects;
import java.util.ArrayList;

/**
*	holds results of a algorithmic comparison- t/f on approve, with the nearest neighbor, as well as a string & enum for exception, if it is one
*/
public class Action
{
	private boolean accepted; //true- the user should accept the policy, false the user should not
	private ArrayList<String> reasonDomains; //basis for result- string of other policies.getDomain
	private boolean override; //the generated results was overriden by the user
	private double confidence; //the confidence in the final value- should be between 0 and 1, with 1 = 100% confidence
	
	
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


	/**
	* Sets the confidence, with checks on the value: if confidence = abs(input) if 1>=input>=-1, else negative infinity
	*/
	public void setConfidence(double confidence) {
		if(confidence<0)
			confidence = -confidence;
		if((0<= confidence)&&(confidence <=1))
			this.confidence = confidence;
		else
		{
			System.err.println("!!!setting confidence to "+confidence);
			this.confidence = Double.NEGATIVE_INFINITY;
		}
	}


	public double getConfidence() {
		return confidence;
	}

	/**
	* Parse a comma-seperated string into an Action. The string needs to have four comma-seperated tokens (thus three commas) and no spaces. The format is accept,domains,confidence,override where accept is 'accept' if accept==true, or anything else if accept!=true; domains is a semi-colon seperated string list of domains (no commas, no spaces, etc), eg 'www.google.com;www.yahoo.com;domain3;domain4' ; confidence is a double that is the confidence (parsed by parseDouble), and override is a boolean (parsed by parseBoolean).
	* @param optionValue the option string- see above. must have 3 commas, no spaces
	* @return an Action parsed from above
	*/
	public Action parse(String optionValue) {
		String[] parsedOptions = optionValue.split(",");
		
		if (parsedOptions.length != 4) {
			System.err.println("Not enough arguments given to -userResponse");
			return null;
		}
		
		boolean accept = false;
		if (parsedOptions[0].equalsIgnoreCase("accept")) {
			accept = true;
		}
		
		ArrayList<String> domains = new ArrayList<String>();
		for (String domain: parsedOptions[1].split(";")) {
			domains.add(domain);			
		}
		
		double confidence = Double.parseDouble(parsedOptions[2]);
		boolean override = Boolean.parseBoolean(parsedOptions[3]);
		
		return new Action(accept, domains, confidence, override);
	}

	public Action setOverride(boolean b) {
		this.override = b;
		return this;
	}


	/**
	* Overriden toString. format: ('Accepted.'|'Rejected.')('Override.'|'No Override.')("Confidences: %f")["reasonDomain: [" (" string,")+].
	* @return fancy string version see above. eg "Accepted. Override. Confidence: 0.5 reasonDomain: [ google.com"
	*/	
	@Override
	public String toString() {
		String str = accepted == true ?
				"Accepted. " : "Rejected. ";
		str += override ? "Override. " : "No Override. ";
		str += "Confidence: "+ confidence;
		if(reasonDomains != null) {
			str += " reasonDomain: [";
			for(String i : reasonDomains)
				str += (" "+i+",");			
		}
			
				
		return str;
	}
	
}
