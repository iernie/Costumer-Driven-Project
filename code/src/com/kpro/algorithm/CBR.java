package com.kpro.algorithm;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList; //for moving between reduction and conclusion algorithms 
import java.util.Date;
import java.util.Properties;	//for handling weights

import com.kpro.dataobjects.Action;
import com.kpro.dataobjects.Context;
import com.kpro.dataobjects.PolicyObject;
import com.kpro.datastorage.PolicyDatabase;
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
		
		if(theIO.getNR() == null)
			System.err.println("theIO.getNR == null in CBR.process");
		
		
		if(((a.getConfidence() < theIO.getConfLevel()) || (Double.isNaN(a.getConfidence()))) && (theIO.getNR() != null))
		{
			System.err.println("local confidence="+a.getConfidence());
			Action b = theIO.getNR().reqAct(newPO);
			if(b !=null)
			{
				a=b;
			}
		}
		
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
			if(theIO.getNR()!= null)
			{
				theIO.getNR().saveObj(newpol);//upload to network
			}
			learnAlg.learn(theIO);
		}

	}

	public CBR parse(String string) throws Exception {

		String[] algorithms = string.split(",");

		int k = Integer.parseInt(algorithms[1].split(":")[1]);

		Properties weightsConfig = theIO.loadWeights();
		DistanceMetric dm = getDistanceMetricAlgorithm(algorithms[0], weightsConfig);
		PolicyDatabase pdb = theIO.getPDB();

		ReductionAlgorithm reductionAlgorithm = getReductionAlgorihm(algorithms[1].split(":")[0], dm, pdb, k);
		ConclusionAlgorithm conclusionAlgortihm = getConclusionAlgorihm(algorithms[2], dm);
		LearnAlgorithm learnAlgorithm = getLearnAlgorihm(algorithms[3], weightsConfig);

		return new CBR(theIO, weightsConfig, reductionAlgorithm, conclusionAlgortihm, learnAlgorithm);
	}

	private DistanceMetric getDistanceMetricAlgorithm(String algorithm, Properties weightsConfig) throws ClassNotFoundException {
		try {
			Class<?> cls = Class.forName("com.kpro.algorithm."+algorithm);

			Object[] argsList = new Object[1];
			argsList[0] = weightsConfig;

			return (DistanceMetric) cls.getDeclaredConstructors()[0].newInstance(argsList);
		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundException();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	private ReductionAlgorithm getReductionAlgorihm(String algorithm, DistanceMetric dm, PolicyDatabase pdb, int k) throws ClassNotFoundException {
		try {
			Class<?> cls = Class.forName("com.kpro.algorithm."+algorithm);

			Object[] argsList = new Object[3];
			argsList[0] = dm;
			argsList[1] = pdb;
			argsList[2] = k;

			return (ReductionAlgorithm) cls.getDeclaredConstructors()[0].newInstance(argsList);
		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundException();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	private ConclusionAlgorithm getConclusionAlgorihm(String algorithm, DistanceMetric dm) throws ClassNotFoundException {
		try {
			Class<?> cls = Class.forName("com.kpro.algorithm."+algorithm);

			Object[] argsList = new Object[1];
			argsList[0] = dm;

			return (ConclusionAlgorithm) cls.getDeclaredConstructors()[0].newInstance(argsList);
		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundException();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	private LearnAlgorithm getLearnAlgorihm(String algorithm, Properties weightsConfig) throws ClassNotFoundException {
		try {
			Class<?> cls = Class.forName("com.kpro.algorithm."+algorithm);

			Object[] argsList = new Object[1];
			argsList[0] = weightsConfig;

			return (LearnAlgorithm) cls.getDeclaredConstructors()[0].newInstance(argsList);
		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundException();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}


}
