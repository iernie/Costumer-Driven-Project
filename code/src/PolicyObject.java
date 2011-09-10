import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PolicyObject
{
	
	HashMap entity;
	
	public PolicyObject()
	{
		entity = new HashMap();
	}
	
	public void addEntityData(String key, String value)
	{
		entity.put(key, value);
	}
	
	public void iterate()
	{
		Set set = entity.entrySet();
		
		Iterator it = set.iterator();
		while(it.hasNext())
		{
			Map.Entry me = (Map.Entry)it.next();
			System.out.println("Key: " + me.getKey() + ", Value: " + me.getValue() );
		}
	}
}
