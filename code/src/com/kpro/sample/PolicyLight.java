package com.kpro.sample;

import java.io.Serializable;

/**
 * 
 * @author Aman
 * This is the class that implements Serializable and thus its state can be saved 
 * 2 objects for this class are created in SerializationDemo class
 *
 */

@SuppressWarnings("serial")
class PolicyLight implements Serializable { 
	String name;
	String recipient;
	double value;
	int retention;
	
	/**
	 * This is the constructor used to initialize object values.
	 */
	public PolicyLight(String n, String r, double v, int re) { 
		this.name = n;
		this.recipient = r;
		this.value = v;
		this.retention = re; 
	} 
	
	/**
	 * This is the over-ridden method to display object variables value as a String. 
	 */
	public String toString() { 
		return "Name = " + name + ", recipient = " + recipient + ", value = " + value + ", retention = " + retention;
	}
}