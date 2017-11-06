package ca.mcgill.ecse211.navigation;

import ca.mcgill.ecse211.controller.MainController;
import java.util.ArrayList;
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
	
	/**
	 * Constant that indicates that the robot is off the floor (compared to the light sensor data)
	 */
	private static final double FLOOR_THRESHOLD;
	/**
	 * Speed of the zipline motor in degrees/second
	 */
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
	
	/**
	 * Return the data retrieved from the color sensor
	 * @return float data from the color sensor
	 * @since 1.2
	 */
	
	private float calculateFloorColor() {
		ArrayList<Float> colors = new ArrayList<Float>();
		float sum = 0;
		float color;
		for (int i = 0; i < 1000; i++) {
			color = getColor();
			
			colors.add(getColor());
		}
		
	}
}
