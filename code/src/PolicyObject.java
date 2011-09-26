import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
	private HashMap<String, String> entity;
	private ArrayList<Case> statements;

	public PolicyObject()
	{
		statements = new ArrayList<Case>();
		entity = new HashMap<String, String>();
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
		Set set = entity.entrySet();
		Iterator it = set.iterator();
		while(it.hasNext())
		{
			Map.Entry me = (Map.Entry)it.next();
			System.out.println("KEY: " + me.getKey() + ", VAL: " + me.getValue() );
		}
		
		for(int i=0; i<statements.size(); i++)
		{
			System.out.println("\nSTATEMENT " + i);
			statements.get(i).debug_print();
		}
	}
}
