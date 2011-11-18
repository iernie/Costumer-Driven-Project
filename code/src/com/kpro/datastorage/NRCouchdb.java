package com.kpro.datastorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;
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

	public final String type = "CouchDB"; /** Type of database (one per class*/
	CouchDbClient dbc;	 /**Couchlight object- holds connection to database*/
	public final String LVquerybase = "/_design/Couchdb/_list/test/testing?obj="; /**The view, and the list to pass through when requesting: append jsonated object as key/value*/
	
	public NRCouchdb(String options) {
		super(options); //should call parseNOptions
	}
		
	public Action reqAct(PolicyObject a) {
		
		Response r = (dbc.save(a)); //send policy to database to allow distance comparison- can't send as part of url, :(
		//TODO insecure (anybody can save/delete[/save]). make note, and propose better way
		
		String query = dbc.getDBUri() + LVquerybase + r.getId();
		String result = "";
		try {
		    URL url = new URL(query);
		    // Read all the text returned by the server
		    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		    String str;
		    while ((str = in.readLine()) != null) {
		        // str is one line of text; readLine() strips the newline character(s)
		    	result+= str;
		    }
		    in.close();
		} catch (MalformedURLException e) {
			System.err.println("malformed url exception in nrcouchdb");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IOException when requesting an action from server");
			e.printStackTrace();
		}

		dbc.remove(r.getId(),r.getRev()); 	//delete the doc now that we've gotten the results
		Action b = parseReqResponse(result); //parse the results to an action
		return b;
		
	}

	/**
	 * This method accepts a json string and parses it to an Action. 
	 * Required due to complications in decoding dynamically sized objects (eg, arraylist<string>).
	 * 
	 * @param result an Action in json form
	 * @return the action described by the json string
	 */
	private Action parseReqResponse(String result) {
		
		
		JsonObject json = (new JsonParser()).parse(result).getAsJsonObject();
		Action b =(new Gson()).fromJson(result, Action.class);
		JsonArray array = (json.get("reasonDomains")).getAsJsonArray();
		    
		b.setAccepted(json.get("accepted").getAsBoolean());
		b.setConfidence(json.get("confidence").getAsDouble());
		b.setOverride(json.get("override").getAsBoolean());
		
		ArrayList<String> reas = new ArrayList<String>();
		for(JsonElement i : array)
		{
			reas.add(i.getAsString());
		}
		b.setReason(reas);
		return b;
	}

	/**
	 * Saves the policy object to the database
	 * 
	 * @param a the PolicyObject to save
	 * @return void
	 */
	public void saveObj(PolicyObject a){
        dbc.save(a);	
	}

	/**
	 * closes/deletes any remaing resources
	 * 
	 * @return void
	 */
	public void disconnect() {
		dbc.shutdown();
	}


	/**
	 * class specific parser to get CouchDB options from the string.
	 * called by super() constructor. parses the setup string into the appropriate options.
	 * 
	 * @param options couchdb options- see blank_config.txt for example, but in form database,false,protocol,host,port,username,password
	 */
	protected void parseNOptions(String options) {
		
		String[] opts = options.split(","); //dbName,newDbIfNone,Protocol,location,port,username,password

		//ATTEMPT AT HIDING LIBRARY ERRSTREAM MESSAGES (don't know why they show up).
		//System.err.println("Authenticating with username [" + opts[5] + "] and password [" + opts[6] +"]" );
//		PrintStream a = new PrintStream(System.err);
//		try {
//			System.setErr(new PrintStream("/dev/null"));
//		} catch (FileNotFoundException e) {
//
//			e.printStackTrace();
//		}
		dbc = new CouchDbClient(opts[0], Boolean.parseBoolean(opts[1]), opts[2], opts[3], Integer.parseInt(opts[4]), opts[5], opts[6]) ;
		
		//System.setErr(a);
		
	}

}
