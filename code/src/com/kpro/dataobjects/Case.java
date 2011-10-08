package com.kpro.dataobjects;
import java.util.ArrayList;


public class Case {
	
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
	
	public void setDataType(String s)
	{
		datatype = s;
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
	
	public String getDataType()
	{
		return datatype;
	}
	
	// DEBUG
	
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
	}
}