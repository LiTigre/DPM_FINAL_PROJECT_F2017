package ca.mcgill.ecse211.odometry;

import ca.mcgill.ecse211.navigation.Driver;
import lejos.robotics.SampleProvider;

/**
 * Determines the starting point of the robot based on the corner it has been placed in. 
 * @author Team 2
 * @version 1.1
 * @since 1.0
 */
public class Localization {

	/**
	 * Constructor for the localization class.
	 * @param odometer Odometer created in MainController.
	 * @param colorSensor Color sensor created in MainController.
	 * @param usSensor Ultrasonic sensor created in MainController.
	 * @param driver Driver created in MainController.
	 * @since 1.1
	 */
	public Localization(Odometer odometer, SampleProvider colorSensor, SampleProvider usSensor, Driver driver) {
	}
	
	/**
	 * Performs localization to determine the starting position of the robot.
	 * @since 1.1
	 */
	public void localize() {
		
	}
	
	/**
	 * Performs ultrasonic localization based on falling edge mechanic. 
	 * @since 1.1
	 */
	private void fallingEdgeLocalization() {
		
	}
	
	/**
	 * Performs ultrasonic localization based on rising edge mechanic. 
	 * @since 1.1
	 */
	private void risingEdgeLocalization() {
		
	}
	
	/**
	 * Performs light localization. 
	 * @since 1.1
	 */
	private void lightLocalization() {
		
	}
	
	/**
	 * Calculates position of the robot based on the light localization. 
	 * @since 1.1
	 */
	private void calculatePosition() {
		
	}
	
	/**
	 * Turns until it hits a falling edge. 
	 * @since 1.1
	 */
	private void turnToWall() {
		
	}
	
	/**
	 * Turns until it hits a rising edge. 
	 * @since 1.1
	 */
	private void turnAwayFromWall(){
		
	}
	
	/**
	 * Calculates the value of the rising edge and the falling edge in order for the robot to orient itself to the right angle. 
	 * @param firstAngle first edge found by the robot. 
	 * @param secondAngle second edge found by the robot. 
	 * @return returns the angle it needs to turns to in order to orient itself. 
	 * @since 1.1
	 */
	private double calculateTheta(double firstAngle, double secondAngle) {
		
		return secondAngle; ///to be changed
	}
}
