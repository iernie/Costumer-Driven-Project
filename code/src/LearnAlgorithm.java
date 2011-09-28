import java.util.Properties;

public abstract class LearnAlgorithm {
	Properties weightsConfig;
	
	public LearnAlgorithm(Properties weightsConfig)
	{
		this.weightsConfig = weightsConfig;
	}
	public void learn(Gio theIO)
	{
		theIO.writeWeights(applyML(theIO));
		
	}
	
	protected abstract Properties applyML(Gio theIO);

	
}
