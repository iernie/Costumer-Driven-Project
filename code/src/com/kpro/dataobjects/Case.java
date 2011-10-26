package com.kpro.dataobjects;
import java.util.ArrayList;

/**
 * A class that contains a single datatype from a PolicyObject
 * 
 * @author ernie
 *
 */
public class Case implements Comparable{
	
	private ArrayList<Purpose> purpose;
	private ArrayList<Retention> retention;
	private ArrayList<Recipient> recipient;
	private ArrayList<Category> categories;
	private String datatype;
	
	public Case()
	{
		purpose = new ArrayList<Purpose>();
		retention = new ArrayList<Retention>();
		recipient = new ArrayList<Recipient>();
		categories = new ArrayList<Category>();
		datatype = "#";
	}
	
	public Case(ArrayList<Purpose> purpose, ArrayList<Retention> retention, ArrayList<Recipient> recipients, ArrayList<Category> categories, String datatype)
	{
		this.purpose = purpose;
		this.retention = retention;
		this.recipient = recipients;
		this.categories = categories;
		this.datatype = datatype;
	}
	
	// ADD
	
	/**
	 * Adds a purpose to the case
	 * @param p
	 */
	public void addPurpose(Purpose p)
	{
		purpose.add(p);
	}
	
	/**
	 * Adds a retention to the case
	 * @param t
	 */
	public void addRetention(Retention t)
	{
		retention.add(t);
	}
	
	/**
	 * Adds a recipient to the case
	 * @param r
	 */
	public void addRecipient(Recipient r)
	{
		recipient.add(r);
	}
	
	/**
	 * Adds a category to the case
	 * @param c
	 */
	public void addCategory(Category c)
	{
		categories.add(c);
	}
	
	/**
	 * Sets the datatype to the case
	 * @param s
	 */
	public void setDataType(String s)
	{
		datatype = s;
	}
	
	// GET
	/**
	 * Gets the purposes
	 * @return ArrayList<Purpose>
	 */
	public ArrayList<Purpose> getPurposes()
	{
		return purpose;
	}
	
	/**
	 * Returns the ith purpose
	 * @param int i
	 * @return Purpose
	 */
	public Purpose getPurpose(int i)
	{
		return purpose.get(i);
	}
	
	/**
	 * Gets the retentions
	 * @return ArrayList<Retention>
	 */
	public ArrayList<Retention> getRetentions()
	{
		return retention;
	}
	
	/**
	 * Returns the ith retention
	 * @param int i
	 * @return Retention
	 */
	public Retention getRetention(int i)
	{
		return retention.get(i);
	}
	
	/**
	 * Gets the recipients
	 * @return ArrayList<Recipient>
	 */
	public ArrayList<Recipient> getRecipients()
	{
		return recipient;
	}
	
	/**
	 * Returns the ith recipient
	 * @param int i
	 * @return Recipient
	 */
	public Recipient getRecipient(int i)
	{
		return recipient.get(i);
	}
	
	/**
	 * Gets the categories
	 * @return ArrayList<Category>
	 */
	public ArrayList<Category> getCategories()
	{
		return categories;
	}
	
	/**
	 * Returns the ith category
	 * @param int i
	 * @return Category
	 */
	public Category getCategory(int i)
	{
		return categories.get(i);
	}
	
	/**
	 * Returns the datatype
	 * @return String
	 */
	public String getDataType()
	{
		return datatype;
	}
	
	/**
	 * Based on debug.print
	 * @author ulfnore
	 */
	@Override
	public String toString(){
		String str = "\nDATATYPE: " + datatype +"\n";
		
		str += "PURPOSE:\n";
		for(Purpose p: purpose)
			str += "\t " + p + "\n";
		
		str += "RECIPIENTS:\n";
		for(Recipient r: recipient)
			str += "\t" + r + "\n";
		
		str += "RETENTION: \n";
		for(Retention r: retention)
			str += "\t" + r + "\n";
		
		if(datatype.equalsIgnoreCase("#dynamic.miscdata"))
		{
			str += "CATEGORY: \n";
			for(Category c: categories)
				str +="\t"+ c +"\n";
		}
		
		return str;
	}
	
	/**
	 * to allow comparision of cases, primarily for white/blacklisting.
	 * 
	 * @param another object (a case)
	 * @return -1 if this > other, 0 if equal, else 1
	 * @author ngerstle
	 */
	@Override
	public int compareTo(Object o) {
		
		return toString().compareToIgnoreCase(((Case)o).toString());
	}
	
	// DEBUG
	/*
	public void debug_print()
	{	
		System.out.println("DATATYPE: " + datatype);
		
		for(Purpose p: purpose)
		{
			System.out.println("PURPOSE: " + p);
		}
		
		for(Recipient r: recipient)
		{
			System.out.println("RECIPIENT: " + r);
		}
		
		for(Retention r: retention)
		{
			System.out.println("RETENTION: " + r);
		}
		
		if(datatype.equalsIgnoreCase("#dynamic.miscdata"))
		{
			for(Category c: categories)
			{
				System.out.println("CATEGORY: " + c);
			}	
		}
	}*/
}