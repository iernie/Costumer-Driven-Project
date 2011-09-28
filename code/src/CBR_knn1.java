import java.util.ArrayList;
import java.util.Properties;


/**
 * A simple CBR case using knn- runs knn on the history and newobject, and uses
 * 'simpleConclusion' conclusion to determine the results before proceeding to the
 * recomendation, saving the object, and re-evaluating the weights (which this CBR
 * does not do).
 * 
 * @author ngerstle
 * @version 28.09.11.1
 */
public class CBR_knn1 extends CBR {
	
	
	protected DistanceMetric dmetric;
	
	
	/**
	 * simpleCBR based on knn constructor 
	 * @param theIO
	 * @param newPO
	 */
	public CBR_knn1(Gio theIO,PolicyObject newPO,Properties weightsConfig) {
		this.theIO= theIO;
		newpol = newPO;
		this.weightsConfig = weightsConfig;		
		dmetric = new distanceMetricTest(this.weightsConfig);
		conclusAlg =  new simpleConclusion(dmetric);
	}

	public ArrayList<PolicyObject> reduce() {
		knnReduceAlgorithm knn = new knnReduceAlgorithm(new distanceMetricTest(weightsConfig),theIO.getPDB(), 1);
		return knn.run(newpol);
	
	}
	
	
	




}
