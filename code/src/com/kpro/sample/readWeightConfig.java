package com.kpro.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * A class that reads the weights from the weights.cfg file, and returns 
 * an array with values.
 * The returned array have values that are arranged so that they correspond to the 
 * values as they are written in the config file.
 * That means that if the first uncommented line in the config file 
 * is Recipient.OUR, then the first value in the returned array
 * from the recipientWeight() method would be the value for Recipient.OUR. 
 * 
 * @author Nesha
 *
 */
public class readWeightConfig {
	
	private File file;
	
	/**
	 * reads the weights of the recipients 
	 * and returns them as a list
	 * 
	 * @author Nesha
	 * 
	 * @return an array of the weights of the recipients
	 */
	public int[] recipientWeight(){
		String fileLocation= " ";// the location of the file as a String
		file = new File(fileLocation, "weights.cfg");
		//System.out.println(file.exists());
		int c;
		BufferedReader buffR;
		int[] recipientWeights = new int[6];//recipient have 6 enum types
		try{
			buffR = new BufferedReader(new FileReader(file));
			String line;
			c=0;
			while((line = buffR.readLine()) != null){
				if(line.charAt(0)!='#'){	//lines that starts with # are assumed to be comments 
					if(line.toString().startsWith("Recipient")){
						String s = line.substring(line.indexOf("="),line.indexOf(line.length()-1)); 
						recipientWeights[c] = Integer.parseInt(s);
						c++;
					}
				}
			}
			
		}
		catch(IOException e){
			System.out.println("something wrong with the filepath to the weights config file");
			e.printStackTrace();
		}
		return recipientWeights;
	}
	
	
	/**
	 * reads the weights of the retentions 
	 * and returns them as a list
	 * 
	 * @author Nesha
	 * 
	 * @return an array of the weights of the retentions
	 */
	public int[] retentionWeight(){
		String fileLocation= " ";// the location of the file as a String
		file = new File(fileLocation, "weights.cfg");
		//System.out.println(file.exists());
		int c;
		BufferedReader buffR;
		int[] retentionWeights = new int[5];//retention have 5 enum types
		try{
			buffR = new BufferedReader(new FileReader(file));
			String line;
			c=0;
			while((line = buffR.readLine()) != null){
				if(line.charAt(0)!='#'){	//lines that starts with # are assumed to be comments 
					if(line.toString().startsWith("Retention")){
						String s = line.substring(line.indexOf("=")+1); 
						retentionWeights[c] = Integer.parseInt(s);
						c++;
					}
				}
			}
			
		}
		catch(IOException e){
			System.out.println("something wrong with the filepath to the weights config file");
			e.printStackTrace();
		}
		return retentionWeights;
	}
	

	/**
	 * reads the weights of the purposes 
	 * and returns them as a list
	 * 
	 * @author Nesha
	 * 
	 * @return an array of the weights of the purposes
	 */
	public int[] purposeWeight(){
		String fileLocation= " ";// the location of the file as a String
		file = new File(fileLocation, "weights.cfg");
		//System.out.println(file.exists());
		int c;
		BufferedReader buffR;
		int[] purposeWeights = new int[12];//purpose have 12 enum types
		try{
			buffR = new BufferedReader(new FileReader(file));
			String line;
			c=0;
			while((line = buffR.readLine()) != null){
				if(line.charAt(0)!='#'){	//lines that starts with # are assumed to be comments 
					if(line.toString().startsWith("Purpose")){
						String s = line.substring(line.indexOf("=")+1); 
						purposeWeights[c] = Integer.parseInt(s.trim());
						c++;
					}
				}
			}
			
		}
		catch(IOException e){
			System.out.println("something wrong with the filepath to the weights config file");
			e.printStackTrace();
		}
		return purposeWeights;
	}
	

	
	//used for testing:
//	public static void main(String[] args) {
//		purposeWeight();
//		
//	}
	
}
