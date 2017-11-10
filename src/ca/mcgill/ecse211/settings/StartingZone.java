package ca.mcgill.ecse211.settings;

import ca.mcgill.ecse211.settings.Setting.TeamColor;

/**
 * All information related to the two zones of the board (red and green). Received from the server. 
 * @author Team 2
 * @version 1.0
 * @since 1.5
 */
public class StartingZone {
	// Constants
	/** X and Y coordinates of the red zone's upper right corner. */
	private static int[] redZoneUpperRightCorner;
	/**  X and Y coordinates of the red zone's lower left corner. */
	private static int[] redZoneLowerLeftCorner;
	
	/** X and Y coordinates of the green zone's upper right corner. */
	private static int[] greenZoneUpperRightCorner;
	/**  X and Y coordinates of the green zone's lower left corner. */
	private static int[] greenZoneLowerLeftCorner;
	
	
	// Methods
	/**
	 * Getter for the coordinates of the red zone's upper right corner.
	 * @return Array that contains the X and Y coordinates in that order. 
	 * @since 1.0
	 */
	public static int[] getRedZoneUpperRightCorner() {
		return redZoneUpperRightCorner;
	}
	/**
	 * Mutator for the coordinates of the red zone's upper right corner. Only used by the wifi class. 
	 * @param x Integer value for the X coordinate. 
	 * @param y Integer value for the Y coordinate. 
	 * @since 1.0
	 */
	public static void setRedZoneUpperRightCorner(int x, int y) {
		int coordinate[] = {x, y};
		StartingZone.redZoneUpperRightCorner = coordinate;
	}
	/**
	 * Getter for the coordinates of the red zone's lower left corner.
	 * @return Array that contains that X and Y coordinate in that order.
	 * @since 1.0
	 */
	public static int[] getRedZoneLowerLeftCorner() {
		return redZoneLowerLeftCorner;
	}
	/**
	 * Mutator for the coordinates of the red zone's lower left corner. Only used by the wifi class. 
	 * @param x Integer value for the X coordinate. 
	 * @param y Integer value for the Y coordinate. 
	 * @since 1.0
	 */
	public static void setRedZoneLowerLeftCorner(int x, int y) {
		int coordinate[] = {x, y};
		StartingZone.redZoneLowerLeftCorner = coordinate;
	}
	/**
	 * Getter for the coordinates of the green zone's upper right corner.
	 * @return Array that contains that X and Y coordinate in that order.
	 * @since 1.0 
	 */
	public static int[] getGreenZoneUpperRightCorner() {
		return greenZoneUpperRightCorner;
	}
	/**
	 * Mutator for the coordinates of the green zone's upper right corner. Only used by the wifi class. 
	 * @param x Integer value for the X coordinate. 
	 * @param y Integer value for the X coordinate. 
	 * @since 1.0
	 */
	public static void setGreenZoneUpperRightCorner(int x, int y) {
		int coordinate[] = {x, y};
		StartingZone.greenZoneUpperRightCorner = coordinate;
	}
	/**
	 * Getter for the coordinates of the green zone's lower left corner.
	 * @return Array that contains that X and Y coordinate in that order.
	 * @since 1.0
	 */
	public static int[] getGreenZoneLowerLeftCorner() {
		return greenZoneLowerLeftCorner;
	}
	/**
	 * Mutator for the coordinates of the green zone's lower left corner. Only used by the wifi class. 
	 * @param x Integer value for the X coordinate. 
	 * @param y Integer value for the Y coordinate. 
	 * @since 1.0 
	 */
	public static void setGreenZoneLowerLeftCorner(int x, int y) {
		int coordinate[] = {x, y};
		StartingZone.greenZoneLowerLeftCorner = coordinate;
	}
	/**
	 * Getter for the team's upper right corner.
	 * @return Either the green zone upper right corner or the red zone upper right corner depending on the team's color. 
	 * @since 1.0 
	 */
	public static int[] getMyZoneUpperRightCorner() {
		if (Setting.getTeamColor() == TeamColor.Red) return redZoneUpperRightCorner;
		if (Setting.getTeamColor() == TeamColor.Green) return greenZoneUpperRightCorner;
		return null;
	}
	/**
	 * Getter for the team's lower left corner. Only used by the wifi class. 
	 * @return Either the green zone lower left corner or the red zone lower left corner depending on the team's color. 
	 * @since 1.0 
	 */
	public static int[] getMyZoneLowerLeftCorner() {
		if (Setting.getTeamColor() == TeamColor.Red) return redZoneLowerLeftCorner;
		if (Setting.getTeamColor() == TeamColor.Green) return greenZoneLowerLeftCorner;
		return null;
	}
	/**
	 * Getter for the opposing team's upper right corner. 
	 * @return Either the green zone upper right corner or the red zone upper right corner depending on the team's color. 
	 * @since 1.0 
	 */
	public static int[] getOpponentZoneUpperRightCorner() {
		if (Setting.getTeamColor() == TeamColor.Red) return greenZoneUpperRightCorner;
		if (Setting.getTeamColor() == TeamColor.Green) return redZoneUpperRightCorner;
		return null;
	}
	/**
	 * Getter for the opposing team's lower left corner. 
	 * @return Either the green zone lower left corner or the red zone lower left corner depending on the team's color. 
	 * @since 1.0
	 */
	public static int[] getOpponentZoneLowerLeftCorner() {
		if (Setting.getTeamColor() == TeamColor.Red) return greenZoneLowerLeftCorner;
		if (Setting.getTeamColor() == TeamColor.Green) return redZoneLowerLeftCorner;
		return null;
	}
}
