package ca.mcgill.ecse211.odometry;

import java.util.ArrayList;

import ca.mcgill.ecse211.controller.MainController;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import ca.mcgill.ecse211.navigation.Driver;

/**
 * Use the two light sensors in the back to correct drive the robot in a straight line.
 * @author Team 2
 * @version 1.0
 * @since 2.0
 */
public class LightCorrection extends Thread {

	//Constants
	/** Size of list of stored light values*/
	private final float MAX_LIST_SIZE = 100;
	/** Number of seconds to wait before starting thread again after correcting */
	private final int SLEEP_SECONDS = 2;
	/** Max distance to turn before turning back if no other line found */
	private final int TURN_THRESHOLD_DEGREES_CLOCKWISE = 10;
	/** Robot doesn't turn back enough, add this offset to fix */
	private final int OFFSET_CLOCKWISE = 2;
	
	/** Boolean that indicates wheter the robot should light correct or not. */
	public static volatile boolean doCorrection;
	
	/** Driver object created in the Main Controller. */
	private Driver driver;
	/** Odometer object created in the Main Controller. */
	private Odometer odometer;
	
	/**
	 * Constructor for the LightCorrection class. 
	 * @param driver Driver created in the Main Controller.
	 * @param odomter Odometer created in the Main Controller. 
	 * @since 1.0 
	 */
	public LightCorrection(Driver driver, Odometer odometer) {
		this.driver = driver;
		this.odometer = odometer;
	}

	/**
	 * Do light correction. Drive until a line is detected, then make sure both
	 * sensors are on a line, if not then rotate until both sensors are on a line.
	 * @since 1.0
	 */
	@Override
	public void run() {
		
		ArrayList<Float> centerLightDataList = new ArrayList<Float>();
		float centerLightData;
		float angleLightData;
		
		double oldTheta;
		double newTheta;
		double diffTheta;
		
		for (int i = 0; i < 10; i++) {
			centerLightDataList.add(MainController.getLightValue());
		}
		while (true) {
			centerLightData = MainController.getLightValue();
			angleLightData = MainController.getAngleLightValue();
			centerLightDataList.add(centerLightData);
			if (centerLightDataList.size() > MAX_LIST_SIZE) {
				centerLightDataList.remove(0);
			}
			if (Math.abs(Driver.destinationX - odometer.getX()) < 20 && Math.abs(Driver.destinationY - odometer.getY()) < 20) {
				doCorrection = false;
			}
			if (!Localization.isLocalizing && !driver.isTurning() && !(centerLightDataList.get(5) < MainController.LINE_THRESHOLD) && doCorrection){
				oldTheta = odometer.getTheta();
				if (angleLightData < MainController.LINE_THRESHOLD && centerLightData > MainController.LINE_THRESHOLD) {
					preCorrection();
					while (MainController.getLightValue() > MainController.LINE_THRESHOLD) {
						newTheta = odometer.getTheta();
						diffTheta = getDiffTheta(oldTheta, newTheta);
						if (diffTheta > TURN_THRESHOLD_DEGREES_CLOCKWISE * 2) {
							driver.instantStopAsync();
							driver.turnDistanceSynchronous(diffTheta - (OFFSET_CLOCKWISE * 2));
							break;
						}
						MainController.rightMotor.forward();
					}
					MainController.rightMotor.stop();
					postCorrection();
				} else if (centerLightData < MainController.LINE_THRESHOLD && angleLightData > MainController.LINE_THRESHOLD) {
					preCorrection();
					while (MainController.getAngleLightValue() > MainController.LINE_THRESHOLD) {
						newTheta = odometer.getTheta();
						diffTheta = getDiffTheta(oldTheta, newTheta);
						if (diffTheta > TURN_THRESHOLD_DEGREES_CLOCKWISE) {
							driver.instantStopAsync();
							driver.turnDistanceSynchronous(-(diffTheta + OFFSET_CLOCKWISE));
							break;
						}
						MainController.leftMotor.forward();
					}
					MainController.leftMotor.stop();
					postCorrection();
				}
			}
		}
	}
	
	/**
	 * Setup before the correction. Stop the robot and set the speed to rotate speed.
	 * @since 1.0
	 */
	private void preCorrection() {
		driver.instantStopAsync();
		driver.setSpeed(driver.ROTATE_SPEED);
	}
	
	/**
	 *  Finalize after correction. Take a break, and then continue driving.
	 * @since 1.0
	 */
	private void postCorrection() {
		takeBreak(300);
		driver.travelToStraight(Driver.destinationX, Driver.destinationY);
		takeBreak(SLEEP_SECONDS * 1000);
	}
	
	/**
	 * Sleep the thread for a set amount of milliseconds
	 * @param milliseconds how many milliseconds to sleep the thread for
	 * @since 1.0
	 */
	private void takeBreak(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the difference of two angles
	 * @param oldTheta first angle
	 * @param newTheta second angle
	 * @return difference between oldTheta and newTheta
	 * @since 1.0
	 */
	private double getDiffTheta (double oldTheta, double newTheta) {
		double diffTheta = Math.abs(newTheta - oldTheta);
		if (diffTheta > 180) return 360 - diffTheta;
		return diffTheta;
	}
}
