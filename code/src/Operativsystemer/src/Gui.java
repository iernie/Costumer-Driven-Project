public interface Gui
{
	/**
	 * Outputs a text string to the user.
	 * @param text	The text to be outputted.
	 */
	public void println(String text);

	/**
	 * Shows a customer sitting in a waiting lounge chair.
	 * @param pos		The position of the chair.
	 * @param customer	The customer that is sitting in that chair.
	 */
	public void fillLoungeChair(int pos, Customer customer);

	/**
	 * Shows a waiting lounge chair as being unoccupied.
	 * @param pos	The position of the chair.
	 */
	public void emptyLoungeChair(int pos);

	/**
	 * Shows a customer sitting in a barber's chair.
	 * @param pos	The position of the barber chair.
	 */
	public void fillBarberChair(int pos, Customer customer);

	/**
	 * Shows a barber chair as being unoccupied.
	 * @param pos		The position of the barber chair.
	 */
	public void emptyBarberChair(int pos);

	/**
	 * Shows a barber sleeping.
	 * @param pos	The position of the barber's chair.
	 */
	public void barberIsSleeping(int pos);

	/**
	 * Shows a barber as being awake.
	 * @param pos	The position of the barber's chair.
	 */
	public void barberIsAwake(int pos);
}
