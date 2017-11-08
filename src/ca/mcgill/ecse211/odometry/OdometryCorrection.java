package ca.mcgill.ecse211.odometry;

import ca.mcgill.ecse211.controller.MainController;
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
	
	/**
	 * Constructor for the OdometryCorrection class. 
	 * @param odometer Odometer created in the MainCpntroller class.
	 * @param colorSensor Color sensor created in MainController.
	 * @since 1.1
	 */
	public OdometryCorrection(Odometer odometer, SampleProvider colorSensor) {
		
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
		
	}
}
