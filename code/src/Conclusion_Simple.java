import java.util.ArrayList;

/**
 * a very simple conclusion class. result is based on the closest object only.
 * 
 * @author ngerstle
 * @version 29.09.11.1
 */
public class Conclusion_Simple extends ConclusionAlgorithm {

	private DistanceMetric distanceMetric; // distance metric to use for choosing
	
	
	/**
	 * a simple conclusion that just bases the result on the inverse distance of the policies,
	 * and returns as a confidence the sum of inverse distances
	 * 
	 * @param knearestns
	 * @return the action to take
	 */
	public Conclusion_Simple(DistanceMetric distanceMetric)
	{
		this.distanceMetric = distanceMetric;
	}
	
	/**
	 * makes a decision on the reduced set
	 * 
	 * @author ngerstle
	 * 
	 * @param np the object under consideration
	 * @param releventSet the reduced set of neighbors
	 * @return an arraylist of {Action a, double Confidence) 
	 *
	 */
	public Action conclude(PolicyObject np, Iterable<PolicyObject> releventSet)
	{
		ArrayList<PolicyObject> approveList = new ArrayList<PolicyObject>();
		ArrayList<PolicyObject> rejectList = new ArrayList<PolicyObject>();
		double appdistance = 0; //the sum of the inverse distance to all approved PolicyObjects
		double rejdistance = 0; //the sum of the inverse distance to all rejected PolicyObjects
		
		for( PolicyObject i : releventSet)
		{
			if(i.getAction().isAccept())
			{
				approveList.add(i);
				appdistance+=(1/distanceMetric.getTotalDistance(np, i));
			}
			else
			{
				rejectList.add(i);
				rejdistance+=(1/distanceMetric.getTotalDistance(np, i));				
			}
		}
		if(appdistance > rejdistance)
		{
			return new Action(true, approveList, appdistance, false);
		}
		else
		{
			return new Action(true, rejectList, rejdistance, false);
		}
	}

}
