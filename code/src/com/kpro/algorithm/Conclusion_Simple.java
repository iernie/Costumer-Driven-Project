package com.kpro.algorithm;

import java.util.ArrayList;

import com.kpro.dataobjects.Action;
import com.kpro.dataobjects.PolicyObject;

/**
 * a very simple conclusion class. result is based on the closest object only.
 * 
 * @author ngerstle
 * @version 29.09.11.1
 */
public class Conclusion_Simple extends ConclusionAlgorithm {

	private DistanceMetric distanceMetric; // distance metric to use for choosing


	/**
	 * a simple conclusion that just bases the result on the inverse distance of the policies,
	 * and returns as a confidence the sum of inverse distances
	 * 
	 * @param knearestns
	 * @return the action to take
	 */
	public Conclusion_Simple(DistanceMetric distanceMetric)
	{
		this.distanceMetric = distanceMetric;
	}

	/**
	 * makes a decision on the reduced set
	 * 
	 * @author ngerstle
	 * 
	 * @param np the object under consideration
	 * @param releventSet the reduced set of neighbors
	 * @return an arraylist of {Action a, double Confidence) 
	 *
	 */
	@Override
	public Action conclude(PolicyObject np, Iterable<PolicyObject> releventSet)
	{
		ArrayList<String> approveList = new ArrayList<String>();
		ArrayList<String> rejectList = new ArrayList<String>();
		double appdistance = 0; //the sum of the inverse distance to all approved PolicyObjects
		double rejdistance = 0; //the sum of the inverse distance to all rejected PolicyObjects

		for( PolicyObject i : releventSet)
		{
			if(i.getAction() != null)
			{
				double d =distanceMetric.getTotalDistance(np, i); //just incase this is zero...
				if(i.getAction().getAccepted())
				{
					approveList.add(i.getContextDomain());

					appdistance+=(d==0)?(0):(1/d);
				}
				else
				{
					rejectList.add(i.getContextDomain());
					rejdistance+=(d==0)?(0):(1/d);				
				}
			}
		}

		
//		System.err.print("approveList \t"+approveList+"\t\t confidence \t"+ (appdistance/(appdistance+rejdistance)));
//		System.err.print("rejectList \t"+rejectList+"\t\t confidence \t"+ (rejdistance/(appdistance+rejdistance)) );
//		System.err.print("\t\t appdistance: "+appdistance +"\trejdistance"+rejdistance);
		if(approveList.isEmpty() && rejectList.isEmpty())
		{
			System.err.println("c=0");
			return new Action(false, approveList, 0, false);
		}
		else if(approveList.isEmpty())
		{
			return new Action(false, rejectList, 1, false);
		}
		else if(rejectList.isEmpty())
		{
			return new Action(true, approveList, 1, false);
		}
		else
		{
			if(appdistance > rejdistance) 
			{
				return new Action(true, approveList, (appdistance/(appdistance+rejdistance)), false);
			}
			else 
			{
				return new Action(false, rejectList, (rejdistance/(appdistance+rejdistance)), false);
			}
		}
	}

}
