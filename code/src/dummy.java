
class dummy {
	
	public static void main(String[] args) {
		
		P3PParser p = new P3PParser();
        
        try {
			p.parse("http://info.yahoo.com/privacy/w3c/p3p_policy.xml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        PolicyObject policy = p.getObject();
        policy.iterate();
    }
    
}
//