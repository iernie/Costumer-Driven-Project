import java.util.ArrayList;
import java.util.Properties;

public class CBR {

	protected PolicyObject newpol;
	protected Gio theIO;
	protected Properties weightsConfig;

	protected ReductionAlgorithm reduceAlg;
	protected ConclusionAlgorithm conclusAlg;
	protected LearnAlgorithm learnAlg;
	
	public CBR(Gio theIO, Properties weightsConfig,
			ReductionAlgorithm reduceAlg, ConclusionAlgorithm conclusAlg, LearnAlgorithm learnAlg) {
		this.theIO = theIO;
		this.weightsConfig = weightsConfig;
		this.reduceAlg = reduceAlg;
		this.conclusAlg = conclusAlg;
		this.learnAlg = learnAlg;
	}




	/**
	 * Accepts a parsed PolicyObject that needs a action attached to it
	 * 
	 * 
	 * @author ngerstle
	 * 
	 * @param newPO the new policy to be processed
	 * @return the same policy object with an action
	 */
	private PolicyObject process(PolicyObject newPO) {
		ArrayList<PolicyObject> reducedSet = reduceAlg.reduce(newPO);
		Action a = conclusAlg.conclude(newPO,reducedSet);
		newPO.setAction(a);
		return newPO;
	}


	public void run(PolicyObject newPolicy) {
		newpol =  process(newPolicy); //knn & conclusion
		newpol = theIO.userResponse(newpol); //user response
		theIO.getPDB().addPolicy(newpol); //save to database
		learnAlg.learn(theIO);
	}


}