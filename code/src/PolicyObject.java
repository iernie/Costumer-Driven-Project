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

public class PolicyObject implements Serializable
{
	/**
	 * added generated serial id from warning on implementing serializable
	 */
	private static final long serialVersionUID = 5489334832912646160L;
	private HashMap<String, String> entity;
	private ArrayList<Case> statements;
	private Action theRes;	//the action taken (includes acc/reject, reason, etc)
	private Context theCon; //the context for the p3p policy- see context.java
	
	
	public PolicyObject()
	{
		statements = new ArrayList<Case>();
		entity = new HashMap<String, String>();
		theCon = new Context(null,null,null);
	}
	
	public Context getContext() {
		return this.theCon;
	}
	
	public void setContext(Context theCon) {
		this.theCon = theCon;
	}
	
	public Action getAction() {
		return this.theRes;
	}

	public void setAction(Action theRes) {
		this.theRes = theRes;
	}
	
	public void addCase(Case c)
	{
		statements.add(c);
	}
	
	public void addEntityData(String key, String value)
	{
		entity.put(key, value);
	}
	
	public Case getCase(int i){
		return statements.get(i);
	}
	
	public ArrayList<Case> getCase(){
		return statements;
	}
	
	public HashMap<String, String> getEntities()
	{
		return entity;
	}
	
	public String getEntity(String key)
	{
		return entity.get(key);
	}
	
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

	public String getName() {
		return theCon.getDomain();
	}
}
