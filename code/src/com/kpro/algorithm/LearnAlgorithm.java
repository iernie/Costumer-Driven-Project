package com.kpro.algorithm;

import java.util.Properties;  //this is what the algorithm handles

import com.kpro.main.Gio;

/**
 * An abstract class covering all learning algorithms. The learning algorithm
 * alters the weights configuration after examining the current database after
 * the addition of a new policy.
 * 
 * @version 29.09.11
 * @author ngerstle
 *
 */
public abstract class LearnAlgorithm {
	Properties weightsConfig; //stores the initial weights. 
	/*while this is load early on, the 'learn' method is the only thing
	 * to change the values, and should only be called immediatly before shutdown. 
	 */
	
	/**
	 * Constructor for a learning algorithm. accepts a weights configuration file.
	 * 
	 * @param weightsConfig  the weights configuration file
	 * @author ngerstle
	 */
	public LearnAlgorithm(Properties weightsConfig)
	{
		this.weightsConfig = weightsConfig;
	
	}
	
	/**
	 * runs the learning algorithm, and puts the results in the newWeight buffer in theIO(Gio)
	 * 
	 * @param theIO
	 * @author ngerstle
	 */
	public void learn(Gio theIO)
	{
		theIO.setWeights(applyML(theIO));
		
	}
	
	/**
	 * The place to implement the actual algorithm for learning.
	 * 
	 * @param theIO the current database, via Gio
	 * @return a modified properties, such that it more accurately reflects the relationships between policies
	 * @author ngerstle
	 */
	protected abstract Properties applyML(Gio theIO);
	
}
