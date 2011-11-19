package com.kpro.algorithm;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList; //for moving between reduction and conclusion algorithms 
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
	 * @param theIO    the GIO instance
	 * @param weightsConfig		the weights to use for distance metric & learning algorithm
	 * @param reduceAlg	the retrival algorithm- produce relevent cases from history
	 * @param conclusAlg	the 'reuse' algorithm- produces a solution from relevant cases
	 * @param learnAlg	the retain algorithm- modifies the 'weightsConfig' so the distance metric is more accurate
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

/**
	*	Parses the CBR option to create the class and instantiate correct algorithms.
	*
	*	@param string the string from either configuration file or commandline
	*	@return the CBR defined by the input string
	*/
	public CBR parse(String string) throws Exception {
		//TODO change the parse so as to allow instantiated classes to know how to parse their own objects
		String[] algorithms = string.split(",");

		Properties weightsConfig = theIO.loadWeights();
		DistanceMetric dm = getDistanceMetricAlgorithm(algorithms[0], weightsConfig);
		PolicyDatabase pdb = theIO.getPDB();

		ReductionAlgorithm reductionAlgorithm = getReductionAlgorithm(algorithms[1],dm,pdb);
		ConclusionAlgorithm conclusionAlgortihm = getConclusionAlgorihm(algorithms[2], dm);
		LearnAlgorithm learnAlgorithm = getLearnAlgorihm(algorithms[3], weightsConfig);

		return new CBR(theIO, weightsConfig, reductionAlgorithm, conclusionAlgortihm, learnAlgorithm);
	}

	/**
	*	Parse the distance metric substring into a DistanceMetric object with the right attributes and values.
	*
	*	@param algorithm the string specifying which distancemetric algorithm to use
	*	@param weightsConfig the weightsConfiguration file (load this from Gio)
	*	@return the DistanceMetric class specified
	*/
	private DistanceMetric getDistanceMetricAlgorithm(String algorithm, Properties weightsConfig) throws ClassNotFoundException {
		try {
			String[] params = algorithm.split(":");
			Class<?> cls = Class.forName("com.kpro.algorithm."+params[0]);

			Object[] argsList = new Object[2];
			argsList[0] = weightsConfig;
			if (params.length > 1) {
				List<String> list = new ArrayList<String>(Arrays.asList(params));
				list.removeAll(Arrays.asList(params[0]));
				argsList[1] = list.toArray(params);
			} else {
				argsList[1] = null;
			}

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

	/**
	*	Parse the reduction algorithm substring into a ReductionAlgorithm object with the right attributes and values.
	*
	*	@param algorithm the string specifying which reduction algorithm to use and number of relevant policies to include in format "algorithmName:k" where k is an integer
	*	@param dm the distance metric to use 
	*	@param pdb the policy database to run on
	*	@return the ReductionAlgorithm class specified
	*/
	private ReductionAlgorithm getReductionAlgorithm(String algorithm, DistanceMetric dm, PolicyDatabase pdb) throws ClassNotFoundException {
		try {
			String[] params = algorithm.split(":");
			Class<?> cls = Class.forName("com.kpro.algorithm."+params[0]);

			Object[] argsList = new Object[3];
			argsList[0] = dm;
			argsList[1] = pdb;
			if (params.length > 1) {
				List<String> list = new ArrayList<String>(Arrays.asList(params));
				list.removeAll(Arrays.asList(params[0]));
				argsList[2] = list.toArray(params);
			} else {
				argsList[2] = null;
			}

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

	/**
	*	Parse the conclusion algorithm substring into a ConclusionAlgorithm obejct with the right attributes and values.
	*
	*	@param algorithm the string specifying which conclusion algorithm to use
	*	@param dm the distance metric to use 
	*	@return the ConclusionAlgorithm class specified
	*/
	private ConclusionAlgorithm getConclusionAlgorihm(String algorithm, DistanceMetric dm) throws ClassNotFoundException {
		try {
			String[] params = algorithm.split(":");
			Class<?> cls = Class.forName("com.kpro.algorithm."+params[0]);

			Object[] argsList = new Object[2];
			argsList[0] = dm;
			if (params.length > 1) {
				List<String> list = new ArrayList<String>(Arrays.asList(params));
				list.removeAll(Arrays.asList(params[0]));
				argsList[1] = list.toArray(params);
			} else {
				argsList[1] = null;
			}

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

	/**
	*	Parse the learning algorithm substring into a LearningAlgorithm obejct with the right attributes and values.
	*
	*	@param algorithm the string specifying which learning algorithm to use
	*	@param weightsConfig the weightsConfiguration file (load this from Gio)
	*	@return the LearningAlgorithm class specified
	*/
	private LearnAlgorithm getLearnAlgorihm(String algorithm, Properties weightsConfig) throws ClassNotFoundException {
		try {
			String[] params = algorithm.split(":");
			Class<?> cls = Class.forName("com.kpro.algorithm."+params[0]);

			Object[] argsList = new Object[2];
			argsList[0] = weightsConfig;
			if (params.length > 1) {
				List<String> list = new ArrayList<String>(Arrays.asList(params));
				list.removeAll(Arrays.asList(params[0]));
				argsList[1] = list.toArray(params);
			} else {
				argsList[1] = null;
			}

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
