import java.util.ArrayList;

/**
 * abstract class for all conclusion classes (they take the new policy and 
 * a reduction of the history versus new policy), and return an Action.
 * 
 * @author ngerstle
 * @version 29.09.11.1
 * 
 */

public abstract class ConclusionAlgorithm {

	//constructor
	public ConclusionAlgorithm(){}
	
	public abstract Action conclude(PolicyObject np, ArrayList<PolicyObject> knearestns);
	
	
	
}
