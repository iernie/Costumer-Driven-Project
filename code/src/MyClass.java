import java.io.Serializable;
//test
/**
 * 
 * @author Aman
 * This is the class that implements Serializable and thus its state can be saved 
 * 2 objects for this class are created in SerializationDemo class
 *
 */

class MyClass implements Serializable { 
	String name; 
	int age; 
	double kuchV; 
	
	/**
	 * This is the constructor used to initialize object values.
	 */
	public MyClass(String s, int i, double d) { 
		this.name = s; 
		this.age = i; 
		this.kuchV = d; 
	} 
	
	/**
	 * This is the over-ridden method to display object variables value as a String. 
	 */
	public String toString() { 
		return "s=" + name + "; i=" + age + "; d=" + kuchV; 
	}
}