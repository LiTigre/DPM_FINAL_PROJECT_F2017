package ca.mcgill.ecse211.navigation;

import ca.mcgill.ecse211.odometry.Odometer;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * Main navigation class of the system. Contains all the basic travel methods necessary. 
 * @author Team 2
 * @version 1.1
 * @since 1.0
 */
public class Driver {
	
	/**
	 * Constructor for driver class.
	 * @param leftMotor Left wheel's motor created in the MainController class. 
	 * @param rightMotor Right wheel's motor created in the MainController class. 
	 * @param WHEEL_RADIUS Wheels' radii set in the MainController class.
	 * @param TRACK Track size set in MainController class.
	 * @param odometer Odometer created in the MainController class.
	 * @since 1.1
	 */
	public Driver(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor, double WHEEL_RADIUS,
				double TRACK, Odometer odometer) {
		
	}
	
	/**
	 * Travels to a point specified. 
	 * @param newX X position of the new point in centimeters.
	 * @param newY Y position of the new point in centimeters.
	 * @since 1.1
	 */
	public void travelTo(double newX, double newY) {
		
	}
	
	/**
	 * Travels a certain distance specified. 
	 * @param distance Distance the robot needs to travel in centimeters.
	 * @since 1.1
	 */
	public void travelDistance(double distance) {
		
	}
	
	/**
	 * Turn a specified amount. 
	 * @param angle Angle amount the robots needs to turn in degrees.
	 * @since 1.1
	 */
	public void turnDistance(double angle) {
		
	}
	
	/**
	 * Turns towards a point specified. 
	 * @param newX X position of the point (cm)
	 * @param newY Y position of the point (cm)
	 * @since 1.1
	 */
	public void turnTo(double newX, double newY) {
		
	}
	
	/**
	 * Checks if the robot is currently traveling. 
	 * @return boolean. If it is traveling then its true, otherwise false. 
	 * @since 1.1
	 */
	public boolean isTraveling() {
		return travelling;
	}
	
	/**
	 * Sets the traveling boolean. 
	 * @param travel True when it is traveling, false otherwise. 
	 * @since 1.1
	 */
	private void setTravelling(boolean travel) {
		
	}
}
