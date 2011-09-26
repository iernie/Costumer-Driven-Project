import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class Case {
	
	private ArrayList<Purpose> purpose;
	private ArrayList<Retention> retention;
	private ArrayList<Recipient> recipient;
	private ArrayList<Category> categories;
	private HashSet<String> datatypes;
	
	public Case()
	{
		purpose = new ArrayList<Purpose>();
		retention = new ArrayList<Retention>();
		recipient = new ArrayList<Recipient>();
		categories = new ArrayList<Category>();
		datatypes = new HashSet<String>();
	}
	
	// ADD
	
	public void addPurpose(Purpose p)
	{
		purpose.add(p);
	}
	
	public void addRetention(Retention t)
	{
		retention.add(t);
	}
	
	public void addRecipient(Recipient r)
	{
		recipient.add(r);
	}
	
	public void addCategory(Category c)
	{
		categories.add(c);
	}
	
	public void addDataType(String s)
	{
		datatypes.add(s);
	}
	
	// GET
	
	public ArrayList<Purpose> getPurposes()
	{
		return purpose;
	}
	
	public Purpose getPurpose(int i)
	{
		return purpose.get(i);
	}
	
	public ArrayList<Retention> getRetentions()
	{
		return retention;
	}
	
	public Retention getRetention(int i)
	{
		return retention.get(i);
	}
	
	public ArrayList<Recipient> getRecipients()
	{
		return recipient;
	}
	
	public Recipient getRecipient(int i)
	{
		return recipient.get(i);
	}
	
	public ArrayList<Category> getCategories()
	{
		return categories;
	}
	
	public Category getCategory(int i)
	{
		return categories.get(i);
	}
	
	public HashSet<String> getDataTypes()
	{
		return datatypes;
	}
	
	// DEBUG
	
	public void debug_print()
	{	
		for(int i=0; i < purpose.size(); i++)
		{
			System.out.println("PURPOSE: " + purpose.get(i));
		}
		
		for(int i=0; i < recipient.size(); i++)
		{
			System.out.println("RECIPIENT: " + recipient.get(i));
		}
		
		for(int i=0; i < retention.size(); i++)
		{
			System.out.println("RETENTION: " + retention.get(i));
		}
		
		for(int i=0; i < categories.size(); i++)
		{
			System.out.println("CATEGORIES: " + categories.get(i));
		}
		
		Iterator it1 = datatypes.iterator();
	    System.out.println("DATATYPES");
	    while(it1.hasNext())
	    {
	    	System.out.println(it1.next());	    	
	    }
	}
}