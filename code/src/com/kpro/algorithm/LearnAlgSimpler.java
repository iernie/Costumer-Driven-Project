package com.kpro.algorithm;

import java.util.Properties;
import com.kpro.database.PolicyDatabase;
import com.kpro.dataobjects.PolicyObject;
import com.kpro.main.Gio;

public class LearnAlgSimpler extends LearnAlgorithm{

	public LearnAlgSimpler(Properties weightsConfig) {
		super(weightsConfig);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Properties applyML(Gio theIO) {
		Properties weights = theIO.getWeights();
		PolicyDatabase pdb = theIO.getPDB();
		Properties newWeights = new Properties();
		for(Object i : weights.entrySet()){
			for(PolicyObject po : pdb.getCollection()){
				newWeights.setProperty(i.toString(), Double.toString(correlation(po, i.toString())));
			}
		}
		return newWeights;
	}


	private double correlation(PolicyObject po, String i){
		int countyes = 0;
		int countno = 0;
		if(po.getEntity(i)!=null){
			if(po.getAction().getAccepted()){
				countyes ++;
			}
			else{
				countno ++;
			}
		}
		return countyes/(countyes+countno);
	}
}
