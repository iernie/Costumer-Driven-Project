package com.kpro.algorithm;
import com.kpro.dataobjects.Action;
import com.kpro.dataobjects.PolicyObject;



/**
 * abstract class for all conclusion classes (they take the new policy and 
 * a reduction of the history versus new policy), and return an Action.
 * May extend ReductionAlgorithm in the future (return a modified np, instead of
 * an action).
 * May be used instead of a ReductionAlgorithm. call with:
 * Action a = (new ConclusionAlgorithm()).conclude(newpol,theIO.getPDB());
 * 
 * @author ngerstle
 * @version 29.09.11.1
 * 
 */

public abstract class ConclusionAlgorithm {

	//constructor
	/**
	 * ConclustionAlgorithm constructor
	 * @author ngerstle
	 */
	public ConclusionAlgorithm(){}
	
	/**
	 * Provides an action recommendation for np based on the given set of objects
	 * @param np the new policy
	 * @param knearestns a set of relevant policies
	 * @return a recommended Action
	 */
	public abstract Action conclude(PolicyObject np, Iterable<PolicyObject> knearestns);
	
	
	
}
