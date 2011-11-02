function(head, req) {
	var action = {"accepted": true, "reasonDomains": [], "override": false, "confidence": 0.0};
	var accepted = [];
	var accepted_weight = 0;
	var rejected = [];
	var rejected_weight = 0;
	var i; //an array of [countaccepted, countrejected, distance, domain]
	while(i=getRow()) 
	{
		if(i[0]>0)
		{
			accepted.push(i[3]);
			accepted_weight += (i[2]*i[0]);
		}
		if(i[1]>0)
		{
			rejected.push(i[3]);
			rejected_weight += (i[2]*i[0]);
		}
	}

	if(accepted_weight < rejected_weight)
	{
		action.accepted = true;
		action.reasonDomains = accepted;
		action.confidence = (accepted_weight)/(accepted_weight+rejected_weight);
	}
	else
	{
		action.accepted = false;
		action.reasonDomains = rejected;
		action.confidence = (rejected_weight)/(accepted_weight+rejected_weight);
	}
		
	send(JSON.stringify(action));	  
}
