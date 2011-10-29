package com.kpro.algorithm;

import java.util.Properties;

import com.kpro.dataobjects.PolicyObject;

/**
 * Interface of a Distance metric class
 *
 * @author Dimitry Kongevold(dimitryk)
 * 
 */

/**
 * A DistanceMetric interface has to contain 3 methods
 * method for calculation of distance between Recipients, Purposes and Retentions
 * between cases
 * and distance for data-type string
 * @version 160911.1
 * @author dimitryk
 */
public abstract class DistanceMetric {
	
	Properties weightsConfig;
	
	
	
	protected DistanceMetric(Properties weights){
		this.weightsConfig = weights;
	}
	/**
	 * Calculates total distance between two policies
	 * 
	 * @author dimitryk
	 * @param a input PolicyObject
	 * @param b input PolicyObject
	 * @return double 0 if cases are similar and positive integer if they are not
	 */
	public abstract double getTotalDistance(PolicyObject a, PolicyObject b);

}
