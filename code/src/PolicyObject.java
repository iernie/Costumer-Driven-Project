import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

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

/**
 * This is where the class begins
 * 
 * @author ernie
 */
public class PolicyObject implements Serializable
{
	/**
	 * added generated serial id from warning on implementing serializable
	 */
	private static final long serialVersionUID = 5489334832912646160L;
	private HashMap<String, String> entity;
	private ArrayList<Case> statements;
	private Action actionTaken; //the action taken (includes acc/reject, reason, etc)
	private Context context; //the context for the p3p policy- see context.java
	
	/**
	 * This is the constructor
	 * The constructor initializes the variables within the class when you make a new instance of it
	 * 
	 * @author ernie
	 */
	public PolicyObject()
	{
		statements = new ArrayList<Case>();
		entity = new HashMap<String, String>();
		context = new Context(null,null,null);
	}

	public Context getContext() {
		return this.context;
	}
	
	public void setContext(Context theCon) {
		this.context = theCon;
	}
	
	public Action getAction() {
		return this.actionTaken;
	}

	public PolicyObject setAction(Action theRes) {
		this.actionTaken = theRes;
		return this;
	}
	
	/**
	 * Adds a case to the policy
	 * 
	 * @author ernie
	 * @param c input Case
	 */
	public void addCase(Case c)
	{
		statements.add(c);
	}
	
	/**
	 * Adds a data to the entity hashmap of the policy
	 * 
	 * @author ernie
	 * @param key input String
	 * @param value input String
	 */
	public void addEntityData(String key, String value)
	{
		entity.put(key, value);
	}
	
	/**
	 * Returns a specific case for the policy
	 * 
	 * @author ernie
	 * @param i input int
	 * @return Case
	 */
	public Case getCase(int i){
		return statements.get(i);
	}
	
	/**
	 * Returns all cases for the policy
	 * 
	 * @author ernie
	 * @return ArrayList<Case>
	 */
	public ArrayList<Case> getCases(){
		return statements;
	}
	
	/**
	 * Returns the entity for the policy
	 * 
	 * @author ernie
	 * @return HashMap<String, String>
	 */
	public HashMap<String, String> getEntities()
	{
		return entity;
	}
	
	/**
	 * Returns the entity data for a specific key
	 * 
	 * @author ernie
	 * @param key input String
	 * @param String
	 */
	public String getEntity(String key)
	{
		return entity.get(key);
	}
	
	/**
	 * returns domain of policy (URL) as string.
	 * 
	 * @return the domain/url from which the policy came.
	 * @author ngerstle
	 */
	public String getName() {
		return context.getDomain();
	}
	
	/**
	 * this is a debug method and should NOT BE TAKEN SERIOUSLY
	 * 
	 * @author ernie
	 */
	public void debug_print()
	{
		System.out.println("\nPOLICY OBJECT DEBUG PRINT");
		System.out.println("\nENTITY");
		/*Set set = entity.entrySet();
		Iterator it = set.iterator();
		while(it.hasNext())
		{
			Map.Entry me = (Map.Entry)it.next();
			System.out.println("KEY: " + me.getKey() + ", VAL: " + me.getValue() );
		}
		*/
		
		for(String key : entity.keySet())
		{
			System.out.println("KEY: " + key + ", VAL: " + entity.get(key) );
		}
		
		
		for(int i=0; i<statements.size(); i++)
		{
			System.out.println("\nSTATEMENT " + i);
			statements.get(i).debug_print();
		}
	}
}
