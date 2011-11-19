package com.kpro.algorithm;

import java.util.ArrayList;

import com.kpro.dataobjects.Action;
import com.kpro.dataobjects.PolicyObject;

/**
 * a very simple conclusion class. result is based on the closest objects only, as determined by the sum of inverse distances
 * of the accepted versus rejected policies. confidences is the ratio of sum inverse distances of the chosen decision, versus the sum
 * of all inverse distances.
 * 
 * @author ngerstle
 * @version 29.09.11.1
 */
public class Conclusion_Simple extends ConclusionAlgorithm {
	
	public Conclusion_Simple(DistanceMetric dm, String[] extraArgs) {
		super(dm, extraArgs);
	}

	/**
	 * makes a decision on the reduced set.
	 * This class creates two lists, one for accepted policies and one for rejected. Assuming there are policies in both 
	 * (easy decision otherwise), whether the policy is accepted or not will depend on the difference between the sum of inverse
	 * distances of the list items (excluding zero-distances), with the smaller sum indicating the more relevent decision.
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