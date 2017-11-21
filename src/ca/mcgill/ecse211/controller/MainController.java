package ca.mcgill.ecse211.controller;

import ca.mcgill.ecse211.navigation.Driver;
import ca.mcgill.ecse211.navigation.ObstacleAvoidance;
import ca.mcgill.ecse211.navigation.Search;
import ca.mcgill.ecse211.navigation.Zipline;
import ca.mcgill.ecse211.odometry.Localization;
import ca.mcgill.ecse211.odometry.Odometer;
import ca.mcgill.ecse211.odometry.OdometryCorrection;
import ca.mcgill.ecse211.settings.SearchRegion;
import ca.mcgill.ecse211.settings.Setting;
import ca.mcgill.ecse211.settings.Setting.TeamColor;
import ca.mcgill.ecse211.settings.ShallowZone;
import ca.mcgill.ecse211.settings.StartingZone;
import ca.mcgill.ecse211.wifi.WifiInput;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

/**
 * Controls all actions taken by the robot. Is in charge of sequencing operations 
 * in order to do what the tasks requires the robot to do. 
 * @author Team 2
 * @version 1.3
 * @since 1.0
 */
public class MainController {
	
	// Constants 
	/** The radius of the robot's wheels in cm */
	//public static final double WHEEL_RADIUS = 2.1;
	public static final double WHEEL_RADIUS = 2.063;
	/** The length of the robot's track in cm. */
	//public static final double TRACK = 11;
	public static final double TRACK = 11.3;
	/** Distance from the color sensor to the middle of the track in cm */
	public static final double SENSOR_TO_TRACK = 15.4;
	/** Value that indicates a black line. */
	public static final double LINE_THRESHOLD = 450;
	/** Value of the length of a block in cm */
	public static final double GRID_LENGTH = 30.48; 
	
	
	// Sensors
	/**  Color sensor with associated port. */
	private static final EV3ColorSensor lightSensor = new EV3ColorSensor(LocalEV3.get().getPort("S3"));
	/** Color sensor used for angle correction with associated port. */
	//private static final EV3ColorSensor angleColorSensor = new EV3ColorSensor(LocalEV3.get().getPort("S2"));
	/** Ultrasonic sensor with associated port. */
	private static final SensorModes usSensor = new EV3UltrasonicSensor(LocalEV3.get().getPort("S1"));
	
	
	// Motors
	/** Left motor with associated port. */
	public static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
	/** Right motor with associated port. */
	public static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("B"));
	/** Zipline motor with associated port. */
	static final EV3LargeRegulatedMotor ziplineMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	
	
	// Make it that we can collect data from the sensor
	/** Data collected from the color sensor. */
	private static SampleProvider lightSample = lightSensor.getRedMode();
	/** Data collected from the angle color sensor. */
