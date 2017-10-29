package ca.mcgill.ecse211.odometry;

import lejos.robotics.SampleProvider;

/**
 * Provides correction to the odometer based on light sensor data. 
 * @author Team 2
 * @version 1.1
 * @since 1.0
 */
public class OdometryCorrection extends Thread {

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
		
	}
	
	/**
	 * Corrects the odometer values based on light sensor readings. 
	 * @since 1.1
	 */
	private void performCorrection() {
		
	}
}
