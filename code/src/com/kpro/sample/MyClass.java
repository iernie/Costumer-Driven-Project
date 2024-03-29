package com.kpro.sample;

import java.io.Serializable;

/**
 * 
 * @author Aman
 * This is the class that implements Serializable and thus its state can be saved 
 * 2 objects for this class are created in SerializationDemo class
 *
 */

class MyClass implements Serializable { 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name; 
	int age; 
	double kuchV; 
	
	/**
	 * This is the constructor used to initialize object values.
	 */
	public MyClass(String s, int i, double d) { 
		this.name = s; 
		this.age = i; 
		this.kuchV = d; 
	} 
	
	/**
	 * This is the over-ridden method to display object variables value as a String. 
	 */
	@Override
	public String toString() { 
		return "s=" + name + "; i=" + age + "; d=" + kuchV; 
	}
}