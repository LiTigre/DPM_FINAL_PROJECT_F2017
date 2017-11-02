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
 * @version 1.1
 * @since 1.0
 */
public class MainController {
	
	
	
	// Sensors 
	private static final EV3ColorSensor colorSensor = new EV3ColorSensor(LocalEV3.get().getPort("S1"));
	private static final SensorModes usSensor = new EV3UltrasonicSensor(LocalEV3.get().getPort("S2"));
	private static SampleProvider colorSample = colorSensor.getMode("Red");
	private static SampleProvider usDistance = usSensor.getMode("Distance");
	private static float lightData[] = new float[colorSample.sampleSize()];
	private static float usData[] = new float[usDistance.sampleSize()];
	

	
	/**
	 * Runs capture the flag.
	 * @param args required to make this the main method of the system. 
	 * @since 1.1
	 */
	public static void main(String[] args) {
		
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
}
