package com.kpro.algorithm;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * This class contains methods for reading and writing to/from the weights properties class
 * Can be used by LearnAlgorithm to work with the weights
 *  
 * @author Nesha
 *
 */
public class WeightsIO {

	private Properties props;
	private ArrayList<String> keys;//the value of key.get(i) is value.get(i)
	private ArrayList<Integer> values;
	
	/**
	 * constructor that reads from weights.cfg and
	 * makes a list of keys and one a values
	 */
	public WeightsIO(){
		Properties p = new Properties();
		FileInputStream MyInputStream = null;
		try{
			MyInputStream = new FileInputStream("weights.cfg");	
			p.load(MyInputStream);
			MyInputStream.close(); // better in finally block
		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("IOException in WeightsIO.java");
		}	
		
		
		keys = new ArrayList<String>();
		values = new ArrayList<Integer>();
		System.out.println("start");
		for (Entry<Object, Object> propItem : p.entrySet())
		{
		    keys.add((String) propItem.getKey());
		    values.add(Integer.parseInt((String)propItem.getValue()));
		}
		props = p;
	}
	
	/**
	 * 
	 * @return Properties
	 */
	public Properties getProperties(){
        return props;
	}
	
	/**
	 * 
	 * @param Properties
	 */
	public void setProperties(Properties p){
		props = p;
		update();
	}
	
	/**
	 * returns an array of all the keys
	 * @return String[] keys
	 */
	public String[] getKeys(){
		Object[] o = keys.toArray();
		String[] stringKeys = Arrays.copyOf(o, o.length, String[].class);
		return  stringKeys;
	}
	
	/**
	 * returns an array of all the values
	 * @return int[] values
	 */
	public int[] getValues(){
		int[] ret = new int[values.size()];
	    Iterator<Integer> iterator = values.iterator();
	    for (int i=0; i < ret.length; i++)
	    {
	        ret[i] = iterator.next().intValue();
	    }
		return ret;
	}
	
	/**
	 * takes a String key as input and returns the value of that key
	 * @param String key
	 * @return the value of the key, or -1000 if the value don't exists
	 */
	public int getValue(String key){
		if(keys.contains(key))
		{
			return values.get(keys.indexOf(key));
		}
		return -1000;
		
	}
	
	private void update(){
		//TODO write the new values back to file
	}
	
	//used for testing
//	public static void main(String[] args) {
//		WeightsIO w = new WeightsIO();
//		String[] s = w.getKeys();
//		System.out.println(w.getValue(s[2]));
//	}

}
