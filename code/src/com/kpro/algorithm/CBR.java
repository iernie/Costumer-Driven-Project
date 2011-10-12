package com.kpro.algorithm;

import java.util.ArrayList; //for moving between reduction and conclusion algorithms 
import java.util.Properties;	//for handling weights

import com.kpro.database.PolicyDatabase;
import com.kpro.dataobjects.Action;
import com.kpro.dataobjects.PolicyObject;
import com.kpro.main.Gio;

/**
 * Case based reason. This is a working CBR class that handles process flow between init and shutdown.
 * Should be easy to extend and overload various features, but should work for most cases as is.
 * 
 * @author ngerstle
 * @version 29.09.11.1
 */
public class CBR {

	protected PolicyObject newpol; 			//the new policy to look at
	protected Gio theIO;					//interacting with the outside world
	protected Properties weightsConfig; 	//the weights for distance metric and learning

	protected ReductionAlgorithm reduceAlg;		//the reduction algorithm to use
	protected ConclusionAlgorithm conclusAlg;	//the conclusion algorithm to use
	protected LearnAlgorithm learnAlg;			//the learning algorithm to use
	
	
	/**
	 * Our generic constructor.
	 * 
	 * @param theIO
	 * @param weightsConfig
	 * @param reduceAlg
	 * @param conclusAlg
	 * @param learnAlg
	 * @author ngerstle
	 */
	public CBR(Gio theIO, Properties weightsConfig,
			ReductionAlgorithm reduceAlg, ConclusionAlgorithm conclusAlg, LearnAlgorithm learnAlg) {
		this.theIO = theIO;
		this.weightsConfig = weightsConfig;
		this.reduceAlg = reduceAlg;
		this.conclusAlg = conclusAlg;
		this.learnAlg = learnAlg;
	}
	
	/**
	 * constructor that so we can call cbr.parse(string)
	 * 
	 * @param the io class/object/mess
	 * @author ngerstle
	 */
	public CBR(Gio theIO)
	{
		this.theIO =theIO;
	}




	/**
	 * Accepts a parsed PolicyObject that needs a action attached to it, and returns
	 * with the same object with an action in it.
	 *
	 * @param newPO the new policy to be processed
	 * @return the same policy object with an action
	 * 
	 * @author ngerstle
	 */
	private PolicyObject process(PolicyObject newPO) {
		ArrayList<PolicyObject> reducedSet = reduceAlg.reduce(newPO);
		Action a = conclusAlg.conclude(newPO,reducedSet);
		newPO.setAction(a);
		return newPO;
	}

	/**
	 * runs through CBR with selected algorithms
	 * 
	 * @param newPolicy
	 * @author ngerstle
	 */
	public void run(PolicyObject newPolicy) {
		newpol =  process(newPolicy); //knn & conclusion
		newpol = theIO.userResponse(newpol); //user response
		theIO.getPDB().addPolicy(newpol); //save to database
		learnAlg.learn(theIO);
	}




	public CBR parse(String string) {
		// TODO parse nicely, make new cbr.
		Properties weightsConfig = theIO.loadWeights();
		int k = 1; //size for k in knn algorithm
		DistanceMetric dm = new distanceMetricTest(weightsConfig);
		PolicyDatabase pdb = theIO.getPDB();
		
		CBR machinelearn = new CBR(theIO,weightsConfig , 
									new Reduction_KNN(dm,pdb,k), 
									new Conclusion_Simple(dm), 
									new Learn_Constant(weightsConfig));
		return machinelearn;
		}


}