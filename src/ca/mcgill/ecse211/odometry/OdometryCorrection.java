package ca.mcgill.ecse211.odometry;

import ca.mcgill.ecse211.controller.MainController;
import ca.mcgill.ecse211.navigation.Driver;
import lejos.robotics.SampleProvider;

/**
 * Provides correction to the odometer based on light sensor data. 
 * @author Team 2
 * @version 1.1
 * @since 1.0
 */
public class OdometryCorrection extends Thread {

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
	
	Driver driver;
	
	/**
	 * Constructor for the OdometryCorrection class. 
	 * @param odometer Odometer created in the MainCpntroller class.
	 * @param colorSensor Color sensor created in MainController.
	 * @since 1.1
	 */
	public OdometryCorrection(Odometer odometer, Driver driver) {
		this.driver = driver;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		long correctionStart, correctionEnd;
		double firstX, secondX, firstY, secondY;
		while(true) {
			correctionStart = System.currentTimeMillis();
			// this ensure the odometry correction occurs only once every period
			correctionEnd = System.currentTimeMillis();
		      
			//Main Correction sensor
			if(MainController.getLightValue() <= LINE_THRESHOLD) {
				// To avoid correction while turning on itself.
				if(!(driver.isTurning())) {
					performCorrection();
				}
			}
			//Angle correction sensor
			else if(MainController.getAngleLightValue() <= ANGLE_LINE_THRESHOLD) {
				// To avoid correction while turning on itself.
				if(!(driver.isTurning())) {
					performCorrection();
					//Perform angle correction here
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
		
		// Figure out which way the robot is going and increment/decrement an odometer accordingly. 
		if((whichXWay < 0) && (whichYWay == 0)) {
		
		}
		else if((whichXWay > 0) && (whichYWay == 0)) {
			
		}
		else if((whichYWay < 0) && (whichXWay == 0)) {
			
		}
		else if((whichYWay > 0) && (whichXWay == 0)) {
			
		}
	}
}
