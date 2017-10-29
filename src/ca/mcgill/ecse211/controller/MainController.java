package ca.mcgill.ecse211.controller;

/**
 * Controls all actions taken by the robot. Is in charge of sequencing operations 
 * in order to do what the tasks requires the robot to do. 
 * @author Team 2
 * @version 1.1
 * @since 1.0
 */
public class MainController {

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
	public float getDistanceValue(){
		usSensor.fetchSample(usData, 0);
		return usData[0]*100;
	}
	
	/**
	 * Gets the light value reading of the color sensor. 
	 * @return The color sensor reading multiplied by 1000 for precision. 
	 * @since 1.1
	 */
	public float getLightValue() {
		colorSample.fetchSample(lightData, 0);
		return lightData[0]*1000;
	}
}
