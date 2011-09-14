import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class P3PParser
{
	private PolicyObject policy;
	private String tempKey;

	class XMLParserHandler extends DefaultHandler
	{
		Boolean dataTag = false,
				entity = false;
		
	    public void startElement(String nsURI, String strippedName, String tagName, Attributes attributes) throws SAXException
	    {
	    	
	    	System.out.println("TAG: " + tagName + ", ATTRS: " + attributes.getValue(0));
	    	if (tagName.equalsIgnoreCase("policies"))
	    	{
	    		policy = new PolicyObject();
	    	}
	    	if(tagName.equalsIgnoreCase("entity"))
	    	{
	    		entity = true;
	    	}
	    	if (tagName.equalsIgnoreCase("data"))
	    	{
	    		tempKey = attributes.getValue(0);
	    		dataTag = true;
	    	}
	    }
	    
	    public void characters(char ch[], int start, int length) throws SAXException
	    {
	    	if(entity && dataTag)
	    	{
	    		policy.addEntityData(tempKey, new String(ch, start, length));
	    		dataTag = false;
	    	}
		}
	    
	    public void endElement(String nsURI, String strippedName, String tagName) throws SAXException
	    {
	    	if(tagName.equalsIgnoreCase("entity"))
	    	{
	    		entity = false;
	    	}
	    }
	}
	    
	public void parse(String p3p) throws Exception
	{
		XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.crimson.parser.XMLReaderImpl");
		parser.setContentHandler(new XMLParserHandler( ));
    	parser.parse(p3p);
	}
	
	public PolicyObject getObject()
	{
		return policy;
	}
 
}
