package ca.mcgill.ecse211.settings;

public class SearchRegion {

	private static int[] redLowerLeftCorner;
	private static int[] redUpperRightCorner;
	private static int[] greenLowerLeftCorner;
	private static int[] greenUpperRightCorner;
	
	public static int[] getRedLowerLeftCorner() {
		return redLowerLeftCorner;
	}
	public static void setRedLowerLeftCorner(int x, int y) {
		int[] coordinate = {x, y};
		SearchRegion.redLowerLeftCorner = coordinate;
	}
	public static int[] getRedUpperRightCorner() {
		return redUpperRightCorner;
	}
	public static void setRedUpperRightCorner(int x, int y) {
		int[] coordinate = {x, y};
		SearchRegion.redUpperRightCorner = coordinate;
	}
	public static int[] getGreenLowerLeftCorner() {
		return greenLowerLeftCorner;
	}
	public static void setGreenLowerLeftCorner(int x, int y) {
		int[] coordinate = {x, y};
		SearchRegion.greenLowerLeftCorner = coordinate;
	}
	public static int[] getGreenUpperRightCorner() {
		return greenUpperRightCorner;
	}
	public static void setGreenUpperRightCorner(int x, int y) {
		int[] coordinate = {x, y};
		SearchRegion.greenUpperRightCorner = coordinate;
	}
	public static int[] getMyLowerLeftCorner() {
		if (Setting.getTeamColor() == Setting.TeamColor.Red) return SearchRegion.getGreenLowerLeftCorner();
		if (Setting.getTeamColor() == Setting.TeamColor.Green) return SearchRegion.getRedLowerLeftCorner();
		return null;
	}
	public static int[] getMyUpperRightCorner() {
		if (Setting.getTeamColor() == Setting.TeamColor.Red) return SearchRegion.getGreenUpperRightCorner();
		if (Setting.getTeamColor() == Setting.TeamColor.Green) return SearchRegion.getRedUpperRightCorner();
		return null;
	}
	public static int[] getOpponentLowerLeftCorner() {
		if (Setting.getTeamColor() == Setting.TeamColor.Red) return SearchRegion.getRedLowerLeftCorner();
		if (Setting.getTeamColor() == Setting.TeamColor.Green) return SearchRegion.getGreenLowerLeftCorner();
		return null;
	}
	public static int[] getOpponentUpperRightCorner() {
		if (Setting.getTeamColor() == Setting.TeamColor.Red) return SearchRegion.getRedUpperRightCorner();
		if (Setting.getTeamColor() == Setting.TeamColor.Green) return SearchRegion.getGreenUpperRightCorner();
		return null;
	}
	
}
