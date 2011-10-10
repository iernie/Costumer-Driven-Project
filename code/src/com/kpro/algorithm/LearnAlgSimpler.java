package com.kpro.algorithm;

import java.util.Properties;
import com.kpro.database.PDatabase;
import com.kpro.dataobjects.PolicyObject;
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
			i.setProp (weight(corr));
		
	}
	
	private double reeval(PDatabase pdb)
	{
		int i = 0;
		for(PolicyObject po : pdb.getCollection())
		{
			correlation(po, i);
			i++;
		}
		
	}

	private double correlation(PolicyObject po, int i)
	{
		int countyes = 0;
		int countno = 0;
		for (i in PDatabase)
		{
			if(prop i)
			{
				if(po.getAction().isAccepted())
				{
					countyes ++;
				}
				else
					countno ++;
				}
			}
		}
		return countyes/(countyes+countno)
	}
}
