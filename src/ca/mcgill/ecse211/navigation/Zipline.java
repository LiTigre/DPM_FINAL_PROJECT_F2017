package ca.mcgill.ecse211.navigation;

import ca.mcgill.ecse211.controller.MainController;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.jfree.chart.axis.SegmentedTimeline;

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
	
	// Objects
	/** Zipline motor created in the main controller. */
	private EV3LargeRegulatedMotor ziplineMotor;
	private Driver driver;
	
	
	// Constants
	/** Speed of the zipline motor in degrees/second */
	private static final int ZIPLINE_SPEED = 200;
	/** Create a range of acceptable values to consider the floor */
	private static final int RANGE = 100;
	/** Number of times same light sensor value has to be read to be considered acceptable */
	private static final int FILTER = 300;
	/** Number of seconds to wait before checking if robot landed */ 
	private final int WAIT_SECONDS = 36;
	
	
	// Variables
	/** Keep track of how many times the same value is read from the light sensor */
	private int count = 0;
	/** Light sensor value of the floor */
	private double floorIntensity;
	
	/**
	 * Constructor for the Zipline class.
	 * @param odometer Odometer created in MainController.
	 * @param ZiplineMotor Motor controlling the pulley
	 * @since 1.1
	 * @version 1.1
	 */
	public Zipline(EV3LargeRegulatedMotor ziplineMotor, Driver driver) {
		this.ziplineMotor = ziplineMotor;
		this.driver = driver;
		this.floorIntensity = calculateFloorIntensity();
	}
	
	/**
	 * Starts and stops the pulley's motor. 
	 * @since 1.1
	 * @version 1.0
	 */
	public void performZiplineTravel() {
		ziplineMotor.setSpeed(ZIPLINE_SPEED);
		
		driver.forward();
		ziplineMotor.backward();
		
		Timer timer = new Timer();
		
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				ziplineMotor.stop();
				driver.travelDistance(35);
			}
		}, WAIT_SECONDS * 1000);
		
	}
	
	/**
	 * Returns true if the robot finished traversing the zipline and landed
	 * on ground
	 * @return boolean true if finished traversing zipline, otherwise false
	 * @since 1.2
	 * @version 1.1
	 */
	private boolean hasLanded() {
		System.out.println(MainController.getLightValue());
		if (MainController.getLightValue() < (floorIntensity + RANGE) && MainController.getLightValue() > (floorIntensity - RANGE)) {
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
