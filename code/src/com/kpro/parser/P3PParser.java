package com.kpro.parser;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import com.kpro.dataobjects.Action;
import com.kpro.dataobjects.Case;
import com.kpro.dataobjects.Category;
import com.kpro.dataobjects.Context;
import com.kpro.dataobjects.PolicyObject;
import com.kpro.dataobjects.Purpose;
import com.kpro.dataobjects.Recipient;
import com.kpro.dataobjects.Retention;

/**
 * Parser that parses a P3P policy and makes it into a PolicyObject
 * 
 * @author ernie
 *
 */
public class P3PParser
{
	private PolicyObject policy;
	private String tempKey;
	
	public P3PParser()
	{
		policy = new PolicyObject();
	}
		
	class XMLParserHandler extends DefaultHandler
	{
		// CASE
		private ArrayList<Purpose> purpose;
		private ArrayList<Retention> retention;
		private ArrayList<Recipient> recipients;
		private ArrayList<Category> categories;
		
		// CONTEXT
		private String url;
		private Date accessTime, creationTime, expiryDate;
		
		// ACTION
		private boolean accepted, userOverride;
		private ArrayList<String> domains;
		
		// TAGS
		private Boolean statementTag = false,
						categoriesTag = false,
						dataTag = false,
						entityTag = false,
						purposeTag = false,
						recipientTag = false,
						retentionTag = false,
						contextTag = false,
						actionTag = false,
						urlTag = false,
						accessTimeTag = false,
						creationTimeTag = false,
						acceptedTag = false,
						domainTag = false,
						userOverrideTag = false;
		
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
	    @Override
		public void startElement(String nsURI, String strippedName, String tagName, Attributes attributes) throws SAXException
	    {
	    	
	    	//System.out.println("TAG: " + tagName + ", ATTRS: " + attributes.getValue(0));
	    	String formattedTagName = tagName.replace('-', '_').toUpperCase();
	    	
	    	
	    	// IF TAG HAS ALREADY BEEN FOUND, ADD SUBTAG
	    	
	    	if(statementTag)
	    	{
	    		if(purposeTag)
	    		{
	    			try {
	    				Purpose p = Purpose.valueOf(formattedTagName);
	    				String attribute = attributes.getValue("required");
	    				if(attribute != null
	    						&& (attribute.equalsIgnoreCase("opt-in") || attributes.getValue(0).equalsIgnoreCase("opt-out"))) {
	    					p.setOptional();
	    				}
	    				purpose.add(p);
		    		} catch(IllegalArgumentException e) {
	    				// SKIPPING
	    			}
	    		}
	    		if(recipientTag)
	    		{
	    			try { 
	    				Recipient r = Recipient.valueOf(formattedTagName);
	    				String attribute = attributes.getValue("required");
	    				if(attribute != null
	    						&& (attributes.getValue(0).equalsIgnoreCase("opt-in") || attributes.getValue(0).equalsIgnoreCase("opt-out"))) {
	    					r.setOptional();
	    				}
	    				recipients.add(r);	    				
	    			} catch(IllegalArgumentException e) {
	    				// SKIPPING
	    			}
	    		}
	    		if(retentionTag)
	    		{
	    			try {
	    				retention.add(Retention.valueOf(formattedTagName));
		    		} catch(IllegalArgumentException e) {
	    				// SKIPPING
	    			}
	    		}
	    		if(categoriesTag)
	    		{
	    			try {
	    				categories.add(Category.valueOf(formattedTagName));
		    		} catch(IllegalArgumentException e) {
	    				// SKIPPING
	    			}
	    		}
	    	}
	    	
	    	// IF TAG IS TO BE FOUND, SET IT TO TRUE
	    	
	    	if(tagName.equalsIgnoreCase("expiry"))
	    	{
	    		String maxAge = attributes.getValue("max-age");
	    		if (maxAge != null) {
	    			long dateInMillis = System.currentTimeMillis() + ((long)Integer.parseInt(maxAge))*1000;
	    			Date d = new Date();  
	    			d.setTime(dateInMillis);
	    			expiryDate = d;
				} else {
					String date = attributes.getValue("date");
					if (date != null) {
						Date d = null;
						try {
							d = DateUtils.parseDate(date);
						} catch (DateParseException e) {
							// Date is malformed
						}
						expiryDate = d;
					}
				}
	    	}
	    	if(tagName.equalsIgnoreCase("entity"))
	    	{
	    		entityTag = true;
	    	}
	    	if(tagName.equalsIgnoreCase("statement"))
	    	{
	    		statementTag = true;
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
	    	if(tagName.equalsIgnoreCase("context"))
	    	{
	    		contextTag = true;
	    	}
	    	if(tagName.equalsIgnoreCase("url"))
	    	{
	    		urlTag = true;
	    	}
	    	if(tagName.equalsIgnoreCase("accesstime"))
	    	{
	    		accessTimeTag = true;
	    	}
	    	if(tagName.equalsIgnoreCase("creationtime"))
	    	{
	    		creationTimeTag = true;
	    	}
	    	if(tagName.equalsIgnoreCase("action"))
	    	{
	    		actionTag = true;
	    	}
	    	if(tagName.equalsIgnoreCase("accepted"))
	    	{
	    		acceptedTag = true;
	    	}
	    	if(tagName.equalsIgnoreCase("reasons"))
	    	{
	    		domains = new ArrayList<String>();
	    	}
	    	if(tagName.equalsIgnoreCase("domain"))
	    	{
	    		domainTag = true;
	    	}
	    	if(tagName.equalsIgnoreCase("useroverride"))
	    	{
	    		userOverrideTag = true;
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
	    @Override
		public void characters(char ch[], int start, int length) throws SAXException
	    {
	    	String content = new String(ch, start, length);
	    	
	    	if(dataTag && entityTag)
	    	{
	    		policy.addEntityData(tempKey, content);
	    	}
	    	
	    	if(contextTag)
	    	{
	    		if(urlTag)
	    		{
	    			url = content;
	    		}
	    		if(accessTimeTag)
	    		{
	    			try {
						accessTime = new SimpleDateFormat("yyyyMMdd").parse(content);
					} catch (ParseException e) {
						e.printStackTrace();
					}
	    		}
	    		if(creationTimeTag)
	    		{
	    			try {
						creationTime = new SimpleDateFormat("yyyyMMdd").parse(content);
					} catch (ParseException e) {
						e.printStackTrace();
					}
	    		}
	    	}
	    	if(actionTag)
	    	{
	    		if(acceptedTag)
	    		{
	    			if(content.equalsIgnoreCase("true"))
	    			{
	    				accepted = true;
	    			} else {
	    				accepted = false;
	    			}
	    		}
	    		if(domainTag)
	    		{
	    			domains.add(content);
	    		}
	    		if(userOverrideTag)
	    		{
	    			if(content.equalsIgnoreCase("true"))
	    			{
	    				userOverride = true;
	    			} else {
	    				userOverride = false;
	    			}
	    		}
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
	    @Override
		public void endElement(String nsURI, String strippedName, String tagName) throws SAXException
	    {
	    	// IF TAG ENDS, SET IT TO FALSE
	    	
	    	if(tagName.equalsIgnoreCase("expiry"))
	    	{
	    		policy.getContext().setExpiryDate(expiryDate);
	    	}
	    	if(tagName.equalsIgnoreCase("entity"))
	    	{
	    		entityTag = false;
	    	}
	    	if(tagName.equalsIgnoreCase("data"))
	    	{
	    		dataTag = false;
	    		if(statementTag)
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
	    		statementTag = false;
	    	}
	    	if(tagName.equalsIgnoreCase("context"))
	    	{
	    		contextTag = false;
	    		Context context = new Context(accessTime, creationTime, url);
	    		policy.setContext(context);
	    	}
	    	if(tagName.equalsIgnoreCase("action"))
	    	{
	    		actionTag = false;
	    		Action action = new Action(accepted, domains, 0, userOverride);
	    		policy.setAction(action);
	    	}
	    	if(tagName.equalsIgnoreCase("url"))
	    	{
	    		urlTag = false;
	    	}
	    	if(tagName.equalsIgnoreCase("accesstime"))
	    	{
	    		accessTimeTag = false;
	    	}
	    	if(tagName.equalsIgnoreCase("creationtime"))
	    	{
	    		creationTimeTag = false;
	    	}
	    	if(tagName.equalsIgnoreCase("accepted"))
	    	{
	    		acceptedTag = false;
	    	}
	    	if(tagName.equalsIgnoreCase("domain"))
	    	{
	    		domainTag = false;
	    	}
	    	if(tagName.equalsIgnoreCase("useroverride"))
	    	{
	    		userOverrideTag = false;
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
 
    
}
