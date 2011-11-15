function(doc) {
//compare doc to incomming. return distance, accept, doc ?  



	if(doc[actionTaken] && doc[cases])
	{
		var casevals = [];
		//calculate value for each case:
		doc[cases].foreach(function(tcase){
			var cval = 0.0;

			//purpose
			var purpose_vals = ['CURRENT': 0.1,'ADMIN':0.1,'DEVELOP':0.1,'TAILORING':0.1, 'PSEUDO_ANALYSIS':0.1, 'PSEUDO_DECISION': 0.1, 'INDIVIDUAL_ANALYSIS': 0.1, 'INDIVIDUAL_DECISION': 0.1, 'CONTACT': 0.1, 'HISTORICAL': 0.1, 'TELEMARKETING': 0.1, 'OTHER_PURPOSE': 0.1, 'CUSTOMIZATION': 0.1];
			tcase.purpose.foreach(function(apurp)
			{
				cval+=purpose_vals[apurp];
			}
	
			//retention
			var retention_vals['NO_RETENTION': 0.1,  'STATED_PURPOSE': 0.1, 'LEGAL_REQUIREMENT': 0.1, 'BUSINESS_PRACTICES': 0.1, 'INDEFINITELY': 0.1];
			cval+= retention_vals[tcase.retention];

			//recipient
			var recipient_vals = ['OURS': 0.1, 'DELIVERY': 0.1, 'SAME': 0.1, 'OTHER_RECIPIENT': 0.1, 'UNRELATED': 0.1, 'PUBLIC': 0.1];
			tcase.recipient.foreach(function(arecip)
			{
				cval+=recipient_vals[arecip];
			}



			casevals.push([tcase.datatype,cval]);
		}		

		//return case-vals, doc
		emit(casevals,doc);
				
//old		emit(doc,doc);
	}
}
