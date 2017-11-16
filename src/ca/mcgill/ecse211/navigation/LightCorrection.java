package ca.mcgill.ecse211.navigation;

import java.util.ArrayList;

import ca.mcgill.ecse211.controller.MainController;
import ca.mcgill.ecse211.odometry.Localization;
import ca.mcgill.ecse211.odometry.Odometer;
import lejos.hardware.Button;
import ca.mcgill.ecse211.navigation.Driver;

public class LightCorrection implements Runnable {

	private final float MAX_LIST_SIZE = 100;
	private final int SLEEP_SECONDS = 2;
	private final int TURN_THRESHOLD_DEGREES_CLOCKWISE = 20;
//	private final int TURN_THRESHOLD_DEGREES_COUNTER_CLOCKWISE = 40;
	private final int OFFSET_CLOCKWISE = 6;
//	private final int OFFSET_COUNTER_CLOCKWISE = 12;
	
	private Driver driver;
	private Odometer odometer;
	
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
			if (!Localization.isLocalizing && !(centerLightDataList.get(5) < MainController.LINE_THRESHOLD)){
				oldTheta = odometer.getTheta();
				System.out.println("O: " + oldTheta);
				if (angleLightData < MainController.LINE_THRESHOLD && centerLightData > MainController.LINE_THRESHOLD) {
					preCorrection();
					while (MainController.getLightValue() > MainController.LINE_THRESHOLD) {
						System.out.println("O: " + oldTheta);
						newTheta = odometer.getTheta();
						System.out.println("N: " + newTheta);
						diffTheta = getDiffTheta(oldTheta, newTheta);
						System.out.println("D: " + diffTheta);
						if (diffTheta > TURN_THRESHOLD_DEGREES_CLOCKWISE * 2) {
							driver.instantStop();
							Button.waitForAnyPress();
							System.out.println("Turn back by: " + (-diffTheta));
							driver.turnDistance(diffTheta - (OFFSET_CLOCKWISE * 2));
							System.out.println("Back");
							break;
						}
						MainController.rightMotor.forward();
					}
					postCorrection();
				} else if (centerLightData < MainController.LINE_THRESHOLD && angleLightData > MainController.LINE_THRESHOLD) {
					preCorrection();
					while (MainController.getAngleLightValue() > MainController.LINE_THRESHOLD) {
//						System.out.println("O: " + oldTheta);
						newTheta = odometer.getTheta();
//						System.out.println("N: " + newTheta);
						diffTheta = getDiffTheta(oldTheta, newTheta);
//						System.out.println("D: " + diffTheta);
						if (diffTheta > TURN_THRESHOLD_DEGREES_CLOCKWISE) {
							driver.instantStop();
//							System.out.println("Turn back by: " + (-diffTheta));
							driver.turnDistance(-(diffTheta + OFFSET_CLOCKWISE));
//							System.out.println("Back");
							break;
						}
						MainController.leftMotor.forward();
					}
					postCorrection();
				}
			}
		}
	}
	
	private void preCorrection() {
		driver.instantStop();
		Button.waitForAnyPress();
		MainController.leftMotor.setSpeed(driver.ROTATE_SPEED);
		MainController.rightMotor.setSpeed(driver.ROTATE_SPEED);
	}
	
	private void postCorrection() {
		takeBreak(300);
		driver.forward();
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
