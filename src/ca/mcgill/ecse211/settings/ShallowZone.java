package ca.mcgill.ecse211.settings;

/**
 * All information related to the shallow array traversal. Received from the server. 
 * @author Team 2
 * @version 1.0
 * @since 1.5
 */
public class ShallowZone {
	// Constants 
	/** X and Y coordinates of the horizontal section's lower left corner. */
	private static int[] horizontalLowerLeftCorner;
	/** X and Y coordinates of the horizontal section's upper right corner. */
	private static int[] horizontalUpperRightCorner;
	/** X and Y coordinates of the vertical section's lower left corner. */
	private static int[] verticalLowerLeftCorner;
	/** X and Y coordinates of the vertical section's upper right corner. */
	private static int[] verticalUpperRightCorner;
	
	
	// Methods
	/**
	 * Getter for the coordinates of the horizontal section's lower left corner.
	 * @return Array that contains the X and Y coordinates in that order. 
	 * @since 1.0
	 */
	public static int[] getHorizontalLowerLeftCorner() {
		return horizontalLowerLeftCorner;
	}
	/**
	 * Mutator for the coordinates of the horizontal section's lower left corner. Only used by the wifi class. 
	 * @param x Integer value for the X coordinate. 
	 * @param y Integer value for the Y coordinate. 
	 * @since 1.0
	 */
	public static void setHorizontalLowerLeftCorner(int x, int y) {
		int[] coordinate = {x, y};
		ShallowZone.horizontalLowerLeftCorner = coordinate;
	}
	/**
	 * Getter for the coordinates of the horizontal section's upper right corner. 
	 * @return Array that contains the X and Y coordinates in that order. 
	 * @since 1.0
	 */
	public static int[] getHorizontalUpperRightCorner() {
		return horizontalUpperRightCorner;
	}
	/**
	 * Mutator for the coordinates of the horizontal section's upper right corner. Only used by the wifi class. 
	 * @param x Integer value of the X coordinate. 
	 * @param y Integer value of the Y coordinate. 
	 * @since 1.0
	 */
	public static void setHorizontalUpperRightCorner(int x, int y) {
		int[] coordinate = {x, y};
		ShallowZone.horizontalUpperRightCorner = coordinate;
	}
	/**
	 * Getter for the coordinates of the vertical section's lower left corner. 
	 * @return Array that contains the X and Y coordinates in that order. 
	 * @since 1.0
	 */
	public static int[] getVerticalLowerLeftCorner() {
		return verticalLowerLeftCorner;
	}
	/**
	 * Mutator for the coordinates of the vertical section's lower left corner. Only used by the wifi class. 
	 * @param x Integer value of the X coordinate.
	 * @param y Integer value of the Y coordinate. 
	 * @since 1.0
	 */
	public static void setVerticalLowerLeftCorner(int x, int y) {
		int[] coordinate = {x, y};
		ShallowZone.verticalLowerLeftCorner = coordinate;
	}
	/**
	 * Getter for the coordinates of the vertical section's upper right corner. 
	 * @return Array that contains the X and Y coordinates in that order. 
	 * @since 1.0 
	 */
	public static int[] getVerticalUpperRightCorner() {
		return verticalUpperRightCorner;
	}
	/**
	 * Mutator for the coordinates of the vertical section's upper right corner. Only used by the wifi class. 
	 * @param x Integer value of the X coordinate.
	 * @param y Integer value of the Y coordinate. 
	 * @since 1.0 
	 */
	public static void setVerticalUpperRightCorner(int x, int y) {
		int[] coordinate = {x, y};
		ShallowZone.verticalUpperRightCorner = coordinate;
	}
}
