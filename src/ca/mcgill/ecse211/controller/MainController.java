package ca.mcgill.ecse211.controller;

import ca.mcgill.ecse211.navigation.Driver;
import ca.mcgill.ecse211.navigation.Zipline;
import ca.mcgill.ecse211.odometry.Localization;
import ca.mcgill.ecse211.odometry.Odometer;
import ca.mcgill.ecse211.odometry.OdometryCorrection;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

/**
 * Controls all actions taken by the robot. Is in charge of sequencing operations 
 * in order to do what the tasks requires the robot to do. 
 * @author Team 2
 * @version 1.3
 * @since 1.0
 */
public class MainController {
	
	// Constants 
	/** The radius of the robot's wheels in cm */
	public static final double WHEEL_RADIUS;
	/** The length of the robot's track in cm. */
	public static final double TRACK;
	/** Distance from the color sensor to the middle of the track in cm */
	public static final double SENSOR_TO_TRACK;
	/** Value that indicates a black line. */
	public static final double LINE_THRESHOLD;
	/** Value of the length of a block in cm */
	public static final double BLOCK_LENGTH; 
	
	
	// Sensors
	/**  Color sensor with associated port. */
	private static final EV3ColorSensor colorSensor = new EV3ColorSensor(LocalEV3.get().getPort("S2"));
	/** Color sensor used for angle correction with associated port. */
	private static final EV3ColorSensor angleColorSensor = new EV3ColorSensor(LocalEV3.get().getPort("S1"));
	/** Ultrasonic sensor with associated port. */
	private static final SensorModes usSensor = new EV3UltrasonicSensor(LocalEV3.get().getPort("S4"));
	
	
	// Motors
	/** Left motor with associated port. */
	static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("C"));
	/** Right motor with associated port. */
	static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
	/** Zipline motor with associated port. */
	static final EV3LargeRegulatedMotor ziplineMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	
	
	// Make it that we can collect data from the sensor
	/** Data collected from the color sensor. */
	private static SampleProvider colorSample = colorSensor.getMode("Red");
	/** Data collected from the angle color sensor. */
	private static SampleProvider angleColorSample = angleColorSensor.getMode("Red");
	/** Data collected from the ultrasonic sensor. */
	private static SampleProvider usDistance = usSensor.getMode("Distance");

	
	// Data from the sensors
	/** Array of floats that stores the value of the data from the color sensor. */
	private static float lightData[] = new float[colorSample.sampleSize()];
	/** Array of floats that stores the value of the data from the color sensor. */
	private static float angleLightData[] = new float[angleColorSample.sampleSize()];
	/** Array of floats that stores the value of the data from the ultrasonic sensor. */
	private static float usData[] = new float[usDistance.sampleSize()];
	
	
	// Variables
	/** The X coordinate from where robot began its travel from. */
	private static double previousX;
	/** The Y coordinate from where robot began its travel from. */
	private static double previousY;
	/** The X coordinate to where the robot is supposed to stop its traveling at. */
	private static double futureX;
	/** The Y coordinate to where the robot is supposed to stop its traveling at. */
	private static double futureY;
	
	
	/**
	 * Runs capture the flag.
	 * @param args required to make this the main method of the system. 
	 * @since 1.1
	 */
	public static void main(String[] args) {
		// Object creation 
		Odometer odometer = new Odometer(leftMotor, rightMotor);
		Driver driver = new Driver(leftMotor, rightMotor, odometer);
		Localization localization = new Localization(odometer, driver);
		OdometryCorrection odoCorrection = new OdometryCorrection(odometer, driver);
		Zipline zipline = new Zipline(ziplineMotor);
		
	}
	
	/**
	 * Sets the X and Y coordinates of the previous point it was at. 
	 * @param lastX X value of the previous coordinate.
	 * @param lastY Y value of the previous coordinate.
	 * @since 1.3
	 */
	private void setPreviousCoordinates(double lastX, double lastY) {
		previousX = lastX;
		previousY = lastY;
	}

	/**
	 * Returns the previous X coordinates. 
	 * @return The previous X value. 
	 * @since 1.3
	 */
	public static double getPreviousX() {
		return previousX;
	}
	
	/**
	 * Returns the previous Y coordinates. 
	 * @return The previous Y value. 
	 * @since 1.3
	 */
	public static double getPreviousY() {
		return previousY;
	}
	
	/**
	 * Sets the X and Y coordinates of the next point it is going to. 
	 * @param nextX X value of the next coordinate.
	 * @param nextY Y value of the next coordinate.
	 * @since 1.3
	 */
	private void setFutureCoordinates(double nextX, double nextY) {
		futureX = nextX;
		futureY = nextY;
	}
	
	/**
	 * Returns the next X coordinates. 
	 * @return The previous X value. 
	 * @since 1.3
	 */
	public static double getFutureX() {
		return futureX;
	}
	
	/**
	 * Returns the next Y coordinates. 
	 * @return The previous Y value. 
	 * @since 1.3
	 */
	public static double getFutureY() {
		return futureY;
	}
	
	/**
	 * Gets the distance reading of the ultrasonic sensor.
	 * @return The ultrasonic sensor reading multiplied by 100 for precision. 
	 * @since 1.1
	 */
	public static float getDistanceValue(){
		usSensor.fetchSample(usData, 0);
		return usData[0]*100;
	}
	
	/**
	 * Gets the light value reading of the color sensor. 
	 * @return The color sensor reading multiplied by 1000 for precision. 
	 * @since 1.1
	 */
	public static float getLightValue() {
		colorSensor.fetchSample(lightData, 0);
		return lightData[0]*1000;
	}
	
	/**
	 * Gets the light value reading of the angle color sensor.
	 * @return The angle color sensor reading multiplied by 1000 for precision.
	 * @since 1.2
	 */
	public static float getAngleLightValue() {
		angleColorSensor.fetchSample(angleLightData, 0);
		return lightData[0]*1000;
	}
}
