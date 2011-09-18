
class dummy {
	
	public static void main(String[] args) {
		
		P3PParser parser = new P3PParser();
		PolicyObject policy = new PolicyObject();
		
        policy = parser.parse("http://info.yahoo.com/privacy/w3c/p3p_policy.xml");
        policy.print_all();
    }
    
}
//