package ca.mcgill.ecse211.odometry;

import ca.mcgill.ecse211.controller.MainController;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * This is the robot's odometer. It updates the position of the robot based on the movement of the wheels. 
 * @author Team 2
 * @version 1.1
 * @since 1.0
 */
public class Odometer extends Thread {
	
	// robot position
	/** The x position of the robot based off the origin in cm */
	private double x;
	/** The y position of the robot based off the origin in cm */
	private double y;
	/** The theta position of the robot based in degrees (0 is facing the +Y-axis) */
	private double theta;
	
	
	// Constants
	/** @see MainController#WHEEL_RADIUS */
	private static final double WHEEL_RADIUS = MainController.WHEEL_RADIUS;
	/** @see MainController#TRACK */
	private static final double TRACK = MainController.TRACK;
	/** Odometers update period in ms */
	private static final long ODOMETER_PERIOD = 25; 
	
	// Other
	/** Change in the left motor's angle. Used to calculate the change in position. */
	private int leftMotorTachoCount;
	/** Change in the right motor's angle. Used to calculate the change in the position. */
	private int rightMotorTachoCount;
	
	/** Left motor created in the main controller. */
	private EV3LargeRegulatedMotor leftMotor;
	/** Right motor created in the main controller. */
	private EV3LargeRegulatedMotor rightMotor;
	/** Lock objects for mutual exclusion. */
	private Object lock; 
		
	
	/**
	 * Constructor for the Odometer.
	 * @param leftMotor Left wheel's motor created in the MainController class. 
	 * @param rightMotor Right wheel's motor created in the MainController class. 
	 * @since 1.1
	 */
	public Odometer(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor) {
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		
		this.x = 0.0;
		this.y = 0.0;
		this.theta = 0.0;
		
		this.leftMotorTachoCount = 0;
		this.rightMotorTachoCount = 0;
		
		lock = new Object();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 * @since 1.1
	 */
	public void run() {
		long updateStart, updateEnd;

		while (true) {
			
//			System.out.println("L: " + Localization.isLocalizing);
			
//			System.out.println("X: " + x);
//			System.out.println("Y: " + y);
			
			updateStart = System.currentTimeMillis();
			double distLeft, distRight, deltaDistance, deltaTheta, dX, dY;
			distLeft = Math.PI * WHEEL_RADIUS * (leftMotor.getTachoCount() - leftMotorTachoCount) / 180; // Calculated
																													// using
																													// arclength
			distRight = Math.PI * WHEEL_RADIUS * (rightMotor.getTachoCount() - rightMotorTachoCount) / 180;
			leftMotorTachoCount = leftMotor.getTachoCount(); // Save current TachoCount (change in
																// angle) for next iteration
			rightMotorTachoCount = rightMotor.getTachoCount();
			deltaDistance = (distLeft + distRight) / 2; // Compute displacement of vehicle
			deltaTheta = (distLeft - distRight) / TRACK; // Compute heading

			synchronized (lock) {
				/**
				 * Don't use the variables x, y, or theta anywhere but here! Only update the values of x, y,
				 * and theta in this block. Do not perform complex math
				 * 
				 */
				theta += Math.toDegrees(deltaTheta); // Update the heading
				wrapAngle(theta);
				dX = deltaDistance * Math.sin(Math.toRadians(theta)); // Compute change in X using
																		// current heading
				dY = deltaDistance * Math.cos(Math.toRadians(theta)); // Compute change in Y using
																		// current heading

				x += dX; // Updated x pos
				y += dY; // Updated y pos
			}

			// this ensures that the odometer only runs once every period
			updateEnd = System.currentTimeMillis();
			if (updateEnd - updateStart < ODOMETER_PERIOD) {
				try {
					Thread.sleep(ODOMETER_PERIOD - (updateEnd - updateStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that the odometer will be interrupted by
					// another thread
				}
			}
		}
	}
	
	public void update() {
		if (this.theta < 45 || this.theta > 315 || (this.theta < 225 && this.theta > 135))
			synchronized (lock) {
				double diff = MainController.GRID_LENGTH - (this.x % MainController.GRID_LENGTH);
				this.x = diff < (MainController.GRID_LENGTH / 2) ? this.x - diff : this.x + (MainController.GRID_LENGTH - diff);
			}
		else if ((this.theta < 135 || this.theta > 45) || (this.theta > 225 && this.theta < 315))
			synchronized (lock) {
				double diff = MainController.GRID_LENGTH - (this.y % MainController.GRID_LENGTH);
				this.y = diff < (MainController.GRID_LENGTH / 2) ? this.y - diff : this.y + (MainController.GRID_LENGTH - diff);
			}
	}
	
	/**
	 * Gets the x, y and theta position indicated by the odometer.
	 * @param position Array with the 3 parameters (x, y, theta).
	 * @param update Array of boolean of whether or not the parameter will get recieved.
	 * @since 1.1
	 */
	public void getPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0])
				position[0] = x;
			if (update[1])
				position[1] = y;
			if (update[2])
				position[2] = theta;
		}
	}
	
	/**
	 * Getter for the x position reading of the odometer.
	 * @return X position
	 * @since 1.1
	 */
	public double getX() {
		double result;
		synchronized (lock) {
			result = x;
		}
		return result;
	}

	/**
	 * Getter for the y position reading of the odometer.
	 * @return Y position
	 * @since 1.1
	 */
	public double getY() {
		double result;
		synchronized (lock) {
			result = y;
		}
		return result;
	}

	/**
	 * Getter for the theta reading of the odometer.
	 * @return Theta heading
	 * @since 1.1
	 */
	public double getTheta() {
		double result;
		synchronized (lock) {
			result = theta;
		}
		return result;
	}

	/**
	 * Mutator for all the variables tracked by the odometer. 
	 * Which are X, Y, and Theta readings.
	 * @param position array of 3 doubles in the following order: X, Y, Theta.
	 * @param update array of 3 booleans that checks if the value should be changed in the following order: X, Y, Theta.
	 * @since 1.1
	 */
	public void setPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0])
				x = position[0];
			if (update[1])
				y = position[1];
			if (update[2])
				theta = position[2];
		}
	}

	/**
	 * Mutator for the X position of the odometer. 
	 * @param x a double that will change the X value to that.
	 * @since 1.1
	 */
	public void setX(double x) {
		synchronized (lock) {
			this.x = x;
		}
	}

	/**
	 * Mutator for the Y position of the odometer.
	 * @param y a double that will change the Y value to that.
	 * @since 1.1
	 */
	public void setY(double y) {
		synchronized (lock) {
			this.y = y;
		}
	}

	/**
	 * Mutator for the Theta heading of the odometer.
	 * @param theta a double that will change the Theta value to that. 
	 * @since 1.1
	 */
	public void setTheta(double theta) {
		synchronized (lock) {
			this.theta = theta;
		}
	}

	/**
	 * Getter for the left motor's tacho count.
	 * @return the left motor's tacho count (int)
	 * @since 1.1
	 */
	public int getLeftMotorTachoCount() {
		return leftMotorTachoCount;
	}

	
	/**
	 * Mutator for the left motor's tacho count.
	 * @param leftMotorTachoCount changes the tacho count to this parameter.
	 * @since 1.1
	 */
	public void setLeftMotorTachoCount(int leftMotorTachoCount) {
		synchronized (lock) {
			this.leftMotorTachoCount = leftMotorTachoCount;
		}
	}

	/**
	 * Getter for the right motor's tacho count. 
	 * @return the right motor's tacho count (int)
	 * @since 1.1
	 */
	public int getRightMotorTachoCount() {
		return rightMotorTachoCount;
	}

	/**
	 * Mutator for the right motor's tacho count.
	 * @param rightMotorTachoCount changes the tacho count to this parameter.
	 * @since 1.1
	 */
	public void setRightMotorTachoCount(int rightMotorTachoCount) {
		synchronized (lock) {
			this.rightMotorTachoCount = rightMotorTachoCount;
		}
	}

	/**
	 * Wraps angles of the odometer from 0 to 360 degrees.
	 * @param angle the theta heading of the odometer.
	 * @since 1.1
	 */
	public void wrapAngle(double angle) {
		if (angle < 0) {
			this.theta = ((angle % 360) + 360) % 360;
		}
		else {
			this.theta = angle % 360;
		}
	}
}
