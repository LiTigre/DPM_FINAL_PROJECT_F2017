package ca.mcgill.ecse211.settings;

public class ShallowZone {
	private static int[] horizontalLowerLeftCorner;
	private static int[] horizontalUpperRightCorner;
	private static int[] verticalLowerLeftCorner;
	private static int[] verticalUpperRightCorner;
	
	public static int[] getHorizontalLowerLeftCorner() {
		return horizontalLowerLeftCorner;
	}
	public static void setHorizontalLowerLeftCorner(int x, int y) {
		int[] coordinate = {x, y};
		ShallowZone.horizontalLowerLeftCorner = coordinate;
	}
	public static int[] getHorizontalUpperRightCorner() {
		return horizontalUpperRightCorner;
	}
	public static void setHorizontalUpperRightCorner(int x, int y) {
		int[] coordinate = {x, y};
		ShallowZone.horizontalUpperRightCorner = coordinate;
	}
	public static int[] getVerticalLowerLeftCorner() {
		return verticalLowerLeftCorner;
	}
	public static void setVerticalLowerLeftCorner(int x, int y) {
		int[] coordinate = {x, y};
		ShallowZone.verticalLowerLeftCorner = coordinate;
	}
	public static int[] getVerticalUpperRightCorner() {
		return verticalUpperRightCorner;
	}
	public static void setVerticalUpperRightCorner(int x, int y) {
		int[] coordinate = {x, y};
		ShallowZone.verticalUpperRightCorner = coordinate;
	}
}
