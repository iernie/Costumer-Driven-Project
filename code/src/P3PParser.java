import java.io.IOException;
import java.util.ArrayList;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class P3PParser
{
	private PolicyObject policy;
	private String tempKey;
	
	/**
	 * Constructor for P3PParser
	 * 
	 * @author ernie
	 */
	public P3PParser()
	{
		policy = new PolicyObject();
	}
		
	class XMLParserHandler extends DefaultHandler
	{
		private ArrayList<Purpose> purpose;
		private ArrayList<Retention> retention;
		private ArrayList<Recipient> recipients;
		private ArrayList<Category> categories;
		
		Boolean statement = false,
				categoriesTag = false,
				dataTag = false,
				entity = false,
				purposeTag = false,
				recipientTag = false,
				retentionTag = false;
		
		/**
		 * Function that parses all of first instances of a tag
		 * 
		 * @author ernie
		 * @param nsURI input String
		 * @param strippedName input String
		 * @param tagName input String
		 * @param attributes input Attributes
		 * @throws SAXException
		 */
	    public void startElement(String nsURI, String strippedName, String tagName, Attributes attributes) throws SAXException
	    {
	    	
	    	//System.out.println("TAG: " + tagName + ", ATTRS: " + attributes.getValue(0));
	    	String formattedTagName = tagName.replace('-', '_').toUpperCase();
	    	
	    	
	    	// IF TAG HAS ALREADY BEEN FOUND, ADD SUBTAG
	    	
	    	if(statement)
	    	{
	    		if(purposeTag)
	    		{
	    			purpose.add(Purpose.valueOf(formattedTagName));
	    		}
	    		if(recipientTag)
	    		{
	    			recipients.add(Recipient.valueOf(formattedTagName));
	    		}
	    		if(retentionTag)
	    		{
	    			retention.add(Retention.valueOf(formattedTagName));
	    		}
	    		if(categoriesTag)
	    		{
	    			categories.add(Category.valueOf(formattedTagName));
	    		}
	    	}
	    	
	    	// IF TAG IS TO BE FOUND, SET IT TO TRUE

	    	if(tagName.equalsIgnoreCase("entity"))
	    	{
	    		entity = true;
	    	}
	    	if(tagName.equalsIgnoreCase("statement"))
	    	{
	    		statement = true;
	    	}
	    	if(tagName.equalsIgnoreCase("data"))
	    	{
	    		tempKey = attributes.getValue(0);
	    		dataTag = true;
	    	}
	    	if(tagName.equalsIgnoreCase("categories"))
	    	{
	    		categoriesTag = true;
	    		categories = new ArrayList<Category>();
	    	}
	    	if(tagName.equalsIgnoreCase("purpose"))
	    	{
	    		purposeTag = true;
	    		purpose = new ArrayList<Purpose>();
	    	}
	    	if(tagName.equalsIgnoreCase("recipient"))
	    	{
	    		recipientTag = true;
	    		recipients = new ArrayList<Recipient>();
	    	}
	    	if(tagName.equalsIgnoreCase("retention"))
	    	{
	    		retentionTag = true;
	    		retention = new ArrayList<Retention>();
	    	}
	    }
	    
	    /**
	     * Function that parses content of a tag
	     * 
	     * @author ernie
	     * @param ch input char
	     * @param start input int
	     * @param length input int
	     * @throws SAXException
	     */
	    public void characters(char ch[], int start, int length) throws SAXException
	    {
	    	if(dataTag && entity)
	    	{
	    		policy.addEntityData(tempKey, new String(ch, start, length));
	    	}
		}
	    
	    /**
	     * Function that parses all of last instances of a tag
	     * 
	     * @author ernie
	     * @param nsURI input String
	     * @param strippedName input String
	     * @param tagName input String
	     * @throws SAXException
	     */
	    public void endElement(String nsURI, String strippedName, String tagName) throws SAXException
	    {
	    	// IF TAG ENDS, SET IT TO FALSE
	    	
	    	if(tagName.equalsIgnoreCase("entity"))
	    	{
	    		entity = false;
	    	}
	    	if(tagName.equalsIgnoreCase("data"))
	    	{
	    		dataTag = false;
	    		if(statement)
	    		{
	    			Case policyCase = new Case(purpose, retention, recipients, categories, tempKey);
		    		policy.addCase(policyCase);
	    		}
	    	}
	    	if(tagName.equalsIgnoreCase("categories"))
	    	{
	    		categoriesTag = false;
	    	}
	    	if(tagName.equalsIgnoreCase("purpose"))
	    	{
	    		purposeTag = false;
	    	}
	    	if(tagName.equalsIgnoreCase("recipient"))
	    	{
	    		recipientTag = false;
	    	}
	    	if(tagName.equalsIgnoreCase("retention"))
	    	{
	    		retentionTag = false;
	    	}
	    	if(tagName.equalsIgnoreCase("statement"))
	    	{
	    		statement = false;
	    	}
	    }
	}
	
	/**
	 * Parses a P3P Policy by URL
	 * 
	 * @author ernie
	 * @param p3p input String
	 * @return Parsed P3P Policy as PolicyObject
	 * @throws Exception
	 */
	public PolicyObject parse(String p3p)
	{
		XMLReader parser;
		try {
			parser = XMLReaderFactory.createXMLReader("org.apache.crimson.parser.XMLReaderImpl");
			parser.setContentHandler(new XMLParserHandler( ));
			parser.parse(p3p);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

    	return policy;
	}
 
	/*
	public static void main(String[] args) {
		
		P3PParser parser = new P3PParser();
		PolicyObject policy = new PolicyObject();
		
        //policy = parser.parse("http://info.yahoo.com/privacy/w3c/p3p_policy.xml");
        policy = parser.parse("http://pages.ebay.com/w3c/p3p-policy.xml#policy");
		//policy = parser.parse("http://www.microsoft.com/w3c/p3policy.xml");
        policy.debug_print();
    }
    */
}
