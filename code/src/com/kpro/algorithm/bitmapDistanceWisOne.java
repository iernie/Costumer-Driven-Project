package com.kpro.algorithm;
/**
 * A distance metric that calculates distance based on 
 * weighted union of a bit map interception (all weights are one).
 *
 * @author Dimitry Kongevold(dimitryk)
 * 
 */


import java.util.ArrayList;
import java.util.Properties;

import com.kpro.dataobjects.Case;
import com.kpro.dataobjects.PolicyObject;
import com.kpro.dataobjects.Purpose;
import com.kpro.dataobjects.Recipient;
import com.kpro.dataobjects.Retention;

/**
 * A distance metric that calculates distance based on 
 * weighed union of a bit map interception
 * @version 240911.01 
 * @author dimitryk
 */
public class bitmapDistanceWisOne extends DistanceMetric{
	private double ours, delivery, same, other_recipient, unrelated, pub;
	private double current, admin, develop, tailoring, pseudo_analysis, pseudo_decision;
	private double individual_analysis, individual_decision, contact, historical, telemarketing, other_purpose;
	private double no_retention, stated_purpose, legal_requirement, business_practices, indefinitely;
	
	bitmapDistanceWisOne(Properties weights, String[] extraArgs){
		super(weights, extraArgs);
	}

	/**
	*	All weights are set to one.
	*/	
	private void setWeights(){
		
		//recipients
		ours=1;//Double.parseDouble(weightsConfig.getProperty("ours"));
		delivery=1;//=Double.parseDouble(weightsConfig.getProperty("delivery"));
		same=1;//=Double.parseDouble(weightsConfig.getProperty("same"));
		other_recipient=1;//=Double.parseDouble(weightsConfig.getProperty("other_recipient"));
		unrelated=1;//=Double.parseDouble(weightsConfig.getProperty("unrelated"));
		pub=1;//=Double.parseDouble(weightsConfig.getProperty("public"));
		//purpose
		current=1;//=Double.parseDouble(weightsConfig.getProperty("current"));
		admin=1;//=Double.parseDouble(weightsConfig.getProperty("admin"));
		develop=1;//= Double.parseDouble(weightsConfig.getProperty("develop"));
		tailoring=1;//=Double.parseDouble(weightsConfig.getProperty("tailoring"));
		pseudo_analysis=1;//=Double.parseDouble(weightsConfig.getProperty("pseudo_analysis"));
		pseudo_decision=1;//=Double.parseDouble(weightsConfig.getProperty("pseudo_decision"));
		individual_analysis=1;//=Double.parseDouble(weightsConfig.getProperty("individual_analysis"));
		individual_decision=1;//=Double.parseDouble(weightsConfig.getProperty("individual_decision"));
		contact=1;//=Double.parseDouble(weightsConfig.getProperty("contact"));
		historical=1;//=Double.parseDouble(weightsConfig.getProperty("historical"));
		telemarketing=1;//=Double.parseDouble(weightsConfig.getProperty("telemarketing"));
		other_purpose=1;//=Double.parseDouble(weightsConfig.getProperty("other_purpose"));
		//retention
		no_retention=1;//=Double.parseDouble(weightsConfig.getProperty("no_retention"));
		stated_purpose=1;//=Double.parseDouble(weightsConfig.getProperty("stated_purpose"));
		legal_requirement=1;//=Double.parseDouble(weightsConfig.getProperty("legal_requirement"));
		business_practices=1;//=Double.parseDouble(weightsConfig.getProperty("business_practices"));
		indefinitely=1;//=Double.parseDouble(weightsConfig.getProperty("indefinitely"));
		
	}
	
	/**
	 * internal method that makes a bit map of 6 ints
	 * each place in an array corresponds to a spesfic value
	 * of a Recipient field
	 *  
	 * @author dimitryk
	 * @param list input arraylist of ReciPients for method...
	 * @return Map as bitmap
	 */
	
	
	private double[] MakeRecipMap(ArrayList<Recipient> list){
		double[] Map = new double[6];
		for(int d=0;d<6;d++)Map[d]=0;
		for(int i=0;i<list.size();i++){
			switch (list.get(i)){
			case OURS: //weights can come in here 2
				Map[0]= ours;
				break;
			case DELIVERY:			
				Map[1]=delivery;
				break;
			case SAME:
				Map[2]=same;
				break;
			case OTHER_RECIPIENT:
				Map[3]=other_recipient;
				break;
			case UNRELATED:
				Map[4]=unrelated;
				break;
			case PUBLIC:
				Map[5]=pub;
				break;
			default:break;
			}
		}
		return Map;
	}
	
	/**
	 * internal method that makes a bit map of 12 doubles
	 * each place in an array corresponds to a spesfic value
	 * of a Purpose Enum
	 * @author dimitryk
	 * @param list input arraylist of Purpose for method...
	 * @return Map as bitmap
	 */
	
