package com.kpro.algorithm;

import java.util.Properties;

import com.kpro.database.PDatabase;
import com.kpro.main.Gio;

public class LearnAlgSimpler extends LearnAlgorithm{

	public LearnAlgSimpler(Properties weightsConfig) {
		super(weightsConfig);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Properties applyML(Gio theIO) {
		WeightsIO w = new WeightsIO();

		
			double corr =0;
			for(Object i : getVals(i))
			{
				prop.setProptery(prop.get(new String(i)).reeval(pdb));
				corr (add corr above)
			}
			i.setProp (weight(corr))
		
	}
	
	private double reeval(PDatabase pdb, int i)
	{
		corrrelation(pdb.objs, i);
	}

	private correlation() ( 
	{
		int countyes = 0;
		int countno = 0;
		for i in history:
			if(prop i)
				if(accepted)
				{
					countyes ++;
				}
				else
					countno ++;
		return countyes/(countyes+countno)
	}
}
