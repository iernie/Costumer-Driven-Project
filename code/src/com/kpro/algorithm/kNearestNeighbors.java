package com.kpro.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.kpro.database.PolicyDatabase;
import com.kpro.dataobjects.PolicyObject;

/**
 * A k-nearest-neighbors algorithm class. create it and call run on it to
 * get the nearest k neighbors to the object passed to run().
 * @author ulfnore, ngerstle
 *
 */
public class kNearestNeighbors {
	
	private DistanceMetric distanceMetric; // distance metric
	private PolicyDatabase database; // privacy object database
	private int k; // number of neighbors 
	
	// Constructors
	/**
	 * generic constructors. don't use
	 * @author ulfnore
	 */
	public kNearestNeighbors(){}
	
	/**
	 * creates a kNearestNeighbors algorithm to use
	 * @param distanceMetric	the class defining distance between objects
	 * @param database	the database of objects to operate on
	 * @param k the size of k
	 * @author ngerstle, ulfnore
	 */
	public kNearestNeighbors(DistanceMetric distanceMetric, PolicyDatabase database,int k) {
		super();
		this.distanceMetric = distanceMetric;
		this.k=k;
	}

	// Getters and setters
	public DistanceMetric getDistanceMetric() 	{ 
		return distanceMetric;	
	}

	public void setDistanceMetric(DistanceMetric distanceMetric) { 
		this.distanceMetric = distanceMetric; 
	}

	public PolicyDatabase getDatabase() {
		return database;
	}

	public void setDatabase(PolicyDatabase database) {
		this.database = database;
	}

	public void setK(int k) {
		this.k = k; 
	}

	public int getK() {
		return k; 
	}

	// The algorithm
	
	/**
	 * the method that returns the closest k objects to the parameter.
	 * works by sorting elements by distance from passed object, and passing the first
	 * k elements.
	 * 
	 * @param policyObject the thing to find the neighbors of
	 * @return ArrayList<PolicyObject> an arraylist of size k of the nearest neighbors
	 * @author ngerstle
	 */
	public ArrayList<PolicyObject> run(final PolicyObject policyObject)
	{
		
		ArrayList<PolicyObject> all_pos = new ArrayList<PolicyObject>();
		for(PolicyObject po : database)
		{
			all_pos.add(po);
		}
		Collections.sort(all_pos,		
				new Comparator<PolicyObject>() {
					public int compare(PolicyObject o1, PolicyObject o2) 
					{
		
						double a = distanceMetric.getTotalDistance(policyObject, o1);
						double b = distanceMetric.getTotalDistance(policyObject, o2);
						return (a>b ? -1 : (a==b ? 0 : 1));
					}
				}
		);

		return (ArrayList<PolicyObject>) all_pos.subList(0, k);
	}

	
}
