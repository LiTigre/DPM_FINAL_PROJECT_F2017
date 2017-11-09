package ca.mcgill.ecse211.settings;

import ca.mcgill.ecse211.settings.Setting.TeamColor;

public class StartingZone {
	private static int[] redZoneUpperRightCorner;
	private static int[] redZoneLowerLeftCorner;
	
	private static int[] greenZoneUpperRightCorner;
	private static int[] greenZoneLowerLeftCorner;
	
	public static int[] getRedZoneUpperRightCorner() {
		return redZoneUpperRightCorner;
	}
	public static void setRedZoneUpperRightCorner(int x, int y) {
		int coordinate[] = {x, y};
		StartingZone.redZoneUpperRightCorner = coordinate;
	}
	public static int[] getRedZoneLowerLeftCorner() {
		return redZoneLowerLeftCorner;
	}
	public static void setRedZoneLowerLeftCorner(int x, int y) {
		int coordinate[] = {x, y};
		StartingZone.redZoneLowerLeftCorner = coordinate;
	}
	public static int[] getGreenZoneUpperRightCorner() {
		return greenZoneUpperRightCorner;
	}
	public static void setGreenZoneUpperRightCorner(int x, int y) {
		int coordinate[] = {x, y};
		StartingZone.greenZoneUpperRightCorner = coordinate;
	}
	public static int[] getGreenZoneLowerLeftCorner() {
		return greenZoneLowerLeftCorner;
	}
	public static void setGreenZoneLowerLeftCorner(int x, int y) {
		int coordinate[] = {x, y};
		StartingZone.greenZoneLowerLeftCorner = coordinate;
	}
	public static int[] getMyZoneUpperRightCorner() {
		if (Setting.getTeamColor() == TeamColor.Red) return redZoneUpperRightCorner;
		if (Setting.getTeamColor() == TeamColor.Green) return greenZoneUpperRightCorner;
		return null;
	}
	public static int[] getMyZoneLowerLeftCorner() {
		if (Setting.getTeamColor() == TeamColor.Red) return redZoneLowerLeftCorner;
		if (Setting.getTeamColor() == TeamColor.Green) return greenZoneLowerLeftCorner;
		return null;
	}
	public static int[] getOpponentZoneUpperRightCorner() {
		if (Setting.getTeamColor() == TeamColor.Red) return greenZoneUpperRightCorner;
		if (Setting.getTeamColor() == TeamColor.Green) return redZoneUpperRightCorner;
		return null;
	}
	public static int[] getOpponentZoneLowerLeftCorner() {
		if (Setting.getTeamColor() == TeamColor.Red) return greenZoneLowerLeftCorner;
		if (Setting.getTeamColor() == TeamColor.Green) return redZoneLowerLeftCorner;
		return null;
	}
}
