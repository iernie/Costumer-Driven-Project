package com.kpro.sample;

import java.util.Properties;

import com.kpro.algorithm.DistanceMetric;
import com.kpro.dataobjects.Case;
import com.kpro.dataobjects.PolicyObject;
import com.kpro.dataobjects.Purpose;
import com.kpro.dataobjects.Recipient;
import com.kpro.dataobjects.Retention;


public class distanceMetricTest extends DistanceMetric{

	public distanceMetricTest(Properties weightsConfig) {
		super(weightsConfig);
	}

	public double getDistResip(Case a, Case b) {


		return 0;
	}

	public double getDistReten(Case a, Case b) {
		return 0;
	}

	public double getDistPurpose(Case a, Case b) {
		return 0;
	}

	/**
	 * Just used some random return numbers, should be changed something meaningful later
	 */
	public double getWeigth(Recipient r) {
		switch(r){
		case OURS:
			return 1;
		case DELIVERY:
			return 2;
		case SAME:
			return 3;
		case OTHER_RECIPIENT:
			return 4;
		case UNRELATED:
			return 5;
		case PUBLIC:
			return 6;
			
		}
		return -1;
	}

	public double getWeigth(Retention r) {
		return 0;
	}

	public double getWeigth(Purpose r) {
		return 0;
	}

	@Override
	public double getTotalDistance(PolicyObject a, PolicyObject b) {
		return 0;
	}

	

}