	private double[] MakePurposeMap(ArrayList<Purpose> list){
		double[] Map = new double[12];
		for(int d=0;d<12;d++)Map[d]=0;
		for(int i=0;i<list.size();i++){
			switch (list.get(i)){
			case CURRENT:
				Map[0]=current;
				break;
			case ADMIN:
				Map[1]=admin;
				break;
			case DEVELOP:
				Map[2]=develop;
				break;
			case TAILORING:
				Map[3]=tailoring;
				break;
			case PSEUDO_ANALYSIS:
				Map[4]=pseudo_analysis;
				break;
			case PSEUDO_DECISION:
				Map[5]=pseudo_decision;
				break;
			case INDIVIDUAL_ANALYSIS:
				Map[6]=individual_analysis;
				break;
			case INDIVIDUAL_DECISION:
				Map[7]=individual_decision;
				break;
			case CONTACT:
				Map[8]=contact;
				break;
			case HISTORICAL:
				Map[9]=historical;
				break;
			case TELEMARKETING:
				Map[10]=telemarketing;
				break;
			case OTHER_PURPOSE:
				Map[11]=other_purpose;
				break;
			default:break;
			}
		}
		return Map;
	}
	
	/**
	 * internal method that makes a bit map of 5 ints
	 * each place in an array corresponds to a spesfic value
	 * of a Retention Enum
	 * @author dimitryk
	 * @param list input arraylist of Purpose for method...
	 * @return Map as bitmap
	 */
	
	private double[] MakeRetentionMap(ArrayList<Retention> list){
		double[] Map = new double[5];
		for(int d=0;d<5;d++)Map[d]=0;
		for(int i=0;i<list.size();i++){
			switch (list.get(i)){
			case NO_RETENTION: //weights can come in here 2
				Map[0]=no_retention;
				break;
			case STATED_PURPOSE:
				Map[1]=stated_purpose;
				break;
			case LEGAL_REQUIREMENT:
				Map[2]=legal_requirement;
				break;
			case BUSINESS_PRACTICES:
				Map[3]=business_practices;
				break;
			case INDEFINITELY:
				Map[4]=indefinitely;
				break;
			default:break;
			}
		}
		return Map;
	}
	
	/**
	 * Calculates distance in the Recipien field from case a to case b
	 * using bit map distance = a interseption b * weight
	 * @author dimitryk
	 * @param a input case.
	 * @param b input case.
	 * @return dis double for the distance between to.
	 */
	
	private double getDistRecip(Case a, Case b) {
		double[] MapA, MapB;
		MapA = MakeRecipMap(a.getRecipients());
		MapB = MakeRecipMap(b.getRecipients());
		double dis=0;
		
		for(int i=0;i<6;i++){
			if(MapA[i]!=MapB[i]){
				dis=dis+Math.max(MapA[i], MapB[i]);
				//dis=dis+1; //for now... weights are jet not operational.
			}
		}
		
		
		return dis * 1;//Double.parseDouble(weightsConfig.getProperty("recipient"));// dis*weight later
	}

	/**
	 * Calculates distance in the Retention field from case a to case b
	 * using bit map distance = a interseption b * weight
	 * @author dimitryk
	 * @param a input case.
	 * @param b input case.
	 * @return dis double for the distance between to.
	 */

	private double getDistReten(Case a, Case b) {
		double[] MapA, MapB;
		MapA = MakeRetentionMap(a.getRetentions());
		MapB = MakeRetentionMap(b.getRetentions());
		double dis=0;
		
		for(int i=0;i<5;i++){
			if(MapA[i]!=MapB[i]){
				dis=dis+Math.max(MapA[i], MapB[i]);
			}
		}
		
		
		return dis *1;// Double.parseDouble(weightsConfig.getProperty("retention"));// dis*weight later
	}

	/**
	 * Calculates distance in the Purpose field from case a to case b
	 * using bit map distance = a interseption b * weight
	 * @author dimitryk
	 * @param a input case.
	 * @param b input case.
	 * @return dis double for the distance between to.
	 */
	
	private double getDistPurpose(Case a, Case b) {
		double[] MapA, MapB;
		MapA = MakePurposeMap(a.getPurposes());
		MapB = MakePurposeMap(b.getPurposes());
		double dis=0;
		
		for(int i=0;i<12;i++){
			if(MapA[i]!=MapB[i]){
				dis=dis+Math.max(MapA[i], MapB[i]);
			}
		}
		
		
		return dis*1;//Double.parseDouble(weightsConfig.getProperty("purpose"));// dis*weight later
	}

        /**
	* Returns the distance between two cases a and b based on purpose, recipient, retention, and data.
	* @author dimitryk
	* @param a the 1st case 
	* @param b the 2nd case
	* @return the distance between cases 
	*/
	private double getSumDistance(Case a, Case b) {
		
		return getDistPurpose(a,b)+getDistRecip(a,b)+getDistReten(a,b);
	}

      /**
      * Initializes weights (to one)  and returns the distance between two PolicyObjects.
      * @param a the 1st policy object
      * @param b the second policy object
      * @return the distance between the two policy objects
      */

	@Override
	public double getTotalDistance(PolicyObject a, PolicyObject b) {
		setWeights();
		ArrayList<Case> CasesA, CasesB;
		CasesA=a.getCases();
		CasesB=b.getCases();
		if(CasesA.size()<CasesB.size())return getTotalDistance(b, a);  //little magic to get symetry
		double minDist;
		double Distance = 0;
		for(int i=0;i<CasesA.size();i++){
			minDist=Double.MAX_VALUE;
			for(int d=0;d<CasesB.size();d++){
				minDist=Math.min(getSumDistance(CasesA.get(i), CasesB.get(d)),minDist);
			}
			Distance += minDist;
		}
		
		return Distance;
	}

}
