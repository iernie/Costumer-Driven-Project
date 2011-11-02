package com.kpro.datastorage;

import java.io.FileNotFoundException;
import java.io.PipedInputStream;
import java.io.PrintStream;

import org.lightcouch.CouchDbClient;
import org.lightcouch.Response;

import com.kpro.dataobjects.Action;
import com.kpro.dataobjects.PolicyObject;
import com.google.gson.*;


/**
 * The network resource class for working with a couchDB server cross network at the specified location.
 * 
 * @author ngerstle
 * @version 17.10.11.1
 */
public class NRCouchdb extends NetworkR {

	public final String type = "CouchDB";
	CouchDbClient dbc;	
	
	
	public NRCouchdb(String options) {
		super(options); //should call parseNOptions
		//add extra initialization code (IF NEEDED) here
	}
		
	public Action reqAct(PolicyObject a) {
		System.err.println("Calling uncoded method in NRCouchdb.java: public Action reqAct(PolicyObject A)");
		return null;
	}

	public void saveObj(PolicyObject a){
        dbc.save(a);	
	}


	public void disconnect() {
		dbc.shutdown();
	}


	/**
	 * class specific parser to get CouchDB options from the string.
	 */
	protected void parseNOptions(String options) {
		
		String[] opts = options.split(","); //dbName,newDbIfNone,Protocol,location,port,username,password
		//System.err.println("Authenticating with username [" + opts[5] + "] and password [" + opts[6] +"]" );
		PrintStream a = new PrintStream(System.err);
		try {
			System.setErr(new PrintStream("/dev/null"));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		dbc = new CouchDbClient(opts[0], Boolean.parseBoolean(opts[1]), opts[2], opts[3], Integer.parseInt(opts[4]), opts[5], opts[6]) ;
		
		//System.setErr(a);
		
	}

}
