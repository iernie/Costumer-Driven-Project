package com.kpro.algorithm;

import java.util.ArrayList; //for moving between reduction and conclusion algorithms 
import java.util.Date;
import java.util.Properties;	//for handling weights

import com.kpro.database.PolicyDatabase;
import com.kpro.dataobjects.Action;
import com.kpro.dataobjects.Context;
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
	public void run(PolicyObject newpol) {
		ArrayList<PolicyObject> a = theIO.getPDB().getDomain(newpol.getContextDomain());
		if(!a.isEmpty()) //if we've already seen the item
		{
			//theIO.userMessage("This policy has already been "+a.get(0).getAction().getAcceptedStr()+" on " + a.get(0).getContext().getAccessTime()+".\n No action will be taken.");
			if(a.get(0).equalsCases(newpol))
			{
				Context b = a.get(0).getContext();
				b.setAccessTime(new Date(System.currentTimeMillis())); //update access time
				a.get(0).setContext(b);
				newpol = a.get(0);
			}
		}
		else
		{
			newpol =  process(newpol); //knn & conclusion
		}
			newpol = theIO.userResponse(newpol); //user response
			if(newpol != null) //the user made a one time only choice
			{
				theIO.getPDB().addPolicy(newpol); //save to database
				learnAlg.learn(theIO);
			}
		
	}




	public CBR parse(String string) {
		// TODO parse nicely, make new cbr.
		Properties weightsConfig = theIO.loadWeights();
		int k = 1; //size for k in knn algorithm
		//DistanceMetric dm = new distanceMetricTest(weightsConfig);
		DistanceMetric dm = new bitmapDistanceWisOne(weightsConfig);
		//DistanceMetric dm = new bitmapDistance(weightsConfig);
		//DistanceMetric dm = new Bitmapwithdata(weightsConfig);
		PolicyDatabase pdb = theIO.getPDB();
		
		CBR machinelearn = new CBR(theIO,weightsConfig , 
									new Reduction_KNN(dm,pdb,k), 
									new Conclusion_Simple(dm), 
									new Learn_Constant(weightsConfig));
		return machinelearn;
		}


}