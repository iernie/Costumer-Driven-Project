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
public class knnReduceAlgorithm  extends ReductionAlgorithm{
	
	private DistanceMetric distanceMetric; // distance metric
	private int k; // number of neighbors 
	
	// Constructors	
	/**
	 * creates a kNearestNeighbors algorithm to use
	 * @param distanceMetric	the class defining distance between objects
	 * @param database	the database of objects to operate on
	 * @param k the size of k
	 * @author ngerstle, ulfnore
	 */
	public knnReduceAlgorithm(DistanceMetric distanceMetric, PolicyDatabase database,int k) {
		super(database);
		this.distanceMetric = distanceMetric;
		this.k=k;
	}

	
	// The algorithm
	
	/**
	 * the method that returns the closest k objects to the parameter.
	 * works by sorting elements by distance from passed object, and passing the first
	 * k elements.
	 * 
	 * @param newPO the new PolicyObject the thing to find the neighbors of
	 * 
	 * @return ArrayList<PolicyObject> an arraylist of size k of the nearest neighbors
	 * @author ngerstle
	 */
	private ArrayList<PolicyObject> reduceKNN(final PolicyObject newPO) 
	{
		ArrayList<PolicyObject> all_pos = new ArrayList<PolicyObject>();
		for(PolicyObject po : pdb)
		{
			all_pos.add(po);
		}
		Collections.sort(all_pos,		
				new Comparator<PolicyObject>() {
					public int compare(PolicyObject o1, PolicyObject o2) 
					{
		
						double a = distanceMetric.getTotalDistance(newPO, o1);
						double b = distanceMetric.getTotalDistance(newPO, o2);
						return (a>b ? -1 : (a==b ? 0 : 1));
					}
				}
		);

		return (ArrayList<PolicyObject>) all_pos.subList(0, k);
	}


	@Override
	public ArrayList<PolicyObject> reduce(PolicyObject newPO) {
		return reduceKNN(newPO);
	}

	
}
