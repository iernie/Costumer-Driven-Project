import java.util.ArrayList;


public abstract class ReductionAlgorithm {

	protected PolicyDatabase pdb;
	
	public ReductionAlgorithm(PolicyDatabase pdb)
	{
		this.pdb = pdb;
	}
	
	/**
	 * 
	 * @param policyDatabase 
	 * @param newPO 
	 * @return a modified newpol 
	 */
	public abstract ArrayList<PolicyObject> reduce(PolicyObject newPO);

}
