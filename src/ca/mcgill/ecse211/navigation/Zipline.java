package ca.mcgill.ecse211.navigation;

import ca.mcgill.ecse211.controller.MainController;
import ca.mcgill.ecse211.odometry.Odometer;
import lejos.hardware.ev3.EV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.SampleProvider;

/**
 * Is used when the robot wants to get across the river using the zipline.
 * @author Team 2
 * @version 1.2
 * @since 1.0
 */
public class Zipline {
	
	private Odometer odometer;
	private EV3LargeRegulatedMotor ZiplineMotor;
	
	private static final double FLOOR_THRESHOLD;
	private static final int ZIPLINE_SPEED = 200;

	/**
	 * Constructor for the Zipline class.
	 * @param odometer Odometer created in MainController.
	 * @param ZiplineMotor Motor controlling the pulley
	 * @since 1.1
	 */
	public Zipline(Odometer odometer, EV3LargeRegulatedMotor ZiplineMotor) {
		this.odometer = odometer;
		this.ZiplineMotor = ZiplineMotor;
	}
	
	/**
	 * Starts and stops the pulley's motor. 
	 * @since 1.1
	 */
	public void performZiplineTravel() {
		ZiplineMotor.setSpeed(ZIPLINE_SPEED);
		
		ZiplineMotor.forward();
		
		while (!hasLanded());

		ZiplineMotor.stop();
	}
	
	/**
	 * Returns true if the robot finished traversing the zipline and landed
	 * on ground
	 * @return boolean true if finished traversing zipline, otherwise false
	 * @since 1.2
	 */
	private boolean hasLanded() {
		if (MainController.getLightValue() > FLOOR_THRESHOLD) return true;
		return false;
	}
}
