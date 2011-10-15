package com.kpro.dataobjects;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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
	private ArrayList<Case> cases;
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
		cases = new ArrayList<Case>();
		entity = new HashMap<String, String>();
		context = new Context(null,null,null);
	}

	public Context getContext() {
		return this.context;
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	public Action getAction() {
		return this.actionTaken;
	}

	public PolicyObject setAction(Action action) {
		this.actionTaken = action;
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
		cases.add(c);
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
		return cases.get(i);
	}
	
	/**
	 * Returns all cases for the policy
	 * 
	 * @author ernie
	 * @return ArrayList<Case>
	 */
	public ArrayList<Case> getCases(){
		//must be sorted for the .equalCases method=> sort cases by datatype

		Collections.sort(cases);
		return cases;
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
	public String getContextDomain() {
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
		for(String key : entity.keySet())
		{
			System.out.println("KEY: " + key + ", VAL: " + entity.get(key) );
		}
		
		System.out.println("\nCONTEXT");
		System.out.println(context.getAccessTime());
		
		if(actionTaken != null)
		{
			System.out.println("\nACTION");
			System.out.println(actionTaken.getAccepted());
		}
		
		for(int i=0; i<cases.size(); i++)
		{
			System.out.println("\nCASE " + i);
			cases.get(i).debug_print();
		}
	}
	/**
	 * This is based on the debug_print and SHOULD BE TAKEN SERIOUSLY.
	 * 
	 * @author ulfnore
	 */
	public String toString()
	{
		String str = "";
		str += "Policy Object \n";

		str += "\nENTITY";
		for(String key : entity.keySet())
			str += "\nKEY: " + key + ", VAL: " + entity.get(key);

		
		str += "\nCONTEXT: ";
		str += context.getAccessTime();
		
		if(actionTaken != null)
		{
			str += "\nACTION: ";
			str += actionTaken.getAccepted();
		}
		
		for(int i=0; i<cases.size(); i++)
		{
			str += "\nCASE " + i + cases.get(i);
		}
		
		return str;
	}

	public boolean equalsCases(PolicyObject newpol) {
		if(! getContextDomain().equals(newpol.getContextDomain()))
			return false;
		if(!getCases().equals(newpol.getCases()))
			return false;	
		return true;
	}
}
