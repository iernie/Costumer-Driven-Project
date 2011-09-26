import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * This class displays a GUI for the Barbershop example, and also contains the
 * startup method. It implements the Gui interface containing methods publicly
 * available to be used by the Doorman, Barber and CustomerQueue classes.
 */
public class BarbershopGui extends JFrame implements Constants, Gui, ChangeListener {
	/** Various images used by the GUI */
	public static Image tableImage;
	public static Image deskImage;
	public static Image loungeChairImage;
	public static Image barberChairImage;
	public static Image floorImage;
	public static Image wallsImage;
	public static Image barberImage;
	public static Image sleepImage;
	public static Image[] customerImages;

	/** The text area displaying textual output to the user */
	private TextArea display;
	/** The panel showing the barbershop salon */
	private RoomPanel roomPanel;
	/** The panel containing sliders and the output area */
	private JPanel controlPanel;
	/** The sliders controlling the speeds of different tasks */
	private JSlider barberSleepSlider, barberWorkSlider, doormanSleepSlider;

	/** A reference to the doorman */
	private Doorman doorman;
	/** An array of references to the barbers */
	private Barber barbers[];

	/**
	 * Creates a new GUI.
	 * @param title	The tile of the GUI window.
	 */
	public BarbershopGui(String title) {
		super(title);
		loadImages();
		placeComponents();
		setSize(706,427);
		setResizable(false);
		// Add an anonymous WindowListener which calls quit() when the window is closing
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				quit();
			}
		});
		show();
	}

	/**
	 * Creates the customer queue, the doorman, and the barbers,
	 * and starts the simulation by starting the doorman and barber
	 * threads.
	 */
	public void startSimulation() {
		CustomerQueue queue = new CustomerQueue(NOF_CHAIRS, this);
		doorman = new Doorman(queue, this);
		doorman.startThread();
		barbers = new Barber[NOF_BARBERS];
		for(int i = 0; i < NOF_BARBERS; i++) {
			barbers[i] = new Barber(queue, this, i);
			barbers[i].startThread();
		}
	}

	/**
	 * Stops all threads and exits the program.
	 */
	private void quit() {
		doorman.stopThread();
		for(int i = 0; i < NOF_BARBERS; i++)
			barbers[i].stopThread();
		System.exit(0);
	}

	/**
	 * Loads an image from a file.
	 * @param tk		The toolkit to be used to load the image.
	 * @param file		The name of the file containing the image.
	 * @param tracker	The media tracker tracking the progress of the load.
	 * @return			The image that was loaded, as an Image object.
	 */
	private Image loadImage(Toolkit tk, String file, MediaTracker tracker) {
		Image result = tk.createImage(file);
		tracker.addImage(result, 0);
		return result;
	}

	/**
	 * Loads all images to be used by the GUI, and waits for them to
	 * be fully loaded before returning.
	 */
	private void loadImages() {
		MediaTracker tracker = new MediaTracker(this);
		Toolkit tk = Toolkit.getDefaultToolkit();
		wallsImage = loadImage(tk, "images/walls.gif", tracker);
		floorImage = loadImage(tk, "images/floor.gif", tracker);
		loungeChairImage = loadImage(tk, "images/loungechair.gif", tracker);
		barberChairImage = loadImage(tk, "images/barberchair.gif", tracker);
		barberImage = loadImage(tk, "images/barber.gif", tracker);
		customerImages = new Image[NOF_CUSTOMER_LOOKS];
		for(int i = 0; i < NOF_CUSTOMER_LOOKS; i++) {
			customerImages[i] = loadImage(tk, "images/customer"+i+".gif", tracker);
		}
		tableImage = loadImage(tk, "images/table.gif", tracker);
		deskImage = loadImage(tk, "images/desk.gif", tracker);
		sleepImage = loadImage(tk, "images/sleep.gif", tracker);
		try {
			tracker.waitForID(0);
	    } catch (InterruptedException ie) {}
	}

	/**
	 * Creates and places all components of the GUI.
	 */
	private void placeComponents(){
		display = new TextArea(4,30);
		display.setEditable(false);
		roomPanel = new RoomPanel();
		controlPanel = new JPanel();
		barberSleepSlider = new JSlider(MIN_BARBER_SLEEP, MAX_BARBER_SLEEP, Globals.barberSleep);
		barberWorkSlider = new JSlider(MIN_BARBER_WORK, MAX_BARBER_WORK, Globals.barberWork);
		doormanSleepSlider = new JSlider(MIN_DOORMAN_SLEEP, MAX_DOORMAN_SLEEP, Globals.doormanSleep);
		barberSleepSlider.addChangeListener(this);
		barberWorkSlider.addChangeListener(this);
		doormanSleepSlider.addChangeListener(this);
		controlPanel.setLayout(null);
		controlPanel.add(doormanSleepSlider);
		controlPanel.add(barberSleepSlider);
		controlPanel.add(barberWorkSlider);
		controlPanel.add(display);
		addSliderLabels(controlPanel,10,10,280,20,MIN_DOORMAN_SLEEP,MAX_DOORMAN_SLEEP,"Doorman sleep time");
		doormanSleepSlider.setBounds(10,30,280,20);
		addSliderLabels(controlPanel,10,50,280,20,MIN_BARBER_SLEEP,MAX_BARBER_SLEEP,"Barber sleep time");
		barberSleepSlider.setBounds(10,70,280,20);
		addSliderLabels(controlPanel,10,90,280,20,MIN_BARBER_WORK,MAX_BARBER_WORK,"Barber work time");
		barberWorkSlider.setBounds(10,110,280,20);
		display.setBounds(10,150,280,240);
		controlPanel.setPreferredSize(new Dimension(300,400));

		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(roomPanel, BorderLayout.CENTER);
		cp.add(controlPanel, BorderLayout.EAST);
	}

	/**
	 * Creates, adds and positions labels above a slider.
	 * @param p			The panel to add the labels to.
	 * @param x			The x position of the leftmost label.
	 * @param y			The y position of the topmost label.
	 * @param w			The width from leftmost label to rightmost pixel of the rightmost label
	 * @param h			The height of the labels.
	 * @param minValue	The value to be displayed on the left label.
	 * @param maxValue	The value to be displayed on the right label.
	 * @param text		The text to be displayed in the central label.
	 */
	private void addSliderLabels(JPanel p, int x, int y, int w, int h, int minValue, int maxValue, String text) {
		JLabel left, middle, right;
		left = new JLabel(""+minValue);
		left.setHorizontalAlignment(JLabel.LEFT);
		left.setOpaque(false);
		p.add(left);
		left.setBounds(x,y,w,h);
		middle = new JLabel(text);
		middle.setHorizontalAlignment(JLabel.CENTER);
		middle.setOpaque(false);
		p.add(middle);
		middle.setBounds(x,y,w,h);
		right = new JLabel(""+maxValue);
		right.setHorizontalAlignment(JLabel.RIGHT);
		right.setOpaque(false);
		p.add(right);
		right.setBounds(x,y,w,h);
	}

	/**
	 * Called when one of the sliders' knobs has been moved.
	 * @param e	The ChangeEvent describing the change.
	 */
	public void stateChanged(ChangeEvent e) {
		Globals.barberWork = barberWorkSlider.getValue();
		Globals.barberSleep = barberSleepSlider.getValue();
		Globals.doormanSleep = doormanSleepSlider.getValue();
	}

	/**
	 * Outputs a text string to the user.
	 * @param text	The text to be outputted.
	 */
	public synchronized void println(String text) {
		display.append(text+"\n");
	}

	/**
	 * Shows a customer sitting in a waiting lounge chair.
	 * @param pos		The position of the chair.
	 * @param customer	The customer that is sitting in that chair.
	 */
	public void fillLoungeChair(int pos, Customer customer) {
		roomPanel.fillLoungeChair(pos, customer);
		repaint();
	}

	/**
	 * Shows a waiting lounge chair as being unoccupied.
	 * @param pos	The position of the chair.
	 */
	public void emptyLoungeChair(int pos) {
		roomPanel.emptyLoungeChair(pos);
		repaint();
	}

	/**
	 * Shows a customer sitting in a barber's chair.
	 * @param pos	The position of the barber chair.
	 */
	public void fillBarberChair(int pos, Customer customer) {
		roomPanel.fillBarberChair(pos, customer);
		repaint();
	}

	/**
	 * Shows a barber chair as being unoccupied.
	 * @param pos		The position of the barber chair.
	 */
	public void emptyBarberChair(int pos) {
		roomPanel.emptyBarberChair(pos);
		repaint();
	}

	/**
	 * Shows a barber sleeping.
	 * @param pos	The position of the barber's chair.
	 */
	public void barberIsSleeping(int pos) {
		roomPanel.setBarberSleep(pos, true);
		repaint();
	}

	/**
	 * Shows a barber as being awake.
	 * @param pos	The position of the barber's chair.
	 */
	public void barberIsAwake(int pos) {
		roomPanel.setBarberSleep(pos, false);
		repaint();
	}

	/**
	 * The startup method.
	 * @param args	Parameters passed to the program from the command line, none expected.
	 */
	public static void main(String args[]) {
		BarbershopGui gui = new BarbershopGui("Solution to P2");
		gui.startSimulation();
	}
}
