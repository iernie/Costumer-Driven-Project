import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class XMLParser
{

	class XMLParserHandler extends DefaultHandler
	{
	
		boolean data = false;
		boolean purpose = false;
		boolean recipients = false;
		boolean retension = false;
		
	    public void startElement(String nsURI, String strippedName, String tagName, Attributes attributes) throws SAXException
	    {
	    	if (tagName.equalsIgnoreCase("data"))
	    	{
	    		data = true;
	    	}
	    	if (tagName.equalsIgnoreCase("purpose"))
	    	{
	    		purpose = true;
	    	}
	    	if (tagName.equalsIgnoreCase("recipients"))
	    	{
	    		recipients = true;
	    	}
	    	if (tagName.equalsIgnoreCase("retension"))
	    	{
	    		retension = true;
	    	}
	    }

	    public void characters(char[] ch, int start, int length)
	    {
	    	if (data)
	    	{
	    		System.out.println("Data: " + new String(ch, start, length));
	    		data = false;
	    	} else if(purpose)
	    	{
	    		System.out.println("Purpose: " + new String(ch, start, length));
	    		purpose = false;
	    	} else if(recipients)
	    	{
	    		System.out.println("Recipients: " + new String(ch, start, length));
	    		recipients = false;
	    	} else if(retension)
	    	{
	    		System.out.println("Retension: " + new String(ch, start, length));
	    		retension = false;
	    	}
	    }
	}
	    
	public void load(String xml) throws Exception
	{
		XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.crimson.parser.XMLReaderImpl");
		parser.setContentHandler(new XMLParserHandler( ));
    	parser.parse(xml);
	}
 
}