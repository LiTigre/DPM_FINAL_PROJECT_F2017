package ca.mcgill.ecse211.settings;

/**
 * General information related to the game. Received from the server. 
 * @author Team 2
 * @version 1.0
 * @since 1.5
 */
public class Setting {
	// Enumerations
	/** Enumeration of all possible team colors (2). */
	public static enum TeamColor {Red, Green};
	/** Enumeration of all possible block colors (4). */
	public static enum BlockColor {Red, Blue, Yellow, White};
	
	
	// Constants
	/** Variable that will hold the team's color that was given from the server. */
	private static TeamColor teamColor; 
	/** Variable that indicates the color of the flag that must be captured. */
	private static BlockColor opponentFlagColor;
	
	/** Indicates which corner the robot will be starting in. Values = 0-3; */
	private static int startingCorner;
	
	/** Array that holds the X and Y coordinates of the beginning of the zipline. [0] = X; [1] = Y; */
	private static int[] ziplineStart;
	/** X and Y coordinates of the end of the zipline. [0] = X; [1] = Y */
	private static int[] ziplineEnd;
	
	
	// Methods
	/**
	 * Getter for the team's color.
	 * @return The team's color. TeamColor enum value.
	 * @since 1.0
	 */
	public static TeamColor getTeamColor() {
		return teamColor;
	}
	/**
	 * Mutator for the team's color. Used only by the wifi class. 
	 * @param teamColor The color being assigned. TeamColor enum value. 
	 * @since 1.0
	 */
	public static void setTeamColor(TeamColor teamColor) {
		Setting.teamColor = teamColor;
	}
	/**
	 * Getter for the block's (flag) color. 
	 * @return The block's color. This is the flag the robot wants to capture. BlockColor enum value. 
	 * @since 1.0
	 */
	public static BlockColor getOpponentFlagColor() {
		return opponentFlagColor;
	}
	/**
	 * Mutator for the block's color. Used only by the wifi class. 
	 * @param n INteger value that is associated to the color. Method converts the integer to the BlockColor enum. 
	 * @since 1.0
	 */
	public static void setOpponentFlagColor(int n) {
		if (n == 1) Setting.opponentFlagColor = BlockColor.Red;
		if (n == 2) Setting.opponentFlagColor = BlockColor.Blue;
		if (n == 3) Setting.opponentFlagColor = BlockColor.Yellow;
		if (n == 4) Setting.opponentFlagColor = BlockColor.White;
	}
	/**
	 * Getter for the starting corner of the robot. 
	 * @return The starting corner of the robot. Value from 0-3
	 * @since 1.0
	 */
	public static int getStartingCorner() {
		return startingCorner;
	}
	/**
	 * Mutator for the starting corner of the robot. Used only by the wifi class. 
	 * @param startingCorner Integer value from 0 to 3 that indicated one of 4 corners. 
	 * @since 1.0 
	 */
	public static void setStartingCorner(int startingCorner) {
		Setting.startingCorner = startingCorner;
	}
	/**
	 * Getter for the starting point of the zipline. 
	 * @return An array that holds the X and Y coordinates of the starting position of the zipline in that order. 
	 * @since 1.0
	 */
	public static int[] getZiplineStart() {
		return ziplineStart;
	}
	/**
	 * Mutator for the zipline starting coordinates. Only used by the wifi class. 
	 * @param x Integer value of the X coordinate.
	 * @param y Integer value of the Y coordinate. 
	 * @since 1.0 
	 */
	public static void setZiplineStart(int x, int y) {
		int coordinate[] = {x, y};
		Setting.ziplineStart = coordinate;
	}
	/**
	 * Getter for the end point of the zipline. 
	 * @return An array that holds the X and Y coordinates of the ending position of the zipline in that order. 
	 * @since 1.0 
	 */
	public static int[] getZiplineEnd() {
		return ziplineEnd;
	}
	/**
	 * Mutator for the zipline ending coordinates. Only used by the wifi class. 
	 * @param x Integer value of the X coordinate. 
	 * @param y Integer value of the Y coordinate. 
	 * @since 1.0 
	 */
	public static void setZiplineEnd(int x, int y) {
		int coordinate[] = {x, y};
		Setting.ziplineEnd = coordinate;
	}
	
}
