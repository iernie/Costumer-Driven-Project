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
	
	public double getDistResip(Case a, Case b);
	
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
	
	/**
	 * Returns the weight of a recipient
	 * Used by the getDistResip() method
	 * 
	 * @author Nesha
	 * @param r input Recipient
	 * @return double value >0
	 */
	public double getWeigth(Recipient r);
	
	/**
	 * Returns the weight of a retention
	 * Used by the getDistReten() method
	 * 
	 * @author Nesha
	 * @param r input Retention
	 * @return double value >0
	 */
	public double getWeigth(Retention r);
	
	/**
	 * Returns the weight of a purpose
	 * Used by the getDistPurpose() method
	 * 
	 * @author Nesha
	 * @param r input Retention
	 * @return double value >0
	 */
	public double getWeigth(Purpose r);
	
}