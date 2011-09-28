import java.util.ArrayList; //used to store results
import java.util.Collections;
import java.util.Comparator;

/**
 * A k-nearest-neighbors algorithm class. create it and call run on it to
 * get the nearest k neighbors to the object passed to run().
 * @author ngerstle
 * @version 29.09.11.1
 *
 */
public class Reduction_KNN  extends ReductionAlgorithm{
	
	private DistanceMetric distanceMetric; // distance metric
	private int k; // number of neighbors 
	
	
	/**
	 * creates a kNearestNeighbors algorithm to use
	 * @param distanceMetric	the class defining distance between objects
	 * @param database	the database of objects to operate on
	 * @param k the size of k
	 * @author ngerstle, ulfnore
	 */
	public Reduction_KNN(DistanceMetric distanceMetric, PolicyDatabase database,int k) {
		super(database);
		this.distanceMetric = distanceMetric;
		this.k=k;
	}

	
	/**
	 * the method that returns the closest k objects to the parameter.
	 * works by sorting elements by distance from passed object, and passing the first
	 * k elements.
	 * 
	 * @param newPO the new PolicyObject the thing to find the neighbors of 
	 * @return ArrayList<PolicyObject> an arraylist of size k of the nearest neighbors
	 * @author ngerstle
	 */
	public ArrayList<PolicyObject> reduce(final PolicyObject newPO) 
	{
		//Copying pdb.idb should not be unnecessary- remove all_pos and for loop
		ArrayList<PolicyObject> all_pos = new ArrayList<PolicyObject>();
		for(PolicyObject po : pdb)
		{
			all_pos.add(po);
		}
		Collections.sort(all_pos,		
				new Comparator<PolicyObject>() {
					public int compare(PolicyObject o1, PolicyObject o2) 
					{		
						double a = distanceMetric.getTotalDistance(newPO, o1);
						double b = distanceMetric.getTotalDistance(newPO, o2);
						return (a>b ? -1 : (a==b ? 0 : 1));
					}
				}
		);
		return (ArrayList<PolicyObject>) all_pos.subList(0, k);
	}


	
}
