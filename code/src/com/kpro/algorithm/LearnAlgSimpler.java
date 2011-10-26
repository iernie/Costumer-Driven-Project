package com.kpro.algorithm;

import java.util.Properties;

import com.kpro.dataobjects.Case;
import com.kpro.dataobjects.PolicyObject;
import com.kpro.dataobjects.Purpose;
import com.kpro.dataobjects.Recipient;
import com.kpro.dataobjects.Retention;
import com.kpro.datastorage.PolicyDatabase;
import com.kpro.main.Gio;

/**
 * An learning algorithm that extends LearnAlgorithm
 * 
 * @author Nesha
 *
 */
public class LearnAlgSimpler extends LearnAlgorithm{
	
	private PolicyDatabase pdb;
	
	public LearnAlgSimpler(Properties weightsConfig) {
		super(weightsConfig);
		
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Learns by updating the weights config file
	 * 
	 * @param GIO
	 * @return Properties
	 */
	@Override
	protected Properties applyML(Gio theIO) {
		Properties weights = theIO.getWeights();
		pdb = theIO.getPDB();
		Properties newWeights = new Properties();
		
		for(Object i : weights.keySet()){
				newWeights.setProperty(i.toString(), Double.toString(correlation(i)));
									 
		}
		return newWeights;

	}
	/**
	 * Goes through the PolicyDatabaeses and their cases and checks if 
	 * atleast one of them contains the parameter.
	 * 
	 * @param Objects from GIO.getWeights()
	 * @return double
	 */
	private double correlation(Object i){
		double countyes = 1;
		double countno = 1;
		boolean y = false;
		for(PolicyObject po : pdb.getCollection()){
			//checks if the parameter object is in any of the cases in any of the policyobjects
			for(Case c : po.getCases()){
				if(!y){
					for(Purpose p : c.getPurposes()){
						//String compares the purposes from the config file, and the ones stored in the database
						if(i.toString().substring(8).toLowerCase().equals(p.toString())){
							//if y is set to true we don't need to check further
							y = true;
							break;
						}
					}
				}
				if(!y){
					for(Retention p : c.getRetentions()){
						if(i.toString().substring(10).toLowerCase().equals(p.toString())){
							y = true;
							break;
						}
					}
				}
				if(!y){
					for(Recipient p : c.getRecipients()){
						if(i.toString().substring(11).toLowerCase().equals(p.toString())){
							y = true;
							break;
						}
					}
				}
			}
			
			if(y){
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
		}
		return countyes/(countyes+countno);
	}
	
}