//	private static SampleProvider angleColorSample = angleColorSensor.getRedMode();
	/** Data collected from the ultrasonic sensor. */
	private static SampleProvider usDistance = usSensor.getMode("Distance");

	
	// Data from the sensors
	/** Array of floats that stores the value of the data from the color sensor. */
	private static float lightData[] = new float[lightSample.sampleSize()];
	/** Array of floats that stores the value of the data from the color sensor. */
	//private static float angleLightData[] = new float[angleColorSample.sampleSize()];
	/** Array of floats that stores the value of the data from the ultrasonic sensor. */
	private static float usData[] = new float[usDistance.sampleSize()];
	
	
	// Variables
	/** The X coordinate from where robot began its travel from. */
	private static double previousX;
	/** The Y coordinate from where robot began its travel from. */
	private static double previousY;
	/** The X coordinate to where the robot is supposed to stop its traveling at. */
	private static double futureX;
	/** The Y coordinate to where the robot is supposed to stop its traveling at. */
	private static double futureY;
	/** The X coordinate to where the robot started from. */
	private static double startingX;
	/** The Y coordinate to where the robot started from. */
	private static double startingY;
	
	/**
	 * Runs capture the flag.
	 * @param args required to make this the main method of the system. 
	 * @since 1.1
	 */
	public static void main(String[] args) {
		// Object creation 
		Odometer odometer = new Odometer(leftMotor, rightMotor);
		Driver driver = new Driver(leftMotor, rightMotor, odometer);
		Localization localization = new Localization(odometer, driver);
		OdometryCorrection odoCorrection = new OdometryCorrection(odometer, driver, localization);
		Zipline zipline = new Zipline(ziplineMotor, driver); 
		
		// First wait for server to send info.
		WifiInput.recieveServerData();
		int preZip[] = Setting.getStartPointNearZipline();
		int zipStart[] = Setting.getZiplineStart();
		int postZip[] = Setting.getEndPointNearZipline(); 
		int zipEnd[] = Setting.getZiplineEnd();
		int upperFlag[] = SearchRegion.getMySearchUpperRightCorner();
		int lowerFlag[] = SearchRegion.getMySearchLowerLeftCorner();
		int verticalLowerShallowPath[] = ShallowZone.getVerticalLowerLeftCorner();
		int verticalUpperShallowPath[] = ShallowZone.getVerticalUpperRightCorner();
		int horizontalLowerShallowPath[] = ShallowZone.getHorizontalLowerLeftCorner();
		int horizontalUpperShallowPath[] = ShallowZone.getHorizontalUpperRightCorner();
		int horizontalRedZone[] = StartingZone.getRedZoneLowerLeftCorner();
		int verticalRedZone[] = StartingZone.getRedZoneUpperRightCorner();
		
		// Shallow path
		double upperRightSquareX = Math.min(verticalUpperShallowPath[0], horizontalUpperShallowPath[0]);
		double upperRightSquareY = Math.min(verticalUpperShallowPath[1], horizontalUpperShallowPath[1]);
					
		double lowerLeftSquareX = Math.max(verticalLowerShallowPath[0], horizontalLowerShallowPath[0]);
		double lowerLeftSquareY = Math.max(verticalLowerShallowPath[1], horizontalLowerShallowPath[1]);
					
		double middleSquareX = (upperRightSquareX + lowerLeftSquareX)/2;
		double middleSquareY = (upperRightSquareY + lowerLeftSquareY)/2;
		
		odometer.start();
		
		localization.localize();
		
		// Setting the odometer to the right corner
		if(Setting.getStartingCorner() == 1){
			odometer.setPosition(new double[] {odometer.getX()+11*GRID_LENGTH, odometer.getY()+GRID_LENGTH, odometer.getTheta()+270}, new boolean[] {true, true, true});
			startingX = 11;
			startingY = 1;
		}
		else if(Setting.getStartingCorner() == 2) {
			odometer.setPosition(new double[] {odometer.getX()+11*GRID_LENGTH, odometer.getY()+11*GRID_LENGTH, odometer.getTheta()+180}, new boolean[] {true, true, true});
			startingX = 11;
			startingY = 11;
		}
		else if(Setting.getStartingCorner() == 3){
			odometer.setPosition(new double[] {odometer.getX()+GRID_LENGTH, odometer.getY()+11*GRID_LENGTH, odometer.getTheta()+90}, new boolean[] {true, true, true});
			startingX = 1;
			startingY = 11;
		}
		else{
			odometer.setPosition(new double[] {odometer.getX()+GRID_LENGTH, odometer.getY()+GRID_LENGTH, odometer.getTheta()}, new boolean[] {true, true, true});
			startingX = 1;
			startingY = 1; 
		} 
		
		// Do zipline first 
		if(Setting.getTeamColor() == TeamColor.Green) {
			// Set the pre zip point 
			futureX = preZip[0];
			futureY = preZip[1];
			
			// Travel to the pre zipline point
			driver.travelTo((preZip[0]*GRID_LENGTH), (preZip[1]*GRID_LENGTH));
			
			// Travel to the pre zipline point
			driver.travelTo((preZip[0]*GRID_LENGTH), (preZip[1]*GRID_LENGTH));
			localization.reLocalize((preZip[0]*GRID_LENGTH), (preZip[1]*GRID_LENGTH));
			driver.travelTo((preZip[0]*GRID_LENGTH), (preZip[1]*GRID_LENGTH));
			
			//Travel to the zipline point 
			driver.turnTo(zipStart[0]*GRID_LENGTH, zipStart[1]*GRID_LENGTH);
			//double preZipTheta = odometer.getTheta();
			while(driver.getWheelsMoving());
			
			//driver.turnDistance(10);
			//driver.turnDistance(-13);
			while(driver.getWheelsMoving());
			driver.travelDistance(40);
			
			// Perform zipline
			zipline.performZiplineTravel();
			while(driver.getWheelsMoving());
			
			if(zipEnd[0] == zipStart[0] && zipEnd[1] < zipStart[1]) {				//negative y
				odometer.setTheta(180);
			}
			else if (zipEnd[0] == zipStart[0] && zipEnd[1] > zipStart[1]) {		//positive y
				odometer.setTheta(1);
			}
			else if(zipEnd[1] == zipStart[1] && zipEnd[0] < zipStart[0]) {		//negative x
				odometer.setTheta(270);
			}
			else if (zipEnd[1] == zipStart[1] && zipEnd[0] > zipStart[0]) {		//positive x
				odometer.setTheta(90);
			}
			else if (zipEnd[0] - zipStart[0] > 0 && zipEnd[1] - zipStart[1] > 0) {	//top right
				odometer.setTheta(45);
			}
			else if (zipEnd[0] - zipStart[0] > 0 && zipEnd[1] - zipStart[1] < 0) {	//bottom right
				odometer.setTheta(135);
			}
			else if (zipEnd[0] - zipStart[0] < 0 && zipEnd[1] - zipStart[1] > 0 ) {	//top left
				odometer.setTheta(315);
			}
			else if (zipEnd[0] - zipStart[0] < 0 && zipEnd[1] - zipStart[1] > 0 ) {	//bottom left
				odometer.setTheta(225);
			}
			
			// Figure out where it is after zipline
			localization.reLocalize(postZip[0]*GRID_LENGTH, postZip[1]*GRID_LENGTH); 
			
			// Set post zip point and region to go to 
			previousX = postZip[0];
			previousY = postZip[1];
			futureX = upperFlag[0];
			futureY = upperFlag[1];
			
			// Travel to the flag location
			driver.travelTo(upperFlag[0]*GRID_LENGTH, upperFlag[1]*GRID_LENGTH);
			
			// SEARCH HERE
			
			// Shallow path 
			if(horizontalRedZone[0] < middleSquareX && verticalRedZone[0] < middleSquareX ) {
				// Go to middle x first
				driver.travelTo(middleSquareX, previousY);
				driver.travelTo(middleSquareX, middleSquareY);
			}
			else {
				// Go to middle y first 
				driver.travelTo(previousX, middleSquareY);
				driver.travelTo(middleSquareX, middleSquareY);
			}
			
			driver.travelTo(startingX*GRID_LENGTH, startingY*GRID_LENGTH);
		}
		// Do shallow path first
		else {
			
			if(horizontalRedZone[0] < middleSquareX && verticalRedZone[0] < middleSquareX ) {
				// Go to middle x first
				driver.travelTo(middleSquareX, previousY);
				driver.travelTo(middleSquareX, middleSquareY);
			}
			else {
				// Go to middle y first 
				driver.travelTo(previousX, middleSquareY);
				driver.travelTo(middleSquareX, middleSquareY);
			}
			
			// SEARCH HERE
			
			// Set the pre zip point 
			futureX = preZip[0];
			futureY = preZip[1];
						
			// Travel to the pre zipline point
			driver.travelTo((preZip[0]*GRID_LENGTH), (preZip[1]*GRID_LENGTH));
						
			// Travel to the pre zipline point
			driver.travelTo((preZip[0]*GRID_LENGTH), (preZip[1]*GRID_LENGTH));
			localization.reLocalize((preZip[0]*GRID_LENGTH), (preZip[1]*GRID_LENGTH));
			driver.travelTo((preZip[0]*GRID_LENGTH), (preZip[1]*GRID_LENGTH));
						
			//Travel to the zipline point 
			driver.turnTo(zipStart[0]*GRID_LENGTH, zipStart[1]*GRID_LENGTH);
			//double preZipTheta = odometer.getTheta();
			while(driver.getWheelsMoving());
						
			//driver.turnDistance(10);
			//driver.turnDistance(-13);
			while(driver.getWheelsMoving());
			driver.travelDistance(40);
						
			// Perform zipline
			zipline.performZiplineTravel();
			while(driver.getWheelsMoving());
						
			if(zipEnd[0] == zipStart[0] && zipEnd[1] < zipStart[1]) {				//negative y
				odometer.setTheta(180);
			}
			else if (zipEnd[0] == zipStart[0] && zipEnd[1] > zipStart[1]) {		//positive y
				odometer.setTheta(1);
			}
			else if(zipEnd[1] == zipStart[1] && zipEnd[0] < zipStart[0]) {		//negative x
				odometer.setTheta(270);
			}
			else if (zipEnd[1] == zipStart[1] && zipEnd[0] > zipStart[0]) {		//positive x
				odometer.setTheta(90);
			}
			else if (zipEnd[0] - zipStart[0] > 0 && zipEnd[1] - zipStart[1] > 0) {	//top right
				odometer.setTheta(45);
			}
			else if (zipEnd[0] - zipStart[0] > 0 && zipEnd[1] - zipStart[1] < 0) {	//bottom right
				odometer.setTheta(135);
			}
			else if (zipEnd[0] - zipStart[0] < 0 && zipEnd[1] - zipStart[1] > 0 ) {	//top left
				odometer.setTheta(315);
			}
			else if (zipEnd[0] - zipStart[0] < 0 && zipEnd[1] - zipStart[1] > 0 ) {	//bottom left
				odometer.setTheta(225);
			}
						
			// Figure out where it is after zipline
			localization.reLocalize(postZip[0]*GRID_LENGTH, postZip[1]*GRID_LENGTH); 
			
			// Travel to the starting corner
			driver.travelTo(startingX*GRID_LENGTH, startingY*GRID_LENGTH);
		}
		

		
		// Conditions to not run into the zipline
//		if(zipEnd[0] == zipStart[0] ) {
//			//Vertical zipline alignment. Do Y first 
//			while(Math.abs(futureY-previousY)!= 1 && Math.abs(futureY-previousY)!= 2 && Math.abs(futureY-previousY)!= 0) {
//				if(Setting.getStartingCorner() == 0 || Setting.getStartingCorner() == 1) {
//					previousY += 2;
//				}
//				else {
//					previousY -= 2;
//				}
//				driver.travelTo(previousX*GRID_LENGTH, previousY*GRID_LENGTH);
//				localization.reLocalize(previousX*GRID_LENGTH, previousY*GRID_LENGTH);
//			}
//			
//			if(Math.abs(futureY-previousY)!= 0) {
//				driver.travelTo(previousX*GRID_LENGTH, preZip[1]*GRID_LENGTH);
//				localization.reLocalize(previousX*GRID_LENGTH, preZip[1]*GRID_LENGTH);
//			}
//			
//			//Then X
//			while(Math.abs(futureX-previousX)!= 1 && Math.abs(futureX-previousX)!= 2 && Math.abs(futureX-previousX)!= 0) {
//				if(Setting.getStartingCorner() == 0 || Setting.getStartingCorner() == 3) {
//					previousX += 2;
//				}
//				else {
//					previousX -= 2;
//				}
//				driver.travelTo(previousX*GRID_LENGTH, previousY*GRID_LENGTH);
//				localization.reLocalize(previousX*GRID_LENGTH, previousY*GRID_LENGTH);
//			}
//		}
//		else {
//			//Horizontal zipline alignment. Do X first
//			while(Math.abs(futureX-previousX)!= 1 && Math.abs(futureX-previousX)!= 2 && Math.abs(futureX-previousX)!= 0) {
//				if(Setting.getStartingCorner() == 0 || Setting.getStartingCorner() == 3) {
//					previousX += 2;
//				}
//				else {
//					previousX -= 2;
//				}
//				driver.travelTo(previousX*GRID_LENGTH, previousY*GRID_LENGTH);
//				localization.reLocalize(previousX*GRID_LENGTH, previousY*GRID_LENGTH);
//			}
//			
//			if(Math.abs(futureX-previousX)!= 0) {
//				driver.travelTo((preZip[0]*GRID_LENGTH), previousY*GRID_LENGTH);
//				localization.reLocalize((preZip[0]*GRID_LENGTH), previousY*GRID_LENGTH);
//			}
//			
//			//Then Y
//			while(Math.abs(futureY-previousY)!= 1 && Math.abs(futureY-previousY)!= 2 && Math.abs(futureY-previousY)!= 0) {
//				if(Setting.getStartingCorner() == 0 || Setting.getStartingCorner() == 1) {
//					previousY += 2;
//				}
//				else {
//					previousY -= 2;
//				}
//				driver.travelTo((preZip[0]*GRID_LENGTH), previousY*GRID_LENGTH);
//				localization.reLocalize((preZip[0]*GRID_LENGTH), previousY*GRID_LENGTH);
//			}
//		}
//		
		
		// Conditions to not run into the zipline
//				if(zipEnd[1] == zipStart[1] ) {
//					//Vertical zipline alignment. Do Y first 
//					while(Math.abs(futureY-previousY)!= 1 && Math.abs(futureY-previousY)!= 2 && Math.abs(futureY-previousY)!= 0) {
//						if(Setting.getStartingCorner() == 0 || Setting.getStartingCorner() == 1) {
//							previousY += 2;
//						}
//						else {
//							previousY -= 2;
//						}
//						driver.travelTo(previousX*GRID_LENGTH, previousY*GRID_LENGTH);
//						localization.reLocalize(previousX*GRID_LENGTH, previousY*GRID_LENGTH);
//					}
//					
//					if(Math.abs(futureY-previousY)!= 0) {
//						driver.travelTo(previousX*GRID_LENGTH, upperFlag[1]*GRID_LENGTH);
//						localization.reLocalize(previousX*GRID_LENGTH, upperFlag[1]*GRID_LENGTH);
//					}
//					
//					//Then X
//					while(Math.abs(futureX-previousX)!= 1 && Math.abs(futureX-previousX)!= 2 && Math.abs(futureX-previousX)!= 0) {
//						if(Setting.getStartingCorner() == 0 || Setting.getStartingCorner() == 3) {
//							previousX += 2;
//						}
//						else {
//							previousX -= 2;
//						}
//						driver.travelTo(previousX*GRID_LENGTH, previousY*GRID_LENGTH);
//						localization.reLocalize(previousX*GRID_LENGTH, previousY*GRID_LENGTH);
//					}
//				}
//				else {
//					//Horizontal zipline alignment. Do X first
//					while(Math.abs(futureX-previousX)!= 1 && Math.abs(futureX-previousX)!= 2 && Math.abs(futureX-previousX)!= 0) {
//						if(Setting.getStartingCorner() == 0 || Setting.getStartingCorner() == 3) {
//							previousX += 2;
//						}
//						else {
//							previousX -= 2;
//						}
//						driver.travelTo(previousX*GRID_LENGTH, previousY*GRID_LENGTH);
//						localization.reLocalize(previousX*GRID_LENGTH, previousY*GRID_LENGTH);
//					}
//					
//					if(Math.abs(futureX-previousX)!= 0) {
//						driver.travelTo((upperFlag[0]*GRID_LENGTH), previousY*GRID_LENGTH);
//						localization.reLocalize((upperFlag[0]*GRID_LENGTH), previousY*GRID_LENGTH);
//					}
//					
//					//Then Y
//					while(Math.abs(futureY-previousY)!= 1 && Math.abs(futureY-previousY)!= 2 && Math.abs(futureY-previousY)!= 0) {
//						if(Setting.getStartingCorner() == 0 || Setting.getStartingCorner() == 1) {
//							previousY += 2;
//						}
//						else {
//							previousY -= 2;
//						}
//						driver.travelTo((preZip[0]*GRID_LENGTH), previousY*GRID_LENGTH);
//						localization.reLocalize((preZip[0]*GRID_LENGTH), previousY*GRID_LENGTH);
//					}
//				}
	}
	
	/**
	 * Sets the X and Y coordinates of the previous point it was at. 
	 * @param lastX X value of the previous coordinate.
	 * @param lastY Y value of the previous coordinate.
	 * @since 1.3
	 */
	private static void setPreviousCoordinates(double lastX, double lastY) {
		previousX = lastX;
		previousY = lastY;
	}

	/**
	 * Returns the previous X coordinates. 
	 * @return The previous X value. 
	 * @since 1.3
	 */
	public static double getPreviousX() {
		return previousX;
	}
	
	/**
	 * Returns the previous Y coordinates. 
	 * @return The previous Y value. 
	 * @since 1.3
	 */
	public static double getPreviousY() {
		return previousY;
	}
	
	/**
	 * Sets the X and Y coordinates of the next point it is going to. 
	 * @param nextX X value of the next coordinate.
	 * @param nextY Y value of the next coordinate.
	 * @since 1.3
	 */
	private static void setFutureCoordinates(double nextX, double nextY) {
		futureX = nextX;
		futureY = nextY;
	}
	
	/**
	 * Returns the next X coordinates. 
	 * @return The previous X value. 
	 * @since 1.3
	 */
	public static double getFutureX() {
		return futureX;
	}
	
	/**
	 * Returns the next Y coordinates. 
	 * @return The previous Y value. 
	 * @since 1.3
	 */
	public static double getFutureY() {
		return futureY;
	}
	
	/**
	 * Gets the distance reading of the ultrasonic sensor.
	 * @return The ultrasonic sensor reading multiplied by 100 for precision. 
	 * @since 1.1
	 */
	public static float getDistanceValue(){
		usDistance.fetchSample(usData, 0);
		return usData[0]*100;
	}
	
	/**
	 * Gets the light value reading of the color sensor. 
	 * @return The color sensor reading multiplied by 1000 for precision. 
	 * @since 1.1
	 */
	public static float getLightValue() {
		lightSample.fetchSample(lightData, 0);
		return lightData[0]*1000;
	}
	
	/**
	 * Gets the light value reading of the angle color sensor.
	 * @return The angle color sensor reading multiplied by 1000 for precision.
	 * @since 1.2
	 */
	//public static float getAngleLightValue() {
	//	angleColorSample.fetchSample(angleLightData, 0);
	//	return lightData[0]*1000;
	//}
}
