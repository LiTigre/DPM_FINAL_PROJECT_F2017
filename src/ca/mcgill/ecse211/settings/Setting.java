package ca.mcgill.ecse211.settings;

public class Setting {
	private enum TeamColor {Red, Green};
	private enum BlockColor {Red, Blue, Yellow, White};
	
	private TeamColor teamColor; 
	private BlockColor flagColor;
	
	private int startingCorner;
	
	private int[] ziplineStart;
	private int[] ziplineEnd;
}
