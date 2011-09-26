/**
 * Constants used by the GUI.
 */
public interface Constants
{
	/** The width of the barbershop area */
	public final static int ROOM_WIDTH = 400;
	/** The height of the barbershop area */
	public final static int ROOM_HEIGHT = 400;
	/** The width of a chair */
	public final static int CHAIR_WIDTH = 30;
	/** The height of a chair */
	public final static int CHAIR_HEIGHT = 30;
	/** The width of a barber image */
	public final static int BARBER_WIDTH = 30;
	/** The height of a barber image */
	public final static int BARBER_HEIGHT = 30;
	/** The orientation SOUTH */
	public final static int SOUTH = 0;
	/** The orientation WEST */
	public final static int WEST = 1;
	/** The orientation NORTH */
	public final static int NORTH = 2;
	/** The orientation EAST */
	public final static int EAST = 3;
	/** The positions of 18 chairs in the waiting lounge */
	public final static int CHAIR_POSITIONS[][] = {{15,53,EAST},{56,15,SOUTH},{98,15,SOUTH},{140,15,SOUTH},
		{183,15,SOUTH},{226,15,SOUTH},{268,15,SOUTH},{311,15,SOUTH},{354,53,WEST},{354,96,WEST},{354,139,WEST},
		{311,177,NORTH},{268,177,NORTH},{183,177,NORTH},{140,177,NORTH},{98,177,NORTH},{56,177,NORTH},{15,139,EAST}};
	/** The number of chairs actually being shown, must be no more than 18 */
	public final static int NOF_CHAIRS = 18;
	/** The number of barbers and barber's chairs being shown, must be no more than 3 */
	public final static int NOF_BARBERS = 3;
	/** The number of different customer looks, must be reflected by corresponding images in the /images directory */
	public final static int NOF_CUSTOMER_LOOKS = 16;
	/** Identifier identifying a chair as a waiting lounge chair */
	public final static int LOUNGE_CHAIR = 0;
	/** Identifier identifying a chair as a barber's chair */
	public final static int BARBER_CHAIR = 1;
	/** The minimum number of milliseconds a barber sleeps between each work period */
	public final static int MIN_BARBER_SLEEP = 0;
	/** The maximum number of milliseconds a barber sleeps between each work period */
	public final static int MAX_BARBER_SLEEP = 6000;
	/** The minimum number of milliseconds it takes a barber to cut a customer's hair */
	public final static int MIN_BARBER_WORK = 0;
	/** The maximum number of milliseconds it takes a barber to cut a customer's hair */
	public final static int MAX_BARBER_WORK = 6000;
	/** The minimum number of milliseconds between each time a new customer arrives */
	public final static int MIN_DOORMAN_SLEEP = 100;
	/** The maximum number of milliseconds between each time a new customer arrives */
	public final static int MAX_DOORMAN_SLEEP = 5900;
}
