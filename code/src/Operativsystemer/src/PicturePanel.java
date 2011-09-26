import java.awt.*;
import javax.swing.*;

/**
 * Component used by the GUI to display an image, with an optional overlay image
 * and four possible orientations.
 */
public class PicturePanel extends JPanel implements Constants {
	/** The image being displayed by this component */
	private Image image;
	/** An optional image being displayed on top of the other image */
	private Image topImage;
	/** The width of the image */
	private int imageWidth;
	/** The height of the image */
	private int imageHeight;
	/** The orientation of the image, NORTH, WEST, EAST or SOUTH */
	private int orientation;

	/**
	 * Creates a new PicturePanel displaying a given image.
	 * @param image	The image to be displayed.
	 */
	public PicturePanel(Image image) {
		super();
		this.image = image;
		orientation = SOUTH;
		setOpaque(false);
	}

	/**
	 * Specifies an image to be shown on top of the background image.
	 * In areas where the top image is transparent the background image will show.
	 * @param newTop	The new overlay image to be displayed.
	 */
	public void setTopImage(Image newTop) {
		topImage = newTop;
	}

	/**
	 * Specifies the orientation of the images being displayed.
	 * @param or	The new image orientation.
	 */
	public void setOrientation(int or){
		orientation = or;
	}

	/**
	 * Returns the preferred size of this component.
	 * @returns	The size of the images being displayed.
	 */
	public Dimension getPreferredSize() {
		return new Dimension(imageWidth, imageHeight);
	}

	/**
	 * Sets the position and size of this component.
	 * @param x	The x position of the top left corner.
	 * @param y	The y position of the top left corner.
	 * @param w	The width.
	 * @param h	The height.
	 */
	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x,y,w,h);
		imageWidth = w;
		imageHeight = h;
	}

	/**
	 * Paints this component.
	 * @param g	The Graphics2D graphics content to paint this component in.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		if(orientation > 0)
			g2D.rotate(Math.PI*orientation/2.0, imageWidth/2.0, imageHeight/2.0);
		g.drawImage(image,0,0,imageWidth,imageHeight,this);
		if(topImage != null)
			g2D.drawImage(topImage,0,0,imageWidth,imageHeight,this);
	}
}
