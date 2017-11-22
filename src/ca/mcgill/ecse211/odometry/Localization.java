package ca.mcgill.ecse211.odometry;

import java.util.Timer;
import java.util.TimerTask;

import ca.mcgill.ecse211.controller.MainController;
import ca.mcgill.ecse211.navigation.Driver;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.robotics.SampleProvider;

/**
 * Determines the starting point of the robot based on the corner it has been placed in. 
 * @author Team 2
 * @version 1.3
 * @since 1.0
 */
public class Localization {

	// Objects 
	/** Odometer created in the main controller. */
	private final Odometer odometer;
	/** Driver created in the main controller. */
	private final Driver driver;

	
	// Constants
	/** @see MainController#SENSOR_TO_TRACK */
	private static final double SENSOR_TO_TRACK = MainController.SENSOR_TO_TRACK;
	/** @see MainController#LINE_THRESHOLD */
	private static final double LINE_THRESHOLD = MainController.LINE_THRESHOLD;
	/** Value that separates falling and rising edge. */
	private static final int THRESHOLD_WALL =35;
	/** Noise created from the corner during localization and must be ignored. */
	private static final int NOISE_GAP = 2;
	
	
	// Color sensor data
	/** Array that collects the angle values of every time the robot reads a black line. */
	private double [] collectedData = new double[4];
	
	
	// Booleans
	/** Boolean that indicates whether the edges have been completed. */
	private boolean isCompleted;
	/** Boolean that indicates whether the robot is currently localizing. */
	public static volatile boolean isLocalizing;
	
	
	/**
	 * Constructor for the localization class.
	 * @param odometer Odometer created in MainController.
	 * @param driver Driver created in MainController.
	 * @since 1.1
	 */
	public Localization(Odometer odometer, Driver driver) {
		this.odometer = odometer;
		this.driver = driver;
		
		isCompleted = false;
		isLocalizing = false;
	}
	
