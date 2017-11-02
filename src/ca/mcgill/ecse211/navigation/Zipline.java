package ca.mcgill.ecse211.navigation;

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
	private SampleProvider colorSensor;
	private float[] colorSensorData;
	private EV3LargeRegulatedMotor motor;
	
	private double floorColorThreshold;

	private final int MOTOR_SPEED = 200;

	/**
	 * Constructor for the Zipline class.
	 * @param odometer Odometer created in MainController.
	 * @param colorSensor Color sensor created in MainController.
	 * @param motor Motor controlling the pulley
	 * @param floorColorThreshold Calculated color sensor value of floor
	 * @since 1.1
	 */
	public Zipline(Odometer odometer, SampleProvider colorSensor, EV3LargeRegulatedMotor motor, double floorColorThreshold) {
		this.odometer = odometer;
		this.colorSensor = colorSensor;
		this.colorSensorData = new float[colorSensor.sampleSize()];
		this.motor = motor;
		this.floorColorThreshold = floorColorThreshold;
	}
	
	/**
	 * Starts and stops the pulley's motor. 
	 * @since 1.1
	 */
	public void performZiplineTravel() {
		motor.setSpeed(MOTOR_SPEED);
		motor.forward();
		while (!hasLanded()) {
			continue;
		}
		motor.stop();
	}
	
	/**
	 * Returns true if the robot finished traversing the zipline and landed
	 * on ground
	 * @return boolean true if finished traversing zipline, otherwise false
	 * @since 1.2
	 */
	private boolean hasLanded() {
		if (getColorData() < floorColorThreshold) return true;
		return false;
	}
	
	/**
	 * Return the data retrieved from the color sensor
	 * @return float data from the color sensor
	 * @since 1.2
	 */
	private float getColorData() {
		colorSensor.fetchSample(colorSensorData, 0);
		return colorSensorData[0];
	}
}
