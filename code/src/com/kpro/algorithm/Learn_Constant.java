package com.kpro.algorithm;

import java.util.Properties;

import com.kpro.main.Gio;

/**
 * The simplest implementation of learnAlgorithm, this literally
 * does nothing, and thus doesn't actually learn.
 * 
 * @author ngerstle
 * @version 29.09.11.1
 */

public class Learn_Constant extends LearnAlgorithm {

	/**
	 * constructor
	 *
	 * @param weightsConfig the weights configuration file
	 * @param extraArgs from the config file
	 * @author ngerstle, ernie
	 */

	public Learn_Constant(Properties weightsConfig, String[] extraArgs) {
		super(weightsConfig, extraArgs);
	}


	/**
	 * the code that reviews and alters the properties file.
	 * In this case, we make no changes.
	 * 
	 * 
	 * @param theIO   an indirect reference to the policy history
	 * @return a modified properties file
	 * @author ngerstle
	 * 
	 */
	@Override
	protected Properties applyML(Gio theIO) {
		System.out.println("cooonstant");
		return weightsConfig;
	}

}
