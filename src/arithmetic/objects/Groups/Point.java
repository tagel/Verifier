package arithmetic.objects.Groups;

import arithmetic.objects.Field.IntegerFieldElement;




/**
 * this class represents a point representing a location in (x,y) coordinate space, specified in integer precision.
 *
 */
public class Point {

	
	/**
	 * the x coordinate
	 */
	private IntegerFieldElement x;
	/**
	 * the y coordinate
	 */
	private IntegerFieldElement y;
	
	/**
	 * @param x coordinate
	 * @param y coordinate
	 * Constructor.
	 */
	public Point (IntegerFieldElement x, IntegerFieldElement y) {
		this.x=x;
		this.y=y;
	}


	/**
	 * @return the x coordinate.
	 */
	public IntegerFieldElement getX() {
		return x;
	}

	/**
	 * @return the y coordinate.
	 */
	public IntegerFieldElement getY() {
		return y;
	}
	
	/**
	 * @param x new coordinate
	 * @param y new coordinate
	 * Changes the point to have the specified location.
	 */
	public void changeCoordinates(IntegerFieldElement x, IntegerFieldElement y) {
		this.x = x;
		this.y = y;
	}
	
}