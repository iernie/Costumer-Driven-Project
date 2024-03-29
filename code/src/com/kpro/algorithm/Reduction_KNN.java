package com.kpro.algorithm;

import java.util.ArrayList; //used to store results
import com.kpro.dataobjects.PolicyObject;
import com.kpro.datastorage.PolicyDatabase;

/**
 * A k-nearest-neighbors algorithm class. create it and call run on it to
 * get the nearest k neighbors to the object passed to run().
 * @author ngerstle
 * @version 29.09.11.1
 *
 */
public class Reduction_KNN  extends ReductionAlgorithm{

	private DistanceMetric distanceMetric; // distance metric
	private int k = 1; // number of neighbors, default 1


	/**
	 * creates a kNearestNeighbors algorithm to use
	 * @param distanceMetric	the class defining distance between objects
	 * @param database	the database of objects to operate on
	 * @param extraArgs from the config file
	 * @author ngerstle, ulfnore
	 */
	public Reduction_KNN(DistanceMetric distanceMetric, PolicyDatabase database, String[] extraArgs) {
		super(database, extraArgs);
		this.distanceMetric = distanceMetric;
		this.k = Integer.parseInt(extraArgs[0]);
	}


	/**
	 * the method that returns the closest k objects to the parameter.
	 * works by sorting elements by distance from passed object, and passing the first
	 * k elements.
	 * 
	 * @param newPO the new PolicyObject the thing to find the neighbors of 
	 * @return ArrayList<PolicyObject> an arraylist of size k of the nearest neighbors
	 * @author ngerstle
	 */
	@Override
	public ArrayList<PolicyObject> reduce(final PolicyObject newPO) 
	{
		/* //inefficient- don't need to sort everything
		//Copying pdb.idb should not be unnecessary- remove all_pos and for loop
		ArrayList<PolicyObject> all_pos = new ArrayList<PolicyObject>();
		for(PolicyObject po : pdb)
		{
			all_pos.add(po);
		}
		Collections.sort(all_pos,		
				new Comparator<PolicyObject>() {
					@Override
					public int compare(PolicyObject o1, PolicyObject o2) 
					{		
						double a = distanceMetric.getTotalDistance(newPO, o1);
						double b = distanceMetric.getTotalDistance(newPO, o2);
						return (a<b ? -1 : (a==b ? 0 : 1));
					}
				}
		);
		//TODO ugly hack here
		ArrayList<PolicyObject> results = new ArrayList<PolicyObject>();
		for(PolicyObject i : all_pos.subList(0, k))
			results.add(i);
		*/

		//below is faster, as k is assumed to be << than n, the size of history. thus O(k*n) < O(nlog(n))
		ArrayList<PolicyObject> results = new ArrayList<PolicyObject>();

		for(PolicyObject po : pdb)
		{
			if(results.size() < k)	//fill up the initial list
			{
				results.add(po);
			}
			else	//we have at least k items in our list
			{
				for(PolicyObject i : results) //check the new object against all current suspect results
				{
					if(distanceMetric.getTotalDistance(newPO,i)>distanceMetric.getTotalDistance(newPO,po))
					{//the new object is closer than one of the suspected results, so replace it
						results.remove(i);
						results.add(po);
						break;
					}
				}
			}
		}
		return results;
	}



}
