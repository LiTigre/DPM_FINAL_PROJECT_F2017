package ca.mcgill.ecse211.navigation;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.SampleProvider;

/**
 * Is used to detect if an object is too close to the robot, it then begins obstacle avoidance. 
 * Thread constantly polls data from the ultrasonic sensor.
 * Once the object has been avoided, it returns to the previous process.
 * @author Team 2
 * @version 1.1
 * @since 1.0
 */
public class ObstacleAvoidance extends Thread {

	/**
	 * Constructor for the ObstacleAvoidance class.
	 * @param leftMotor Left wheel's motor created in the MainController class. 
	 * @param rightMotor Right wheel's motor created in the MainController class. 
	 * @param usSensor Ultrasonic sensor created in MainController.
	 * @param driver Driver created in MainController.
	 * @param search Search created in MainController.
	 * @since 1.1
	 */
	public ObstacleAvoidance(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor, 
							SampleProvider usSensor, Driver driver, Search search) {
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		
	}
	
	/**
	 * Performs the avoidance and returns to the previous operation. 
	 * @since 1.1
	 */
	public void avoidReturn() {
		
	}
	
	/**
	 * Performs the avoidance and continues to the next operation.
	 * @since 1.1 
	 */
	public void avoidContinue() {
		
	}
	
	/**
	 * Checks if the robot is currently avoiding an object. 
	 * @return Boolean. True if it avoiding, otherwise false. 
	 * @since 1.1
	 */
	public boolean isAvoiding() {
		return avoiding; 
	}
	
	/**
	 * Sets the avoiding boolean to true when avoiding and false otherwise. 
	 * @param avoid Boolean it is being set to.
	 * @since 1.1
	 */
	public void setAvoiding(boolean avoid) {
		
	}
}
