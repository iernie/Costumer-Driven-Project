function(doc) {
//compare doc to incomming. return distance, accept, doc ?  
	if(doc.actionTaken && doc.cases)
	{
		emit(doc,doc);
	}
}
