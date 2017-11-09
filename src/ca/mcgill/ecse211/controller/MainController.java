package ca.mcgill.ecse211.controller;

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
 * @version 1.2
 * @since 1.0
 */
public class MainController {
	// Constants 
	/**
	 * The radius of the robot's wheels in cm  
	 */
	public static final double WHEEL_RADIUS;
	/**
	 * The length of the robot's track in cm. 
	 */
	public static final double TRACK;
	/**
	 * Distance from the color sensor to the middle of the track in cm
	*/
	public static final double SENSOR_TO_TRACK;
	/**
	 * Value that indicates a black line.
	 */
	public static final double LINE_THRESHOLD;
	/**
	 * Value of the length of a block in cm
	 */
	public static final double BLOCK_LENGTH; 
	
	/**
	 * Color sensor with associated port.
	 */
	private static final EV3ColorSensor colorSensor = new EV3ColorSensor(LocalEV3.get().getPort("S2"));
	/**
	 * Color sensor used for angle correction with associated port. 
	 */
	private static final EV3ColorSensor angleColorSensor = new EV3ColorSensor(LocalEV3.get().getPort("S1"));
	/**
	 * Ultrasonic sensor with associated port. 
	 */
	private static final SensorModes usSensor = new EV3UltrasonicSensor(LocalEV3.get().getPort("S2"));
	/**
	 * Data collected from the color sensor.
	 */
	private static SampleProvider colorSample = colorSensor.getMode("Red");
	/**
	 * Data collected from the angle color sensor.
	 */
	private static SampleProvider angleColorSample = angleColorSensor.getMode("Red");
	/**
	 * Data collected from the ultrasonic sensor.
	 */
	private static SampleProvider usDistance = usSensor.getMode("Distance");
	/**
	 * Array of floats that stores the value of the data from the color sensor.
	 */
	private static float lightData[] = new float[colorSample.sampleSize()];
	/**
	 * Array of floats that stores the value of the data from the color sensor.
	 */
	private static float angleLightData[] = new float[angleColorSample.sampleSize()];
	/**
	 * Array of floats that stores the value of the data from the ultrasonic sensor. 
	 */
	private static float usData[] = new float[usDistance.sampleSize()];
	
	/**
	 * The coordinates from where robot began its travel from. 
	 */
	private static double previousX;
	private static double previousY;
	
	/**
	 * The coordinates to where the robot is supposed to stop its traveling at. 
	 */
	private static double futureX;
	private static double futureY;
	

	
	/**
	 * Runs capture the flag.
	 * @param args required to make this the main method of the system. 
	 * @since 1.1
	 */
	public static void main(String[] args) {
		
	}
	
	private void setPreviousCoordinates(double lastX, double lastY) {
		previousX = lastX;
		previousY = lastY;
	}

	public static double getPreviousX() {
		return previousX;
	}
	
	public static double getPreviousY() {
		return previousY;
	}
	
	private void setFutureCoordinates(double nextX, double nextY) {
		futureX = nextX;
		futureY = nextY;
	}
	
	public static double getFutureX() {
		return futureX;
	}
	
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
