package com.kpro.algorithm;

import java.util.ArrayList; //storage structure for intermediate results
import com.kpro.dataobjects.PolicyObject;
import com.kpro.datastorage.PolicyDatabase;

/**
 * The abstract class for implementing reduction algorithms, like Knearestneighbors.
 * ReductionAlgorithm objects store the database, and reduce the set of polices to
 * only the relevent policies (one or more).
 * May include 'Conclusion'/'Summary' algorithms in the future.
 * 
 * @author ngerstle
 * @version 29.09.11.1
 *
 */
public abstract class ReductionAlgorithm {

	protected PolicyDatabase pdb; //the history we are reducing
	String[] extraArgs = null;
	
	/**
	 * Constructor for a reductionAlgorithm
	 * 
	 * @param pdb
	 * @param extraArgs
	 */
	public ReductionAlgorithm(PolicyDatabase pdb, String[] extraArgs)
	{
		this.pdb = pdb;
		this.extraArgs = extraArgs;
	}

	/**
	 * the reduce call. returns an arraylist of policies in the policydatabase relevent to newPO
	 * 
	 * @param policyDatabase 
	 * @param newPO the new policy to consider- it shouldn't change within the algorithm
	 * @return a modified newpol 
	 * @author ngerstle
	 */
	public abstract ArrayList<PolicyObject> reduce(final PolicyObject newPO);

}