package ca.mcgill.ecse211.odometry;

import java.util.ArrayList;

import ca.mcgill.ecse211.controller.MainController;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import ca.mcgill.ecse211.navigation.Driver;

public class LightCorrection extends Thread {

	private final float MAX_LIST_SIZE = 100;
	private final int SLEEP_SECONDS = 2;
	private final int TURN_THRESHOLD_DEGREES_CLOCKWISE = 10;
//	private final int TURN_THRESHOLD_DEGREES_COUNTER_CLOCKWISE = 40;
	private final int OFFSET_CLOCKWISE = 2;
//	private final int OFFSET_COUNTER_CLOCKWISE = 12;
	
	public static volatile boolean doCorrection = true;
	
	private Driver driver;
	private Odometer odometer;
	
//	private boolean updateOdometer = false;
	
	public LightCorrection(Driver driver, Odometer odometer) {
		this.driver = driver;
		this.odometer = odometer;
	}

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
//			System.out.println("L: " + Localization.isLocalizing);
//			System.out.println("Localizing: " + Localization.isLocalizing);
//			System.out.println("Is turning: " + driver.isTurning());
//			System.out.println("Center black: " + centerLightDataList.get(5));
//			System.out.println("Do correction: " + doCorrection);
			if (!Localization.isLocalizing && !driver.isTurning() && !(centerLightDataList.get(5) < MainController.LINE_THRESHOLD) && doCorrection){
//				System.out.println("DO");
				oldTheta = odometer.getTheta();
//				System.out.println("O: " + oldTheta);
				if (angleLightData < MainController.LINE_THRESHOLD && centerLightData > MainController.LINE_THRESHOLD) {
//					System.out.println("SIDE LINE");
					preCorrection();
					while (MainController.getLightValue() > MainController.LINE_THRESHOLD) {
//						updateOdometer = true;
//						System.out.println("10");
//						System.out.println("O: " + oldTheta);
						newTheta = odometer.getTheta();
//						System.out.println("N: " + newTheta);
						diffTheta = getDiffTheta(oldTheta, newTheta);
//						System.out.println("D: " + diffTheta);
						if (diffTheta > TURN_THRESHOLD_DEGREES_CLOCKWISE * 2) {
							driver.instantStopAsync();
							driver.turnDistanceSynchronous(diffTheta - (OFFSET_CLOCKWISE * 2));
//							updateOdometer = false;
							break;
						}
						MainController.rightMotor.forward();
					}
					MainController.rightMotor.stop();
					postCorrection();
				} else if (centerLightData < MainController.LINE_THRESHOLD && angleLightData > MainController.LINE_THRESHOLD) {
//					System.out.println("CENTER LINE");
					preCorrection();
//					System.out.println("30");
					while (MainController.getAngleLightValue() > MainController.LINE_THRESHOLD) {
//						updateOdometer = true;
//						System.out.println(MainController.getAngleLightValue());
//						System.out.println("20");
//						System.out.println("O: " + oldTheta);
						newTheta = odometer.getTheta();
//						System.out.println("N: " + newTheta);
						diffTheta = getDiffTheta(oldTheta, newTheta);
//						System.out.println("D: " + diffTheta);
						if (diffTheta > TURN_THRESHOLD_DEGREES_CLOCKWISE) {
							driver.instantStopAsync();
							driver.turnDistanceSynchronous(-(diffTheta + OFFSET_CLOCKWISE));
//							updateOdometer = false;
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
	
	private void preCorrection() {
//		System.out.println("HERE 1");
		driver.instantStopAsync();
//		updateOdometer = true;
//		System.out.println("HERE 2");
		driver.setSpeed(driver.ROTATE_SPEED);
//		System.out.println("HERE 3");
	}
	
	private void postCorrection() {
//		if (updateOdometer) {
//			odometer.update();
//		}
		takeBreak(300);
//		driver.forward();
//		System.out.println("X: " + Driver.destinationX);
//		System.out.println("Y: " + Driver.destinationY);
		driver.travelToStraight(Driver.destinationX, Driver.destinationY);
		takeBreak(SLEEP_SECONDS * 1000);
	}
	
	private void takeBreak(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private double getDiffTheta (double oldTheta, double newTheta) {
		double diffTheta = Math.abs(newTheta - oldTheta);
		if (diffTheta > 180) return 360 - diffTheta;
		return diffTheta;
	}
}
