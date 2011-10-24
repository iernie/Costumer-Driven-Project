package com.kpro.algorithm;

import java.util.Properties;

import com.kpro.dataobjects.PolicyObject;
import com.kpro.datastorage.PolicyDatabase;
import com.kpro.main.Gio;

public class LearnAlgSimpler extends LearnAlgorithm{
	
	private PolicyDatabase pdb;
	
	public LearnAlgSimpler(Properties weightsConfig) {
		super(weightsConfig);
		
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	protected Properties applyML(Gio theIO) {
		
		
		Properties weights = theIO.getWeights();
		pdb = theIO.getPDB();
		Properties newWeights = new Properties();
		
		for(Object i : weights.keySet()){
				newWeights.setProperty(i.toString(), Double.toString(correlation(i)*
									   Double.parseDouble(weights.get(i).toString())));
		}
		return newWeights;

	}
	private double correlation(Object i){
		double countyes = 1;
		double countno = 1;
		
		for(PolicyObject po : pdb.getCollection()){
	
			if(po.getAction() == null){
				//System.out.println("PolicyObject.getAction == null in LearnAlgSimpler");
			}
			else{
				if(po.getAction().getAccepted()){
					countyes ++;
				}
				else{
					countno ++;
				}
			}
		}
		return countyes/(countyes+countno);
	}
	
}
