package ca.mcgill.ecse211.settings;

/**
 * All info related to the search zone of the game board. Received from the server through wifi.
 * @author Team 2
 * @version 1.0
 * @since 1.5
 */
public class SearchRegion {

	// Constants
	/** X and Y coordinates of the red search zone's lower left corner. */
	private static int[] redSearchLowerLeftCorner;
	/** X and Y coordinates of the red search zone's upper right corner. */
	private static int[] redSearchUpperRightCorner;
	
	/** X and Y coordinates of the green search zone's lower left corner. */
	private static int[] greenSearchLowerLeftCorner;
	/** X and Y coordinates of the green search zone's lower right corner. */
	private static int[] greenSearchUpperRightCorner;
	
	
	// Methods
	/**
	 * Getter for the coordinates of the red search zone's lower left corner.
	 * @return Array that contains that X and Y coordinate in that order.
	 * @since 1.0
	 */
	public static int[] getRedSearchLowerLeftCorner() {
		return redSearchLowerLeftCorner;
	}
	/**
	 * Mutator for the coordinates of the red search zone's lower left corner. Only used by the wifi class. 
	 * @param x Integer value for the X coordinate. 
	 * @param y Integer value for the Y coordinate. 
	 * @since 1.0
	 */
	public static void setRedSearchLowerLeftCorner(int x, int y) {
		int[] coordinate = {x, y};
		SearchRegion.redSearchLowerLeftCorner = coordinate;
	}
	/**
	 * Getter for the coordinates of the red search zone's upper right corner.
	 * @return Array that contains the X and Y coordinates in that order. 
	 * @since 1.0
	 */
	public static int[] getRedSearchUpperRightCorner() {
		return redSearchUpperRightCorner;
	}
	/**
	 * Mutator for the coordinates of the red search zone's upper right corner. Only used by the wifi class. 
	 * @param x Integer value for the X coordinate. 
	 * @param y Integer value for the Y coordinate. 
	 * @since 1.0
	 */
	public static void setRedSearchUpperRightCorner(int x, int y) {
		int[] coordinate = {x, y};
		SearchRegion.redSearchUpperRightCorner = coordinate;
	}
	/**
	 * Getter for the coordinates of the green search zone's lower left corner.
	 * @return Array that contains that X and Y coordinate in that order.
	 * @since 1.0
	 */
	public static int[] getGreenSearchLowerLeftCorner() {
		return greenSearchLowerLeftCorner;
	}
	/**
	 * Mutator for the coordinates of the green search zone's lower left corner. Only used by the wifi class. 
	 * @param x Integer value for the X coordinate. 
	 * @param y Integer value for the Y coordinate. 
	 * @since 1.0 
	 */
	public static void setGreenSearchLowerLeftCorner(int x, int y) {
		int[] coordinate = {x, y};
		SearchRegion.greenSearchLowerLeftCorner = coordinate;
	}
	/**
	 * Getter for the coordinates of the green search zone's upper right corner.
	 * @return Array that contains that X and Y coordinate in that order.
	 * @since 1.0 
	 */
	public static int[] getGreenSearchUpperRightCorner() {
		return greenSearchUpperRightCorner;
	}
	/**
	 * Mutator for the coordinates of the green search zone's upper right corner. Only used by the wifi class. 
	 * @param x Integer value for the X coordinate. 
	 * @param y Integer value for the X coordinate. 
	 * @since 1.0
	 */
	public static void setGreenSearchUpperRightCorner(int x, int y) {
		int[] coordinate = {x, y};
		SearchRegion.greenSearchUpperRightCorner = coordinate;
	}
	/**
	 * Getter for our team's lower left search corner. The one we want to search in. 
	 * @return Either the green search zone lower left corner or the red search zone lower left corner depending on the team's color. 
	 * @since 1.0 
	 */
	public static int[] getMySearchLowerLeftCorner() {
		if (Setting.getTeamColor() == Setting.TeamColor.Red) return SearchRegion.getGreenSearchLowerLeftCorner();
		if (Setting.getTeamColor() == Setting.TeamColor.Green) return SearchRegion.getRedSearchLowerLeftCorner();
		return null;
	}
	/**
	 * Getter for our team's upper right search corner. The one we want to search in. 
	 * @return Either the green search zone upper right corner or the red search zone upper right corner depending on the team's color. 
	 * @since 1.0 
	 */
	public static int[] getMySearchUpperRightCorner() {
		if (Setting.getTeamColor() == Setting.TeamColor.Red) return SearchRegion.getGreenSearchUpperRightCorner();
		if (Setting.getTeamColor() == Setting.TeamColor.Green) return SearchRegion.getRedSearchUpperRightCorner();
		return null;
	}
	/**
	 * Getter for the opposing team's lower left search corner. Our zone so it does not matter. 
	 * @return Either the green search zone lower left corner or the red search zone lower left corner depending on the team's color. 
	 * @since 1.0
	 */
	public static int[] getOpponentSearchLowerLeftCorner() {
		if (Setting.getTeamColor() == Setting.TeamColor.Red) return SearchRegion.getRedSearchLowerLeftCorner();
		if (Setting.getTeamColor() == Setting.TeamColor.Green) return SearchRegion.getGreenSearchLowerLeftCorner();
		return null;
	}
	/**
	 * Getter for the opposing team's upper right search corner. Our zone so it does not matter. 
	 * @return Either the green search zone upper right corner or the red search zone upper right corner depending on the team's color. 
	 * @since 1.0 
	 */
	public static int[] getOpponentSearchUpperRightCorner() {
		if (Setting.getTeamColor() == Setting.TeamColor.Red) return SearchRegion.getRedSearchUpperRightCorner();
		if (Setting.getTeamColor() == Setting.TeamColor.Green) return SearchRegion.getGreenSearchUpperRightCorner();
		return null;
	}
	
}
