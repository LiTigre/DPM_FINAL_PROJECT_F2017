package ca.mcgill.ecse211.navigation;

import ca.mcgill.ecse211.controller.MainController;
import ca.mcgill.ecse211.odometry.Localization;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.SampleProvider;

/**
 * Is used to detect if an object is too close to the robot, it then begins obstacle avoidance. 
 * Thread constantly polls data from the ultrasonic sensor.
 * Once the object has been avoided, it returns to the previous process.
 * @author Team 2
 * @version 1.2
 * @since 1.0
 */
public class ObstacleAvoidance extends Thread {

	// Objects
	/** Driver object created in the main controller. */
	Driver driver;
	/** Search object created in the main controller. */
	Search search;
	
	
	// Constants
	/** Constant that is associated to how close the robot can get to an object without triggered obstacle avoidance (cm). */
	private final static double DISTANCE_FROM_OBJECT = 10;
	/** Threshold added to the distance from an object because of variation is ultrasonic data (cm). */
	private final static double DISTANCE_THRESHOLD = 1;
	/** Constant that indicated how far away the robot needs to travel from the block before returning to normal process (cm). */
	private final static double DISTANCE_TO_AVOID = 30.48;
	
	
	// Booleans
	/** Boolean that is used to indicate if the robot is currently avoiding an object or not. */
	private boolean avoiding;
	
	
	/**
	 * Obstacle avoidance object constructor. 
	 * @param driver Object from the main controller. 
	 * @param search Object from the main controller. 
	 * @since 1.1
	 */
	public ObstacleAvoidance(Driver driver, Search search) {
		this.driver = driver;
		this.search = search;
		
		this.avoiding = false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 * @since 1.1
	 */
	public void run() {
		while (true) {
			double error; 	
			if((DISTANCE_FROM_OBJECT+DISTANCE_THRESHOLD) >= MainController.getDistanceValue()) {
				if(driver.isTraveling() && isAvoiding()) {
		    			avoid();
				}
		    }
		
		     try {
		       Thread.sleep(50);
		     } catch (Exception e) {
		     } // Poor man's timed sampling
		}
	} 
	/**
	 * Performs the avoidance and returns to the previous operation. 
	 * @since 1.1
	 */
	public void avoid() {
		setAvoiding(true);
		driver.instantStop();
		driver.turnDistance(90);
		setAvoiding(false);
		driver.travelDistance(DISTANCE_TO_AVOID);
		driver.turnDistance(-90);
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
	private void setAvoiding(boolean avoid) {
		avoiding = avoid;
	}
}
