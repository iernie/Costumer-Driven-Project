package com.kpro.datastorage;


import com.kpro.dataobjects.Action;
import com.kpro.dataobjects.PolicyObject;

/**
 * The network resource class for working with a couchDB server cross network at the specified location.
 * 
 * @author ngerstle
 * @version 17.10.11.1
 */
public class NRCouchdb extends NetworkR {

	public final String type = "CouchDB";
	
	public NRCouchdb(String location) {
		super(location);
	}

	
	public Action reqAct(PolicyObject a) {
		return null;
	}

	public void saveObj(PolicyObject a){

	}


	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

}
