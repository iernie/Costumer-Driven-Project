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

public class constantLearn extends LearnAlgorithm {

	
	public constantLearn(Properties weightsConfig) {
		super(weightsConfig);
	}



	@Override
	protected Properties applyML(Gio theIO) {
		return weightsConfig;
	}

}
