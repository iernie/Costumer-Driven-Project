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
	 * Calculates distance between case a and case b focusing on recipient field only
	 * 
	 * @author dimitryk
	 * @param a input Case
	 * @param b input Case
	 * @return double 0 if cases are similar and positive integer if they are not
	 */
	
	public double getDistRecip(Case a, Case b);
	
	/**
	 * Calculates distance between case a and case b focusing on retention field only
	 * 
	 * @author dimitryk
	 * @param a input Case
	 * @param b input Case
	 * @return double 0 if cases are similar and positive integer if they are not
	 */
	
	public double getDistReten(Case a, Case b);
	
	/**
	 * Calculates distance between case a and case b focusing on urpose field only
	 * 
	 * @author dimitryk
	 * @param a input Case
	 * @param b input Case
	 * @return double 0 if cases are similar and positive integer if they are not
	 */
	
	public double getDistPurpose(Case a, Case b);

}
