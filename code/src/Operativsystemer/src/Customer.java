import java.awt.*;
import javax.swing.*;

/**
 * This class implements a customer's part of the Barbershop example.
 * This is a passive class just holding data.
 */
public class Customer implements Constants {
	/** The ID of the next customer to be created */
	public static int nextID = 0;
	/** The ID of this customer */
	private int customerID;
	/** An integer specifying the look of this customer, used by the GUI only */
	private int customerLook;

	/**
	 * Creates a new customer, giving him a unique ID and a random look.
	 */
	public Customer() {
		customerID = ++nextID;
		customerLook = (int)(Math.random()*NOF_CUSTOMER_LOOKS);
	}

	/**
	 * Returns the ID of this customer.
	 * @return	The ID of this customer.
	 */
	public int getCustomerID() {
		return customerID;
	}

	/**
	 * Returns an image with the look of this customer. Used by the GUI.
	 * @return	The image with the look of this customer.
	 */
	public Image getImage(){
		return BarbershopGui.customerImages[customerLook];
	}
}
