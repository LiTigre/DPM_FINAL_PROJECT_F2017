package ca.mcgill.ecse211.settings;

public class Setting {
	public static enum TeamColor {Red, Green};
	public static enum BlockColor {Red, Blue, Yellow, White};
	
	private static TeamColor teamColor; 
	private static BlockColor opponentFlagColor;
	
	private static int startingCorner;
	
	private static int[] ziplineStart;
	private static int[] ziplineEnd;
	
	
	public static TeamColor getTeamColor() {
		return teamColor;
	}
	public static void setTeamColor(TeamColor teamColor) {
		Setting.teamColor = teamColor;
	}
	public static BlockColor getOpponentFlagColor() {
		return opponentFlagColor;
	}
	public static void setOpponentFlagColor(int n) {
		if (n == 1) Setting.opponentFlagColor = BlockColor.Red;
		if (n == 2) Setting.opponentFlagColor = BlockColor.Blue;
		if (n == 3) Setting.opponentFlagColor = BlockColor.Yellow;
		if (n == 4) Setting.opponentFlagColor = BlockColor.White;
	}
	public static int getStartingCorner() {
		return startingCorner;
	}
	public static void setStartingCorner(int startingCorner) {
		Setting.startingCorner = startingCorner;
	}
	public static int[] getZiplineStart() {
		return ziplineStart;
	}
	public static void setZiplineStart(int x, int y) {
		int coordinate[] = {x, y};
		Setting.ziplineStart = coordinate;
	}
	public static int[] getZiplineEnd() {
		return ziplineEnd;
	}
	public static void setZiplineEnd(int x, int y) {
		int coordinate[] = {x, y};
		Setting.ziplineEnd = coordinate;
	}
	
}
