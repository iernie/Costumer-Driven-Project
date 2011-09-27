import java.util.ArrayList;

/**
 * 
 * @author ulfnore
 *
 */
public class kNearestNeighbors {
	
	private DistanceMetric distanceMetric; // distance metric
	private PDatabase database; // privacy object database
	private int k; // number of neighbors 
	
	// Constructors
	public kNearestNeighbors(){}
	public kNearestNeighbors(DistanceMetric distanceMetric, PDatabase database) {
		super();
		this.distanceMetric = distanceMetric;
	}

	// Getters and setters
	public DistanceMetric getDistanceMetric() { return distanceMetric;	}
	public void setDistanceMetric(DistanceMetric distanceMetric) { 
		this.distanceMetric = distanceMetric; 
	}
	public PDatabase getDatabase() {return database;}
	public void setDatabase(PDatabase database) {this.database = database;}
	public void setK(int k){ this.k = k; }
	public int getK(){ return k; }
	
	// The algorithm
	public ArrayList<PolicyObject> run(PolicyObject policyObject)
	{
		ArrayList<PolicyObject> kNearest = new ArrayList<PolicyObject>();
		PolicyObject farthest =  null;
		double farthestDistance = 0.0;
		
		for(PolicyObject po : database)
		{
			ArrayList<Object> farthestList = new ArrayList<Object>();
			if (kNearest.size()<k)
			{
				kNearest.add(po);
				// this is a hack.. updates the object farthest away 
				farthestList = findFarthest(kNearest, policyObject);
				farthest = (PolicyObject)farthestList.get(0);
				farthestDistance = (Double)farthestList.get(1);
			} else if(distanceMetric.getTotalDistance(po, policyObject) < farthestDistance)
			{
				kNearest.remove(farthest);
				// this is a hack.. updates the object farthest away 
				farthestList = findFarthest(kNearest, policyObject);
				farthest = (PolicyObject)farthestList.get(0);
				farthestDistance = (Double)farthestList.get(1);
			}
		}
		
		return kNearest;
	}


	/**
	 * Helper function to find the policy object 
	 * in the policyObjectsList farthest away from  
	 * "thePolicyObject". 
	 * 
	 * Returns untyped arraylist.
	 * 
	 * @param policyObjectsList
	 * @param thePolicyObject
	 * @return ArrayList [UNTYPED!]
	 */
	private ArrayList<Object> findFarthest(ArrayList<PolicyObject> policyObjectsList,
								   PolicyObject thePolicyObject)
	{
		double farthestDistance = distanceMetric.getTotalDistance(thePolicyObject, policyObjectsList.get(0));
		PolicyObject farthest = policyObjectsList.get(0);;
		
		for(PolicyObject p : policyObjectsList)
		{	
			double thisDist = distanceMetric.getTotalDistance(thePolicyObject, p);  
			if(thisDist > farthestDistance)
			{
				farthestDistance = thisDist;
				farthest = p;
			}
		}
		
		
		ArrayList<Object> farthestList = new ArrayList<Object>();
		farthestList.add(farthest); 
		farthestList.add(farthestDistance);
		return farthestList;
	}


	
}
