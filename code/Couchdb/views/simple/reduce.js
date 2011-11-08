function(keys, values, rereduce) 
{
	//reduce to nearest neighbors
	//return [[acceptcount,rejectcount,distance,domain,policy],...]
	var results = [];
	var k = keys['k'];
	var casevals = [];
	
	//calculates value for each case in new policy:
	keys['newpol'].foreach(function(tcases){
		var cval = 0.0;
		//purpose
		var purpose_vals = ['CURRENT': 0.1,'ADMIN':0.1,'DEVELOP':0.1,'TAILORING':0.1, 'PSEUDO_ANALYSIS':0.1, 'PSEUDO_DECISION': 0.1, 'INDIVIDUAL_ANALYSIS': 0.1, 'INDIVIDUAL_DECISION': 0.1, 'CONTACT': 0.1, 'HISTORICAL': 0.1, 'TELEMARKETING': 0.1, 'OTHER_PURPOSE': 0.1, 'CUSTOMIZATION': 0.1]
			
		tcases.purpose.foreach(function(apurp)
		{
			cval+=purpose_vals[apurp];
		}

                //retention
                var retention_vals['NO_RETENTION': 0.1,  'STATED_PURPOSE': 0.1, 'LEGAL_REQUIREMENT': 0.1, 'BUSINESS_PRACTICES': 0.1, 'INDEFINITELY': 0.1];
                cval+= retention_vals[tcase.retention];


		//recipient
		var recipient_vals = ['OURS': 0.1, 'DELIVERY': 0.1, 'SAME': 0.1, 'OTHER_RECIPIENT': 0.1, 'UNRELATED': 0.1, 'PUBLIC': 0.1]
		tcases.recipient.foreach(function(arecip)
		{
			cval+=recipient_vals[arecip];
		}

		casevals.push([tcase.datatype,cval]);
	}		

	function distance(pol1)
	{
		var d = 0;

		casevals.foreach(function(i){//TODO
			if(pol
		}
		//compare each matching policy, count unmatches
		return d;
	}
	function processI(pol1)
	{
		var r = [0,0,distance(pol1),pol1[contexti][domain],pol1];
		if(pol1.actionTaken.accepted)
		{
			r[0]=1;
		}
		if(!pol1.actionTaken.accepted)
		{
			r[1]=0;
		}
		return r;
	}


	if(!rereduce)
	{
		var newpol = keys['newpol'];
		[results.push(process(i)) for (i in values)];
	}

	//merge duplicates
	var resultsM = [];

	for each (var item in results) function(item) //need to iterate by array, not items in array
	{
		for(var i = 0; i<resultsM.size(); i++)
		{
			if(resultsM[i][3] == item[3])
			{
				resultsM[i][0]+=item[0];
				resultsM[i][1]+=item[1];
				break;
			}
		}
			
	};	

	results = resultsM

	//sort
	results.sort(function(a,b){return a[2]-b[2];});
	//return k
	results = results.slice(0,k);
	return results;
}

