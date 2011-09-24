import java.io.Serializable;

/**
 * 
 * NOTE TO SELF
 * 
 * Object
 * 	hashmap - what we use to compare
 * 		case - from xml
 * 			datatype
 * 			purpose
 * 			recipients
 * 			retention
 * 			distance
 * 		context - object has compare function
 * 			distance
 * 			time
 * 			etc
 *	Action - object
 * 	
 * 		
 */

public class PolicyObject implements Serializable
{
	private Case policyCase;

	public PolicyObject()
	{
		policyCase = new Case();
	}
	
	public void addPurpose(Purpose p)
	{
		policyCase.addPurpose(p);
	}
	
	public void addRetention(Retention t)
	{
		policyCase.addRetention(t);
	}
	
	public void addRecipient(Recipient r)
	{
		policyCase.addRecipient(r);
	}
	
	public void addCategory(Category c)
	{
		policyCase.addCategory(c);
	}
	
	public void addDataType(String s)
	{
		policyCase.addDataType(s);
	}
	
	public void addEntityData(String key, String value)
	{
		policyCase.addEntityData(key, value);
	}
	
	public void debug_print()
	{
		policyCase.debug_print();
	}
}
