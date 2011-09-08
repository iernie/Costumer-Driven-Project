
class dummy {
    public static void test(){
    	System.out.println("Test");
    }
	
	public static void main(String[] args) {
        System.out.println("Hello Worlffd!"); // Display the string.
        test();
        
        try {
			new XMLParser().load("http://info.yahoo.com/privacy/w3c/p3p_policy.xml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}
//