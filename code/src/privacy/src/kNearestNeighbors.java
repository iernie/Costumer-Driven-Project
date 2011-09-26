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
			if (kNearest.size()<k)
			{
				kNearest.add(po);
				findFarthest(kNearest, policyObject);
			} else{
				if(distanceMetric.getTotalDistance(po, policyObject) < farthestDistance)
					
			}
		}
		
		return kNearest;
	}
	private Pair<> findFarthest(ArrayList<PolicyObject> policyObjectsList, thePolicyObject)
	{
		
		
	}
	
	/**
	 * For returning a pair
	 * @author ulfnore
	 *
	 * @param <T>
	 * @param <S>
	 */
	class Pair<T,S>{
		T first;
		S second;
	}
	
}
