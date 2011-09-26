/**
 * Interface of a Distance metric class
 *
 * @author Dimitry Kongevold(dimitryk)
 * 
 */

/**
 * A DistanceMetric interface has to contain 3 methods
 * method for calculation of distance between Recipients, Purposes and Retentions
 * between cases
 * @version 160911.1
 * @author dimitryk
 */
public interface DistanceMetric {
	
	/**
	 * Calculates total distance between all fields
	 * 
	 * @author dimitryk
	 * @param a input Case
	 * @param b input Case
	 * @return double 0 if cases are similar and positive integer if they are not
	 */
	
	public double getTotalDistance(PolicyObject a, PolicyObject b);

}
