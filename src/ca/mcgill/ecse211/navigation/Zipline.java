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
	
	/**
	 * Zipline motor created in the main controller. 
	 */
	private EV3LargeRegulatedMotor ziplineMotor;
	
	/**
	 * Light sensor value of the floor
	 */
	private double FLOOR_INTENSITY;
	/**
	 * Speed of the zipline motor in degrees/second
	 */
	private final int ZIPLINE_SPEED = 200;
	/**
	 * Create a range of acceptable values to consider the floor
	 */
	private final int RANGE;
	/**
	 * Number of times same light sensor value has to be read to be considered acceptable
	 */
	private final int FILTER;
	/**
	 * Keep track of how many times the same value is read from the light sensor
	 */
	private int count = 0;
	
	/**
	 * Constructor for the Zipline class.
	 * @param odometer Odometer created in MainController.
	 * @param ZiplineMotor Motor controlling the pulley
	 * @since 1.1
	 * @version 1.1
	 */
	public Zipline(EV3LargeRegulatedMotor ziplineMotor) {
		this.ziplineMotor = ziplineMotor;
		this.FLOOR_INTENSITY = calculateFloorIntensity();
	}
	
	/**
	 * Starts and stops the pulley's motor. 
	 * @since 1.1
	 * @version 1.0
	 */
	public void performZiplineTravel() {
		ziplineMotor.setSpeed(ZIPLINE_SPEED);
		
		ziplineMotor.forward();
		
		while (!hasLanded());

		ziplineMotor.stop();
	}
	
	/**
	 * Returns true if the robot finished traversing the zipline and landed
	 * on ground
	 * @return boolean true if finished traversing zipline, otherwise false
	 * @since 1.2
	 * @version 1.1
	 */
	private boolean hasLanded() {
		if (MainController.getLightValue() < (FLOOR_INTENSITY + RANGE) && MainController.getLightValue() > (FLOOR_INTENSITY - RANGE)) {
			if (count > FILTER) return true;
			count++;
		} else {
			count = 0;
		}
		return false;
	}
	
	/**
	 * Return the intensity of the floor calculated by getting the average
	 * of the values from the light sensor when facing the floor
	 * @return float light intensity of floor
	 * @since 1.2
	 * @version 1.1
	 */
	
	private float calculateFloorIntensity() {
		float sum = 0;
		int i;
		for (i = 0; i < 1000; i++) {
			sum += MainController.getLightValue();
		}
		return sum / i;
	}
}
