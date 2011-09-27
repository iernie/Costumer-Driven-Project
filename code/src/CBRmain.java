/**
 * 
 * Skeleton CBR class 
 * return types for CBR methods to 
 * be determined
 * 
 * @author ulfnore
 *
 */
public class CBRmain {
	private kNearestNeighbors kNN;
	private PDatabase database;
	private DistanceMetric distanceMetric;
	
	// constructors
	public CBRmain(){}
	
	
	// Getters/setters
	
	public kNearestNeighbors getkNN() {
		return kNN;
	}
	public void setkNN(kNearestNeighbors kNN) {
		this.kNN = kNN;
	}
	public PDatabase getDatabase() {
		return database;
	}
	public void setDatabase(PDatabase database) {
		this.database = database;
	}
	public DistanceMetric getDistanceMetric() {
		return distanceMetric;
	}
	public void setDistanceMetric(DistanceMetric distanceMetric) {
		this.distanceMetric = distanceMetric;
	}
	
	
	// external interface
	
	/**
	 * Run the CBR on a given PolicyObject
	 */
	public void run(PolicyObject policyObj){
		
	}
	
	
	// Internal CBR methods

	
	/**
	 * Retrieve k most similar cases from database
	 */
	private void retrieve(){
		
	}
	/**
	 * Suggest action based on retrieved cases
	 */
	private void reuse(){
		
	}
	/**
	 * Learn [update parameters/weights]
	 * based on user feedback
	 */
	private void revise(){
		
	}
	/**
	 * Store case back to database with action
	 */
	private void retain(){
		
	}

	



	
	
}
