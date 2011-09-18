import java.io.IOException;

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
		Boolean statement = false,
				categories = false,
				dataTag = false,
				entity = false,
				purpose = false,
				recipient = false,
				retention = false;
		
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
	    	
	    	System.out.println("TAG: " + tagName + ", ATTRS: " + attributes.getValue(0));
	    	String formattedTagName = tagName.replace('-', '_').toUpperCase();
	    	
	    	// IF TAG HAS ALREADY BEEN FOUND, ADD SUBTAG
	    	
	    	if(statement)
	    	{
	    		if(purpose)
	    		{
	    			policy.addPurpose(Purpose.valueOf(formattedTagName));
	    		}
	    		if(recipient)
	    		{
	    			policy.addRecipient(Recipient.valueOf(formattedTagName));
	    		}
	    		if(retention)
	    		{
	    			policy.addRetention(Retention.valueOf(formattedTagName));
	    		}	
	    		if(categories)
	    		{
	    			policy.addCategory(Category.valueOf(formattedTagName));
	    		}
	    	}
	    	
	    	// IF TAG IS TO BE FOUND, SET IT TO TRUE

	    	if(tagName.equalsIgnoreCase("entity"))
	    	{
	    		entity = true;
	    	}
	    	if(tagName.equalsIgnoreCase("data"))
	    	{
	    		tempKey = attributes.getValue(0);
	    		dataTag = true;
	    	}
	    	if(tagName.equalsIgnoreCase("categories"))
	    	{
	    		categories = true;
	    	}
	    	if(tagName.equalsIgnoreCase("purpose"))
	    	{
	    		purpose = true;
	    	}
	    	if(tagName.equalsIgnoreCase("recipient"))
	    	{
	    		recipient = true;
	    	}
	    	if(tagName.equalsIgnoreCase("retention"))
	    	{
	    		retention = true;
	    	}
	    	if(tagName.equalsIgnoreCase("statement"))
	    	{
	    		statement = true;
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
	    	if(entity && dataTag)
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
	    	}
	    	if(tagName.equalsIgnoreCase("categories"))
	    	{
	    		categories = false;
	    	}
	    	if(tagName.equalsIgnoreCase("purpose"))
	    	{
	    		purpose = false;
	    	}
	    	if(tagName.equalsIgnoreCase("recipient"))
	    	{
	    		recipient = false;
	    	}
	    	if(tagName.equalsIgnoreCase("retention"))
	    	{
	    		retention = false;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return policy;
	}
 
}