	/**
	 * Performs localization to determine the starting position of the robot.
	 * @since 1.1
	 */
	public void localize() {
		
		LightCorrection.doCorrection = false;
		
		setLocalizing(true);
	
		while(odometer.getTheta() < 10) {
			driver.rotate();
		}
		// If the robot is facing the wall, do rising edge.
		if(MainController.getDistanceValue() < THRESHOLD_WALL + NOISE_GAP) {
			risingEdgeLocalization();
		}
		// If the robot is not facing the wall, do falling edge.
		else{
			fallingEdgeLocalization();
		}
		while(driver.getWheelsMoving());
		driver.instantStop();
		lightLocalization();
		
		Timer timer = new Timer();
		
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				LightCorrection.doCorrection = true;
			}
		}, 7 * 1000);
		
		setLocalizing(false);
	}
	
	/**
	 * Performs ultrasonic localization based on falling edge mechanic. 
	 * @since 1.1
	 */
	private void fallingEdgeLocalization() {
		 double firstAngle;
		 double lastAngle;
		 double deltaTheta;
		 double newTheta;
	
		 // Makes robot turn to its right until it sees wall (falling edge)
		 turnToWall();	
		 //Record first angle at stop
		 firstAngle = odometer.getTheta();
			
		 // Makes robot turn again until it sees a rising edge
		 turnAwayFromWall();
			
		 //Record second angle at stop
		 lastAngle = odometer.getTheta();
			
		 //Calculate the deltaTheta
		 deltaTheta = calculateTheta(lastAngle, firstAngle);
		 newTheta = odometer.getTheta() + deltaTheta;
			
		 odometer.setPosition(new double[] {0.0, 0.0, newTheta}, new boolean[]{true,true,true});
		 
		 //Make the robot turn to the calculated 0
		 driver.turnDistance(359 - newTheta);
	}
	
	/**
	 * Performs ultrasonic localization based on rising edge mechanic. 
	 * @since 1.1
	 */
	private void risingEdgeLocalization() {
		double firstAngle;
		double lastAngle;
		double deltaTheta;
		double newTheta;
		
		//Make robot turn until it does not see a wall (rising edge)
		turnAwayFromWall();
		
		//Record first angle at stop
		firstAngle = odometer.getTheta();
		
		//Makes the robot turn until it sees a wall (falling edge)
		turnToWall();
		
		//Record second angle stop 
		lastAngle = odometer.getTheta();
		
		//Calculate the deltaTheta
		deltaTheta = calculateTheta(firstAngle, lastAngle);
		newTheta = odometer.getTheta() + deltaTheta;
		
		
		odometer.setPosition(new double[] {0.0, 0.0, newTheta}, new boolean[]{true,true,true});
		
		//Make the robot turn to the calculated 0
		driver.turnDistance(359 - newTheta);
	}
	
	/**
	 * Performs light localization. 
	 * @since 1.1
	 */
	public void lightLocalization() {
		
		driver.turnDistance(360);
		int i = 0;
		while(driver.getWheelsMoving()) {
			// Collect data during the ultrasonic localization is running
			if(MainController.getLightValue() < LINE_THRESHOLD) {
	    			//implement collecting data here
	    			Sound.beep();
	    			collectedData[i] = odometer.getTheta();
	    			i++;
			}
		}
		calculateStartingPosition();
	}
	
	/**
	 * This method is used to localize when it is not at the starting location. 
	 * @param aroundX The X coordinate of the point it is localizing around.
	 * @param aroundY The Y coordinate of the point it is localizing around.
	 * @since 1.3
	 */
	public void reLocalize(double aroundX, double aroundY) {
		setLocalizing(true);
//		System.out.println(45-odometer.getTheta());
		driver.turnDistance((45-odometer.getTheta()));
		while(driver.getWheelsMoving());
		odometer.setTheta(0);
		driver.turnDistance(360);
		int i = 0;
		while(driver.getWheelsMoving()) {
			// Collect data during the ultrasonic localization is running
			if(MainController.getLightValue() < LINE_THRESHOLD) {
	    			//implement collecting data here
	    			Sound.beep();
	    			collectedData[i] = odometer.getTheta();
	    			i++;
			}
		}
		calculatePosition(aroundX, aroundY);
		setLocalizing(false);
	}
	
	/**
	 * Calculates the robots position based on the localization around a specified point. 
	 * @param aroundX The X coordinate of the point.
	 * @param aroundY The Y coordinate of the point.
	 * @since 1.3
	 */
	private void calculatePosition(double aroundX, double aroundY) {
		
		double thetaX;
		double thetaY;
		double deltaThetaY;
		
		//Arc angle from the first time you encounter and axis till the end. 
		thetaX = collectedData[3]-collectedData[1];
		thetaY = collectedData[2]-collectedData[0];
		
		//Set the new/actual position of the robot.
		odometer.setY(aroundY-SENSOR_TO_TRACK*Math.cos(Math.toRadians(thetaY/2)));
		odometer.setX(aroundX-SENSOR_TO_TRACK*Math.cos(Math.toRadians(thetaX/2)));
		
		//Correct angle 
		deltaThetaY = 90-(collectedData[3]-180)+thetaX/2;
		odometer.setTheta(deltaThetaY);
	}
	
	/**
	 * Calculates position of the robot based on the light localization. 
	 * @since 1.1
	 */
	private void calculateStartingPosition() {
		double thetaX;
		double thetaY;
		double deltaTheta;
		
		//Arc angle from the first time you encounter and axis till the end. 
		thetaX = collectedData[3]-collectedData[1];
		thetaY = collectedData[2]-collectedData[0];
		
		//Set the new/actual position of the robot.
		odometer.setY(-SENSOR_TO_TRACK*Math.cos(Math.toRadians(thetaY/2)));
		odometer.setX(-SENSOR_TO_TRACK*Math.cos(Math.toRadians(thetaX/2)));
		
		//Correct angle 
		deltaTheta = 90-(collectedData[3]-180)+thetaX/2;
		odometer.setTheta(deltaTheta);
		driver.travelTo(0, 0);
		while (driver.getWheelsMoving());
	}
	
	/**
	 * Turns until it hits a falling edge. 
	 * @since 1.1
	 */
	private void turnToWall() {
		while(!isCompleted){
			driver.rotate();
			
			//Checks if we reached a falling edge
			if(MainController.getDistanceValue() < THRESHOLD_WALL + NOISE_GAP){
				isCompleted = true;
			}
		}
		isCompleted = false;	
	}
	
	/**
	 * Turns until it hits a rising edge. 
	 * @since 1.1
	 */
	private void turnAwayFromWall(){
		while(!isCompleted){
			driver.rotate();
			
			if(MainController.getDistanceValue() > THRESHOLD_WALL + NOISE_GAP){
				isCompleted = true;
			}
			
		}
			isCompleted = false;
	}
	
	/**
	 * Sets the isLocalizing boolean to the boolean passed.
	 * @param localize True when it is localizing, false localizing.
	 * @since 1.2
	 */
	private void setLocalizing(boolean localize) {
		isLocalizing = localize;
	}
	
	/**
	 * Gets the value of the isLocalizing boolean 
	 * @return The booleans value. True if it is localizing, false otherwise.
	 * @since 1.2
	 */
	public boolean getLocalizing() {
		return isLocalizing;
	}
	
	/**
	 * Calculates the value of the rising edge and the falling edge in order for the robot to orient itself to the right angle. 
	 * @param firstAngle first edge found by the robot. 
	 * @param secondAngle second edge found by the robot. 
	 * @return returns the angle it needs to turns to in order to orient itself. 
	 * @since 1.1
	 */
	private double calculateTheta(double firstAngle, double secondAngle) {
		if(firstAngle < secondAngle){
			return 40 - (firstAngle + secondAngle) / 2;
		}
		else{
			return 220 - (firstAngle + secondAngle) / 2;
		}
	}
}
