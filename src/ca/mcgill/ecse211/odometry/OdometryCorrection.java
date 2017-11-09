package ca.mcgill.ecse211.odometry;

import ca.mcgill.ecse211.controller.MainController;
import ca.mcgill.ecse211.navigation.Driver;
import lejos.robotics.SampleProvider;

/**
 * Provides correction to the odometer based on light sensor data. 
 * @author Team 2
 * @version 1.2
 * @since 1.0
 */
public class OdometryCorrection extends Thread {

	// Objects 
	/**
	 * Driver object created in the main controller. 
	 */
	Driver driver;
	/**
	 * Odometer object created in the main controller. 
	 */
	Odometer odometer;
	
	
	// Constants
	/**
	 * @see MainController#SENSOR_TO_TRACK
	*/
	private static final double SENSOR_TO_TRACK = MainController.SENSOR_TO_TRACK;
	/**
	 * @see MainController#LINE_THRESHOLD
	 */
	private static final double LINE_THRESHOLD = MainController.LINE_THRESHOLD;
	/**
	 * @see MainController#BLOCK_LENGTH
	 */
	private static final double BLOCK_LENGTH = MainController.BLOCK_LENGTH;
	/**
	 * Distance from the angle color sensor to the middle of the track in cm 
	 */
	private static final double ANGLE_SENSOR_TO_TRACK;
	/**
	 * Value that indicates a black line for the second sensor. 
	 */
	private static final double ANGLE_LINE_THRESHOLD;
	/**
	 * Thread time of the correction. 
	 */
	private static final long CORRECTION_PERIOD = 10;
	/**
	 * Keeps track of the x position of the robot. 
	 */
	private int xCounter;
	/**
	 * Keeps track of the y position of the robot. 
	 */
	private int yCounter;
	/**
	 * Checks if it able to correct the the angle or not. 
	 */
	private boolean positionCorrection;
	
	/**
	 * Constructor for the OdometryCorrection class. 
	 * @param odometer Odometer created in the MainCpntroller class.
	 * @param colorSensor Color sensor created in MainController.
	 * @since 1.1
	 */
	public OdometryCorrection(Odometer odometer, Driver driver) {
		this.driver = driver;
		this.odometer = odometer;
		
		this.xCounter = 0;
		this.yCounter = 0; 
		this.positionCorrection = false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		long correctionStart, correctionEnd;
		while(true) {
			correctionStart = System.currentTimeMillis();
			// this ensure the odometry correction occurs only once every period
			correctionEnd = System.currentTimeMillis();
		      
			//Main Correction sensor
			if(MainController.getLightValue() <= LINE_THRESHOLD) {
				// To avoid correction while turning on itself.
				if(!(driver.isTurning())) {
					
					performCorrection();
					changeBoolState();
				}
			}
			//Angle correction sensor
			else if(MainController.getAngleLightValue() <= ANGLE_LINE_THRESHOLD) {
				// To avoid correction while turning on itself.
				if(!(driver.isTurning())) {
					performCorrection();
					changeBoolState();
				}
			}
			
			if (correctionEnd - correctionStart < CORRECTION_PERIOD) {
				try {
					Thread.sleep(CORRECTION_PERIOD - (correctionEnd - correctionStart));
				} catch (InterruptedException e) {
				// there is nothing to be done here because it is not
				// expected that the odometry correction will be
				// interrupted by another thread
				}
			}
		}
	}
	
	/**
	 * Corrects the odometer values based on light sensor readings. 
	 * @since 1.1
	 */
	private void performCorrection() {
		double whichXWay = MainController.getFutureX() - MainController.getPreviousX(); 
		double whichYWay = MainController.getFutureY() - MainController.getPreviousY();
		
		// Figure out which way the robot is going and increment/decrement the odometer accordingly. 
		if((whichXWay < 0) && (whichYWay == 0)) {
			if(!(positionCorrection)) {
				xCounter = xCounter - 1;
			}
			odometer.setX((xCounter*BLOCK_LENGTH) + SENSOR_TO_TRACK);
		}
		else if((whichXWay > 0) && (whichYWay == 0)) {
			if(!(positionCorrection)) {
				xCounter = xCounter + 1;
			}
			odometer.setX((xCounter*BLOCK_LENGTH) + SENSOR_TO_TRACK);
		}
		else if((whichYWay < 0) && (whichXWay == 0)) {
			if(!(positionCorrection)) {
				yCounter =yCounter - 1;
			}
			odometer.setY((yCounter*BLOCK_LENGTH) + SENSOR_TO_TRACK);
		}
		else if((whichYWay > 0) && (whichXWay == 0)) {
			if(!(positionCorrection)) {
				yCounter = yCounter + 1;
			}
			odometer.setY((yCounter*BLOCK_LENGTH) + SENSOR_TO_TRACK);
		}
	}
	
	/**
	 * This changes the odometry correction X and Y counters to the specified values. 
	 * @param counterX Value that the X counter will be changed to.  
	 * @param counterY Value that the Y counter will be changed to.
	 * @since 1.2
	 */
	public void hardResetPosition(int counterX, int counterY) {
		xCounter = counterX;
		yCounter = counterY;
	}
	
	/**
	 * This changes the value of the boolean to the opposite of the current state. 
	 * @since 1.2
	 */
	private void changeBoolState() {
		positionCorrection = !(positionCorrection);
	}
}
